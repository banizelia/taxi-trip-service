package com.web.service.favorite.managment;

import com.web.model.enums.FavoriteTripEnum;
import com.web.service.favorite.managment.common.SparsifierService;
import com.web.exceptions.TripNotFoundException;
import com.web.model.FavoriteTrip;
import com.web.repository.FavoriteTripRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DragAndDropFavoriteTripService {
    private static final long POSITION_GAP = FavoriteTripEnum.POSITION_GAP.getValue();
    private static final long MIN_GAP = FavoriteTripEnum.MIN_GAP.getValue();
    private static final long INITIAL_POSITION = FavoriteTripEnum.INITIAL_POSITION.getValue();

    private FavoriteTripRepository favoriteTripRepository;
    private SparsifierService sparsifier;

    @Transactional
    public void execute(Long tripId, Long targetPosition) {
        FavoriteTrip favoriteTrip = favoriteTripRepository.findByTripId(tripId)
                .orElseThrow(() -> new TripNotFoundException("Trip not found"));

        long totalCount = favoriteTripRepository.count();

        if (targetPosition < 1 ) {
            throw new IllegalArgumentException("Target position is out of bounds");
        }

        if (targetPosition > totalCount){
            favoriteTrip.setPosition(sparsifier.sparsifyAndGetMaxPosition() + POSITION_GAP);
            favoriteTripRepository.save(favoriteTrip);
            return;
        }

        favoriteTrip.setPosition(calculateNewPosition(targetPosition));

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
            sparsifier.sparsifyInRange(0, firstPosition + POSITION_GAP * 2);
            firstPosition = favoriteTripRepository.findMinPosition().orElse(INITIAL_POSITION);
        }

        return firstPosition / 2;
    }

    private long calculateMiddlePosition(long targetPosition) {

        long prevPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 2).orElse(0L);
        long nextPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 1)
                .orElse(prevPosition + POSITION_GAP);

        if ((nextPosition - prevPosition) < MIN_GAP) {
            long rangeStart = Math.max(0, prevPosition - POSITION_GAP);
            long rangeEnd = nextPosition + POSITION_GAP * 2;

            sparsifier.sparsifyInRange(rangeStart, rangeEnd);

            prevPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 2).orElse(0L);
            nextPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 1).orElse(prevPosition + POSITION_GAP);
        }

        return prevPosition + (nextPosition - prevPosition) / 2;
    }
}