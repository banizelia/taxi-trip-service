package com.banizelia.taxi.favorite.service.position;

import com.banizelia.taxi.config.FavoriteTripListConfig;
import com.banizelia.taxi.error.position.PositionOverflowException;
import com.banizelia.taxi.favorite.model.FavoriteTrip;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class Sparsifier {
    private final FavoriteTripRepository favoriteTripRepository;
    private final FavoriteTripListConfig config;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public long getNextAvailablePosition() {
        long maxPosition = favoriteTripRepository.findMaxPosition().orElse(0L);

        if (isRebalancingNeeded(maxPosition)) {
            sparsify();
            maxPosition = favoriteTripRepository.findMaxPosition().orElse(0L);
            if (isRebalancingNeeded(maxPosition)) {
                throw new PositionOverflowException(maxPosition, config.getRebalanceThreshold());
            }
        }

        return maxPosition == 0 ? config.getInitialPosition() : (maxPosition + config.getPositionGap());
    }

    @Transactional
    public void sparsify() {
        AtomicLong currentPosition = new AtomicLong(config.getInitialPosition());
        AtomicInteger count = new AtomicInteger();

        Stream<FavoriteTrip> stream = favoriteTripRepository.findAllByOrderByPositionAscStream();

        stream.forEach(trip -> {
            trip.setPosition(currentPosition.getAndAdd(config.getPositionGap()));
            count.getAndIncrement();

            if (count.get() % config.getBatchSize() == 0) {
                flush();

                logUsedMemoryInMB();
            }

        });

        flush();
    }

    private boolean isRebalancingNeeded(long maxPosition) {
        return maxPosition > Long.MAX_VALUE * config.getRebalanceThreshold();
    }

    private void flush() {
        entityManager.flush();
        entityManager.clear();
    }

    private void logUsedMemoryInMB() {
        Runtime runtime = Runtime.getRuntime();

        long usedMemoryBytes = runtime.totalMemory() - runtime.freeMemory();

        log.info("Batch written. RAM Used: {} MB", usedMemoryBytes / (1024 * 1024));
    }
}