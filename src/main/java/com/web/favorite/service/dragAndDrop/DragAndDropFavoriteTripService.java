package com.web.favorite.service.dragAndDrop;

import com.web.common.FavoriteTripConf;
import com.web.common.exception.TripNotFoundException;
import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DragAndDropFavoriteTripService {
    private static final long POSITION_GAP = FavoriteTripConf.POSITION_GAP.getValue();
    private static final long MIN_GAP = FavoriteTripConf.MIN_GAP.getValue();
    private static final long INITIAL_POSITION = FavoriteTripConf.INITIAL_POSITION.getValue();

    private FavoriteTripRepository favoriteTripRepository;
    private SparsifierService sparsifier;

    @Transactional
    public void execute(Long tripId, Long targetPosition) {
        if (targetPosition < 1 ) {
            throw new IllegalArgumentException("Target position is out of bounds");
        }

        FavoriteTrip favoriteTrip = favoriteTripRepository.findByTripId(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found"));

        long totalCount = favoriteTripRepository.count();
        long newPosition;

        if (targetPosition > totalCount){
            newPosition = sparsifier.getNextAvailablePosition();
        } else {
            newPosition = calculateNewPosition(targetPosition);
        }

        favoriteTrip.setPosition(newPosition);
        favoriteTripRepository.save(favoriteTrip);
    }

    private long calculateNewPosition(long targetPosition){
        if (targetPosition == 1) {
            return calculateFirstPosition();
        } else {
            return calculateMiddlePosition(targetPosition);
        }
    }

    private long calculateFirstPosition() {
        long firstPosition = favoriteTripRepository.findMinPosition().orElse(INITIAL_POSITION);

        if (firstPosition < MIN_GAP) {
            sparsifier.sparsify();
            firstPosition = favoriteTripRepository.findMinPosition().orElse(INITIAL_POSITION);
        }

        return firstPosition / 2;
    }

    private long calculateMiddlePosition(long targetPosition) {
        long prevPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 2).orElse(0L);
        long nextPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 1)
                .orElse(prevPosition + POSITION_GAP);

        if ((nextPosition - prevPosition) < MIN_GAP) {
            sparsifier.sparsify();

            prevPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 2).orElse(0L);
            nextPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 1).orElse(prevPosition + POSITION_GAP);
        }

        return prevPosition + (nextPosition - prevPosition) / 2;
    }
}