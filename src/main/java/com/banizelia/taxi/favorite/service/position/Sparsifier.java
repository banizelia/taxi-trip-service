package com.banizelia.taxi.favorite.service.position;

import com.banizelia.taxi.config.FavoriteTripListConfig;
import com.banizelia.taxi.error.position.PositionOverflowException;
import com.banizelia.taxi.favorite.model.FavoriteTrip;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class Sparsifier {
    private final FavoriteTripRepository favoriteTripRepository;
    private final FavoriteTripListConfig favoriteTripListConfig;

    @Transactional
    public long getNextAvailablePosition() {
        long maxPosition = favoriteTripRepository.findMaxPosition().orElse(0L);

        if (needRebalancing(maxPosition)){
            sparsify();
            maxPosition = favoriteTripRepository.findMaxPosition().orElse(0L);
            if (needRebalancing(maxPosition)){
                throw new PositionOverflowException(maxPosition, favoriteTripListConfig.getRebalanceThreshold());
            }
        }
        return maxPosition + favoriteTripListConfig.getPositionGap();
    }

    @Transactional
    public void sparsify() {
        List<FavoriteTrip> trips = favoriteTripRepository.findAllByOrderByPositionAsc();

        if (!trips.isEmpty()) {
            AtomicLong currentPosition = new AtomicLong(favoriteTripListConfig.getInitialPosition());

            trips.forEach(trip -> {
                trip.setPosition(currentPosition.get());
                currentPosition.addAndGet(favoriteTripListConfig.getPositionGap());
            });
            favoriteTripRepository.saveAll(trips);
        }
    }

    private boolean needRebalancing(long maxPosition) {
        return maxPosition > Long.MAX_VALUE * favoriteTripListConfig.getRebalanceThreshold();
    }
}