package com.web.favorite.service;

import com.web.common.exception.trip.TripAlreadyInFavoritesException;
import com.web.common.exception.trip.TripNotFoundException;
import com.web.favorite.model.FavoriteTrip;
import com.web.common.config.FavoriteTripListConf;
import com.web.favorite.repository.FavoriteTripRepository;
import com.web.trip.repository.TripsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaveFavoriteTripService {
    private static final long INITIAL_POSITION = FavoriteTripListConf.INITIAL_POSITION.getValue();

    private FavoriteTripRepository favoriteTripRepository;
    private TripsRepository tripsRepository;
    private SparsifierService sparsifier;

    @Transactional
    public synchronized void execute(Long tripId){
        if (favoriteTripRepository.findByTripId(tripId).isPresent()) {
            throw new TripAlreadyInFavoritesException(tripId);
        }

        if (!tripsRepository.existsById(tripId)) {
            throw new TripNotFoundException(tripId);
        }

        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setTripId(tripId);

        long maxPosition = sparsifier.getNextAvailablePosition();

        if (maxPosition == 0) {
            favoriteTrip.setPosition(INITIAL_POSITION);
        } else {
            favoriteTrip.setPosition(maxPosition+INITIAL_POSITION);
        }

        favoriteTripRepository.save(favoriteTrip);
    }
}