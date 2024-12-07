package com.banizelia.taxi.favorite.service.position;

import com.banizelia.taxi.config.FavoriteTripListConfig;
import com.banizelia.taxi.error.position.PositionException;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionCalculator {
    private final FavoriteTripRepository favoriteTripRepository;
    private final FavoriteTripListConfig config;
    private final Sparsifier sparsifier;


    public long calculateNewPosition(Long targetPosition) {
        if (targetPosition < 1) {
            throw new PositionException("Target position < 1: " + targetPosition);
        }

        long count = favoriteTripRepository.count();
        if (targetPosition > count)
            return lastPosition();
        if (targetPosition == 1)
            return firstPosition();
        return middlePosition(targetPosition);
    }

    private long firstPosition() {
        long firstPosition = favoriteTripRepository.findMinPosition().orElse(config.getInitialPosition());

        if (firstPosition < config.getMinGap()) {
            sparsifier.sparsify();
            firstPosition = favoriteTripRepository.findMinPosition().orElse(config.getInitialPosition());
        }

        return firstPosition / 2;
    }

    private long middlePosition(long targetPosition) {
        long prevPos = positionByIndex(targetPosition-2, 0L);
        long nextPos = positionByIndex(targetPosition-1, prevPos + config.getPositionGap());


        if ((nextPos - prevPos) < config.getMinGap()) {
            sparsifier.sparsify();
            prevPos = positionByIndex(targetPosition - 2, 0L);
            nextPos = positionByIndex(targetPosition - 1, prevPos + config.getPositionGap());
        }
        return prevPos + (nextPos - prevPos) / 2;
    }

    public long lastPosition() {
        return sparsifier.getNextAvailablePosition();
    }

    private long positionByIndex(long idx, long fallback) {
        return favoriteTripRepository.findPositionByIndex(idx).orElse(fallback);
    }
}