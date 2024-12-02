package com.banizelia.taxi.favorite.service;

import com.banizelia.taxi.error.trip.TripAlreadyInFavoritesException;
import com.banizelia.taxi.error.trip.TripNotFoundException;
import com.banizelia.taxi.favorite.model.FavoriteTrip;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import com.banizelia.taxi.favorite.service.position.PositionCalculator;
import com.banizelia.taxi.trip.repository.TripsRepository;
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