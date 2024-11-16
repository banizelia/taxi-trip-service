package com.web.favorite.service.dragAndDrop;

import com.web.common.FavoriteTripConf;
import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@AllArgsConstructor
public class SparsifierService {
    private static final long POSITION_GAP = FavoriteTripConf.POSITION_GAP.getValue();

    // Если список достигнет % от MAX_VALUE, произойдет ребалансировка, перенести в конфиг
    private static final double REBALANCE_THRESHOLD_PERCENT = 80.0;

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
            AtomicLong currentPosition = new AtomicLong(FavoriteTripConf.INITIAL_POSITION.getValue());

            trips.forEach(trip -> {
                trip.setPosition(currentPosition.get());
                currentPosition.addAndGet(POSITION_GAP);
            });

            favoriteTripRepository.saveAll(trips);
        }
    }
}
