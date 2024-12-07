package com.banizelia.taxi.favorite.service;

import com.banizelia.taxi.error.trip.FavoriteTripModificationException;
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
        validateTripId(tripId);
        FavoriteTrip favoriteTrip = createFavoriteTrip(tripId);
        saveFavoriteTrip(favoriteTrip);
    }

    private void validateTripId(Long tripId) {
        if (favoriteTripRepository.findByTripId(tripId).isPresent()) {
            throw new TripAlreadyInFavoritesException(tripId);
        }

        if (!tripsRepository.existsById(tripId)) {
            throw new TripNotFoundException(tripId);
        }
    }

    private FavoriteTrip createFavoriteTrip(Long tripId) {
        long maxPosition = positionCalculator.lastPosition();
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setTripId(tripId);
        favoriteTrip.setPosition(maxPosition);
        return favoriteTrip;
    }

    private void saveFavoriteTrip(FavoriteTrip favoriteTrip) {
        try {
            favoriteTripRepository.save(favoriteTrip);
        } catch (OptimisticLockException e) {
            log.warn("Optimistic lock exception while saving favoriteTrip: id={}, tripId={}, position={}",
                    favoriteTrip.getId(),
                    favoriteTrip.getTripId(),
                    favoriteTrip.getPosition(),
                    e
            );
            throw new FavoriteTripModificationException("Failed to save FavoriteTrip due to concurrent modification", e);
        }
    }
}