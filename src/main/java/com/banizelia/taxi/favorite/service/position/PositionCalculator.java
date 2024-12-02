package com.banizelia.taxi.favorite.service.position;

import com.banizelia.taxi.config.FavoriteTripListConfig;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionCalculator {
    private final FavoriteTripListConfig config;
    private final Sparsifier sparsifier;
    private final FavoriteTripRepository favoriteTripRepository;

    public long calculateNewPosition(Long targetPosition) {
        long totalCount = favoriteTripRepository.count();
        long newPosition;

        if (targetPosition > totalCount) {
            newPosition = calculateLastPosition();
        } else if (targetPosition == 1) {
            newPosition = calculateFirstPosition();
        } else {
            newPosition = calculateMiddlePosition(targetPosition);
        }

        return newPosition;
    }

    private long calculateFirstPosition() {
        long firstPosition = favoriteTripRepository.findMinPosition().orElse(config.getInitialPosition());

        if (firstPosition < config.getMinGap()) {
            sparsifier.sparsify();
            firstPosition = favoriteTripRepository.findMinPosition().orElse(config.getInitialPosition());
        }

        return firstPosition / 2;
    }

    private long calculateMiddlePosition(long targetPosition) {
        long prevIndex = targetPosition - 2;
        long nextIndex = targetPosition - 1;

        long prevPosition = favoriteTripRepository.findPositionByIndex(prevIndex).orElse(0L);
        long nextPosition = favoriteTripRepository.findPositionByIndex(nextIndex).orElse(prevPosition + config.getPositionGap());

        if ((nextPosition - prevPosition) < config.getMinGap()) {
            sparsifier.sparsify();

            prevPosition = favoriteTripRepository.findPositionByIndex(prevIndex).orElse(0L);
            nextPosition = favoriteTripRepository.findPositionByIndex(nextIndex).orElse(prevPosition + config.getPositionGap());
        }

        return prevPosition + (nextPosition - prevPosition) / 2;
    }

    public long calculateLastPosition() {
        long maxPosition = sparsifier.getNextAvailablePosition();

        if (maxPosition == 0) {
            maxPosition = config.getInitialPosition();
        } else {
            maxPosition += config.getInitialPosition();
        }
        return maxPosition;
    }
}