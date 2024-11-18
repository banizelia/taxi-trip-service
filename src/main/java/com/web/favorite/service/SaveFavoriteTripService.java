package com.web.favorite.service;

import com.web.common.exception.trip.TripAlreadyInFavoritesException;
import com.web.common.exception.trip.TripNotFoundException;
import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
import com.web.favorite.service.common.PositionCalculator;
import com.web.trip.repository.TripsRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SaveFavoriteTripService {
    private FavoriteTripRepository favoriteTripRepository;
    private TripsRepository tripsRepository;
    private PositionCalculator positionCalculator;

    @Transactional
    public void execute(Long tripId){
        if (favoriteTripRepository.findByTripId(tripId).isPresent()) {
            throw new TripAlreadyInFavoritesException(tripId);
        }

        if (!tripsRepository.existsById(tripId)) {
            throw new TripNotFoundException(tripId);
        }

        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setTripId(tripId);

        long maxPosition = positionCalculator.calculateLastPosition();
        favoriteTrip.setPosition(maxPosition);

        try {
            favoriteTripRepository.save(favoriteTrip);
        } catch (OptimisticLockException e) {
            log.warn("Optimistic lock exception while saving favoriteTrip: id={}, tripId={}, position={}",
                    favoriteTrip.getId(),
                    favoriteTrip.getTripId(),
                    favoriteTrip.getPosition(),
                    e
            );
            throw e;
        }
    }
}