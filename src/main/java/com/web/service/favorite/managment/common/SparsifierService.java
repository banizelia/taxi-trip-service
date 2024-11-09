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
    public long getNextAvailablePosition() {
        long maxPosition = favoriteTripRepository.findMaxPosition().orElse(0L);

        if (maxPosition > Long.MAX_VALUE * REBALANCE_THRESHOLD_PERCENT / 100){
            sparsify();

            maxPosition = favoriteTripRepository.findMaxPosition().orElse(0L);
            if (maxPosition > Long.MAX_VALUE * REBALANCE_THRESHOLD_PERCENT / 100){
                throw new IllegalStateException("Unable to calculate next position even after sparsification");
            }
        }

        return maxPosition + POSITION_GAP;
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
}
