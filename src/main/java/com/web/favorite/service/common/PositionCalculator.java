package com.web.favorite.service.common;

import com.web.favorite.config.FavoriteTripListConf;
import com.web.favorite.repository.FavoriteTripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionCalculator {
    private final FavoriteTripListConf config;
    private final SparsifierService sparsifier;
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

        if (firstPosition < config.getPositionGap()) {
            sparsifier.sparsify();
            firstPosition = favoriteTripRepository.findMinPosition().orElse(config.getInitialPosition());
        }

        return firstPosition / 2;
    }

    private long calculateMiddlePosition(long targetPosition) {
        long prevPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 2).orElse(0L);
        long nextPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 1)
                .orElse(prevPosition + config.getPositionGap());

        if ((nextPosition - prevPosition) < config.getMinGap()) {
            sparsifier.sparsify();

            prevPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 2).orElse(0L);
            nextPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 1).orElse(prevPosition + config.getPositionGap());
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