package com.web.service;

import com.web.model.FavoriteTrip;
import com.web.model.Trip;
import com.web.repository.FavoriteTripRepository;
import com.web.repository.TripsRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteTripService {
    @Autowired
    FavoriteTripRepository favoriteTripRepository;

    @Autowired
    TripsRepository tripsRepository;

    public ResponseEntity<String> saveToFavourite(Long tripId){
        if (tripId == null) {
            return ResponseEntity.badRequest().body("Trip ID cannot be null");
        }

        if (tripId < 1){
            return ResponseEntity.badRequest().body("ID cant be smaller than 1");
        }

        try {
            if (favoriteTripRepository.findByTripId(tripId).isPresent()) {
                return ResponseEntity.badRequest().body("Trip already in the table");
            }

            if (!tripsRepository.existsById(tripId)) {
                return ResponseEntity.badRequest().body("Such trip doesn't exist");
            }

            Long maxPosition = favoriteTripRepository.findMaxPosition();

            FavoriteTrip favoriteTrip = new FavoriteTrip();
            favoriteTrip.setTripId(tripId);
            favoriteTrip.setPosition(maxPosition + 1);

            favoriteTripRepository.save(favoriteTrip);

            return ResponseEntity.status(HttpStatus.CREATED).body("Trip added to favorites");

        } catch (OptimisticLockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict occurred while saving the trip");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data integrity violation occurred");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving to favorite trips: " + e.getMessage());
        }
    }

    public ResponseEntity<String> deleteFromFavourite(Long tripId) {
        if (tripId == null) {
            return ResponseEntity.badRequest().body("Trip ID cannot be null");
        }

        if (tripId < 1){
            return ResponseEntity.badRequest().body("ID cant be smaller than 1");
        }

        try {
            Optional<FavoriteTrip> favoriteTripOptional = favoriteTripRepository.findByTripId(tripId);

            if (favoriteTripOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip not found");
            }

            FavoriteTrip favoriteTrip = favoriteTripOptional.get();
            Long position = favoriteTrip.getPosition();

            favoriteTripRepository.delete(favoriteTrip);
            favoriteTripRepository.decrementPositionsAfter(position);

            return ResponseEntity.ok("Trip deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred while deleting from favorite trips");
        }
    }

    public ResponseEntity<List<Trip>> getFavouriteTrips() {
        try {
            List<Trip> favoriteTrips = favoriteTripRepository.getFavouriteTrips();
            return ResponseEntity.ok(favoriteTrips);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Transactional
    public ResponseEntity<String> dragAndDrop(Long tripId, Long newPosition) {
        if (tripId == null || newPosition == null) {
            return ResponseEntity.badRequest().body("Trip ID and new position cannot be null");
        }

        try {
            Optional<FavoriteTrip> favoriteTripOptional = favoriteTripRepository.findByTripId(tripId);

            if (favoriteTripOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip not found");
            }

            FavoriteTrip favoriteTrip = favoriteTripOptional.get();
            Long oldPosition = favoriteTrip.getPosition();

            if (newPosition.equals(oldPosition)) {
                return ResponseEntity.ok("Position remains unchanged");
            }

            Long maxPosition = favoriteTripRepository.findMaxPosition();

            if (newPosition < 1 || newPosition > maxPosition) {
                return ResponseEntity.badRequest().body("New position is out of bounds.");
            }

            if (newPosition < oldPosition) {
                favoriteTripRepository.incrementPositions(newPosition, oldPosition);
            } else {
                favoriteTripRepository.decrementPositions(oldPosition, newPosition);
            }

            favoriteTrip.setPosition(newPosition);
            favoriteTripRepository.save(favoriteTrip);

            return ResponseEntity.ok("Position updated successfully");
        } catch (OptimisticLockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict occurred while updating position");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating position: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<String> validatePositions() {
        try {
            List<FavoriteTrip> allFavorites = favoriteTripRepository.findAllByOrderByPositionAsc();
            long expectedPosition = 1;

            for (FavoriteTrip trip : allFavorites) {
                if (trip.getPosition() != expectedPosition) {
                    trip.setPosition(expectedPosition);
                    favoriteTripRepository.save(trip);
                }
                expectedPosition++;
            }

            return ResponseEntity.ok("Positions validated and repaired successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while validating positions: " + e.getMessage());
        }
    }
}
