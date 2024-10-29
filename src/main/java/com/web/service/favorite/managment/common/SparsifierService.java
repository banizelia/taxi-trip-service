package com.web.service.favorite.managment.common;

import com.web.model.FavoriteTrip;
import com.web.model.enums.FavoriteTripEnum;
import com.web.repository.FavoriteTripRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;



@Service
@AllArgsConstructor
public class SparsifierService {
    private static final long POSITION_GAP = FavoriteTripEnum.POSITION_GAP.getValue();
    private static final double REBALANCE_THRESHOLD_PERCENT = FavoriteTripEnum.REBALANCE_THRESHOLD_PERCENT.getValue();

    private FavoriteTripRepository favoriteTripRepository;

    @Transactional
    public long sparsifyAndGetMaxPosition() {
        long maxPosition = favoriteTripRepository.findMaxPosition();

        if (maxPosition > Long.MAX_VALUE * REBALANCE_THRESHOLD_PERCENT / 100){
            sparsify();

            maxPosition = favoriteTripRepository.findMaxPosition();
            if (maxPosition > Long.MAX_VALUE * REBALANCE_THRESHOLD_PERCENT / 100){
                throw new IllegalStateException("Unable to calculate next position even after sparsification");
            }
        }

        return maxPosition;
    }

    @Transactional
    public void sparsify() {
        List<FavoriteTrip> trips = favoriteTripRepository.getFavouriteTripsByPositionAsc();

        if (!trips.isEmpty()) {
            AtomicLong currentPosition = new AtomicLong(FavoriteTripEnum.INITIAL_POSITION.getValue());

            trips.forEach(trip -> {
                trip.setPosition(currentPosition.get());
                currentPosition.addAndGet(POSITION_GAP);
            });

            favoriteTripRepository.saveAll(trips);
        }
    }

    @Transactional
    public void sparsifyInRange(long startPosition, long endPosition) {
        List<FavoriteTrip> tripsInRange = favoriteTripRepository.findAllByPositionBetweenOrderByPositionAsc(startPosition, endPosition);

        if (!tripsInRange.isEmpty()) {

            final long newGap;

            if (tripsInRange.size() > 1) {
                newGap = (endPosition - startPosition) / (tripsInRange.size() - 1);
            } else {
                newGap = POSITION_GAP;
            }

            // Распределяем позиции равномерно в заданном диапазоне
            AtomicLong currentPosition = new AtomicLong(startPosition);
            tripsInRange.forEach(trip -> {
                trip.setPosition(currentPosition.get());
                currentPosition.addAndGet(newGap);
            });

            favoriteTripRepository.saveAll(tripsInRange);
        }
    }
}
