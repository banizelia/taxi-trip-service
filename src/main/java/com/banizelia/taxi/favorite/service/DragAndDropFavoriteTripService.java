package com.banizelia.taxi.favorite.service;

import com.banizelia.taxi.error.position.PositionException;
import com.banizelia.taxi.error.trip.FavoriteTripModificationException;
import com.banizelia.taxi.error.trip.TripNotFoundException;
import com.banizelia.taxi.favorite.model.FavoriteTrip;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import com.banizelia.taxi.favorite.service.position.PositionCalculator;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DragAndDropFavoriteTripService {
    private final FavoriteTripRepository favoriteTripRepository;
    private final PositionCalculator positionCalculator;

    @Transactional
    public void execute(Long tripId, Long targetPosition) {
        if (targetPosition < 1) {
            throw new PositionException(String.format("Target position %d is out of bounds, id: %d, ", targetPosition, tripId));
        }

        FavoriteTrip favoriteTrip = favoriteTripRepository.findByTripId(tripId)
                .orElseThrow(() -> new TripNotFoundException(tripId));

        long newPosition = positionCalculator.calculateNewPosition(targetPosition);
        favoriteTrip.setPosition(newPosition);

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