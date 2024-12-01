package com.web.favorite.service;

import com.web.common.exception.trip.TripAlreadyInFavoritesException;
import com.web.common.exception.trip.TripNotFoundException;
import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
import com.web.favorite.service.position.PositionCalculator;
import com.web.trip.repository.TripsRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveFavoriteTripService {
    private final FavoriteTripRepository favoriteTripRepository;
    private final TripsRepository tripsRepository;
    private final PositionCalculator positionCalculator;

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