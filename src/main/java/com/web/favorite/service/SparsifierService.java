package com.web.favorite.service;

import com.web.common.config.FavoriteTripListConf;
import com.web.common.exception.position.PositionOverflowException;
import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class SparsifierService {
    private FavoriteTripRepository favoriteTripRepository;
    private static final long POSITION_GAP = FavoriteTripListConf.POSITION_GAP.getValue();
    private static final long INITIAL_POSITION = FavoriteTripListConf.INITIAL_POSITION.getValue();

    @Value("${rebalancing-threshold-percent:0.8}")
    private static double rebalanceThreshold;

    @Transactional
    public long getNextAvailablePosition() {
        long maxPosition = favoriteTripRepository.findMaxPosition().orElse(0L);

        if (needRebalancing(maxPosition)){
            sparsify();
            maxPosition = favoriteTripRepository.findMaxPosition().orElse(0L);
            if (needRebalancing(maxPosition)){
                throw new PositionOverflowException(maxPosition, rebalanceThreshold);
            }
        }
        return maxPosition + POSITION_GAP;
    }

    @Transactional
    public void sparsify() {
        List<FavoriteTrip> trips = favoriteTripRepository.getFavouriteTripsByPositionAsc();

        if (!trips.isEmpty()) {
            AtomicLong currentPosition = new AtomicLong(INITIAL_POSITION);

            trips.forEach(trip -> {
                trip.setPosition(currentPosition.get());
                currentPosition.addAndGet(POSITION_GAP);
            });

            favoriteTripRepository.saveAll(trips);
        }
    }

    private boolean needRebalancing(long maxPosition) {
        return maxPosition > Long.MAX_VALUE * rebalanceThreshold;
    }
}
