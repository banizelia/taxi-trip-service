package com.web.favorite.service.common;

import com.web.favorite.config.FavoriteTripListConf;
import com.web.common.exception.position.PositionOverflowException;
import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class SparsifierService {
    private final FavoriteTripRepository favoriteTripRepository;
    private final FavoriteTripListConf favoriteTripListConf;

    @Transactional
    public long getNextAvailablePosition() {
        long maxPosition = favoriteTripRepository.findMaxPosition().orElse(0L);

        if (needRebalancing(maxPosition)){
            sparsify();
            maxPosition = favoriteTripRepository.findMaxPosition().orElse(0L);
            if (needRebalancing(maxPosition)){
                throw new PositionOverflowException(maxPosition, favoriteTripListConf.getRebalanceThreshold());
            }
        }
        return maxPosition + favoriteTripListConf.getPositionGap();
    }

    @Transactional
    public void sparsify() {
        List<FavoriteTrip> trips = favoriteTripRepository.findAllByOrderByPositionAsc();

        if (!trips.isEmpty()) {
            AtomicLong currentPosition = new AtomicLong(favoriteTripListConf.getInitialPosition());

            trips.forEach(trip -> {
                trip.setPosition(currentPosition.get());
                currentPosition.addAndGet(favoriteTripListConf.getPositionGap());
            });
            favoriteTripRepository.saveAll(trips);
        }
    }

    private boolean needRebalancing(long maxPosition) {
        return maxPosition > Long.MAX_VALUE * favoriteTripListConf.getRebalanceThreshold();
    }
}