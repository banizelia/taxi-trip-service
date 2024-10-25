package com.web.service.favorite.managment;

import com.web.model.enums.FavoriteTripEnum;
import com.web.service.favorite.managment.common.SparsifierService;
import com.web.exceptions.TripNotFoundException;
import com.web.model.FavoriteTrip;
import com.web.repository.FavoriteTripRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DragAndDropFavoriteTripService {
    private static final long POSITION_GAP = FavoriteTripEnum.POSITION_GAP.getValue();
    private static final long MIN_GAP = FavoriteTripEnum.MIN_GAP.getValue();

    private FavoriteTripRepository favoriteTripRepository;
    private SparsifierService sparsifier;

    @Transactional
    public void execute(Long tripId, Long targetPosition) throws BadRequestException {
        Optional<FavoriteTrip> favoriteTripOptional = favoriteTripRepository.findByTripId(tripId);

        if (favoriteTripOptional.isEmpty()) {
            throw new TripNotFoundException("Trip not found");
        }

        FavoriteTrip favoriteTrip = favoriteTripOptional.get();

        long totalCount = favoriteTripRepository.count();

        if (targetPosition < 1 || targetPosition > totalCount) {
            throw new BadRequestException("Target position is out of bounds");
        }

        Long prevPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 1).orElse(0L);
        Long nextPosition = favoriteTripRepository.findPositionByIndex(targetPosition).orElse(POSITION_GAP);

        if ((nextPosition - prevPosition) < MIN_GAP) {
            sparsifier.sparsify();

            prevPosition = favoriteTripRepository.findPositionByIndex(targetPosition - 1).orElse(0L);
            nextPosition = favoriteTripRepository.findPositionByIndex(targetPosition).orElse(POSITION_GAP);
        }

        long newPosition = prevPosition + (nextPosition - prevPosition) / 2;

        favoriteTrip.setPosition(newPosition);
        favoriteTripRepository.save(favoriteTrip);
    }
}
