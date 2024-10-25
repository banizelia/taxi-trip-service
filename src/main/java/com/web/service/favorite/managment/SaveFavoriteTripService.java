package com.web.service.favorite.managment;

import com.web.service.favorite.managment.common.SparsifierService;
import com.web.exceptions.TripNotFoundException;
import com.web.model.FavoriteTrip;
import com.web.model.enums.FavoriteTripEnum;
import com.web.repository.FavoriteTripRepository;
import com.web.repository.TripsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaveFavoriteTripService {
    private static final long INITIAL_POSITION = FavoriteTripEnum.INITIAL_POSITION.getValue();
    private static final long POSITION_GAP = FavoriteTripEnum.POSITION_GAP.getValue();
    private static final double MIN_GAP = FavoriteTripEnum.MIN_GAP.getValue();
    private static final double REBALANCE_THRESHOLD_PERCENT = FavoriteTripEnum.REBALANCE_THRESHOLD_PERCENT.getValue();

    private FavoriteTripRepository favoriteTripRepository;
    private TripsRepository tripsRepository;
    private SparsifierService sparsifier;

    @Transactional
    public synchronized void execute(Long tripId) throws BadRequestException {
        if (favoriteTripRepository.findByTripId(tripId).isPresent()) {
            throw new BadRequestException("Trip already in the table");
        }

        if (!tripsRepository.existsById(tripId)) {
            throw new TripNotFoundException("Such trip doesn't exist");
        }

        Long maxPosition = favoriteTripRepository.findMaxPosition();
        Long totalCount = favoriteTripRepository.count();

        if (needsRebalancing(maxPosition, totalCount)) {
            sparsifier.sparsify();
            maxPosition = favoriteTripRepository.findMaxPosition();
        }

        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setTripId(tripId);

        if (maxPosition == 0) {
            favoriteTrip.setPosition(INITIAL_POSITION);
        } else {
            long newPosition = calculateNextPosition(maxPosition);
            favoriteTrip.setPosition(newPosition);
        }

        favoriteTripRepository.save(favoriteTrip);
    }

    private boolean needsRebalancing(long maxPosition, long totalCount) {
        // Проверяем, достигли ли мы порога использования доступного диапазона
        if (maxPosition >= Long.MAX_VALUE * REBALANCE_THRESHOLD_PERCENT/100) {
            return true;
        }

        // Проверяем, не стали ли промежутки слишком маленькими
        if (totalCount > 0) {
            double averageGap = (double) maxPosition / totalCount;
            return averageGap < MIN_GAP;
        }

        return false;
    }

    private long calculateNextPosition(long maxPosition) {
        try {
            long newPosition = Math.addExact(maxPosition, POSITION_GAP);
            return newPosition;
        } catch (ArithmeticException e) {
            sparsifier.sparsify();
            return Math.addExact(favoriteTripRepository.findMaxPosition(), POSITION_GAP);
        }
    }
}