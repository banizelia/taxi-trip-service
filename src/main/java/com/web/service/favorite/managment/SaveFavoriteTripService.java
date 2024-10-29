package com.web.service.favorite.managment;

import com.web.service.favorite.managment.common.SparsifierService;
import com.web.exceptions.TripNotFoundException;
import com.web.model.FavoriteTrip;
import com.web.model.enums.FavoriteTripEnum;
import com.web.repository.FavoriteTripRepository;
import com.web.repository.TripsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaveFavoriteTripService {
    private static final long INITIAL_POSITION = FavoriteTripEnum.INITIAL_POSITION.getValue();
    private static final long POSITION_GAP = FavoriteTripEnum.POSITION_GAP.getValue();
    private static final double REBALANCE_THRESHOLD_PERCENT = FavoriteTripEnum.REBALANCE_THRESHOLD_PERCENT.getValue();

    private FavoriteTripRepository favoriteTripRepository;
    private TripsRepository tripsRepository;
    private SparsifierService sparsifier;

    @Transactional
    public synchronized void execute(Long tripId){
        if (favoriteTripRepository.findByTripId(tripId).isPresent()) {
            throw new IllegalArgumentException("Trip already in the table");
        }

        if (!tripsRepository.existsById(tripId)) {
            throw new TripNotFoundException("Such trip doesn't exist");
        }

        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setTripId(tripId);

        long maxPosition = sparsifier.sparsifyAndGetMaxPosition();

        if (maxPosition == 0) {
            favoriteTrip.setPosition(INITIAL_POSITION);
        } else {
            favoriteTrip.setPosition(maxPosition+INITIAL_POSITION);
        }

        favoriteTripRepository.save(favoriteTrip);
    }
}