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

/**
 * Service class for managing favorite trips.
 * This class provides methods for adding, removing, retrieving, and reordering favorite trips.
 */
@Service
public class FavoriteTripService {
    private final FavoriteTripRepository favoriteTripRepository;
    private final TripsRepository tripsRepository;

    public FavoriteTripService(TripsRepository tripsRepository, FavoriteTripRepository favoriteTripRepository) {
        this.tripsRepository = tripsRepository;
        this.favoriteTripRepository = favoriteTripRepository;
    }

    /**
     * Adds a trip to favorites.
     *
     * @param tripId The ID of the trip to be added to favorites.
     * @return ResponseEntity with a status message.
     */
    public ResponseEntity<String> saveToFavourite(Long tripId) {
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

    /**
     * Removes a trip from favorites.
     *
     * @param tripId The ID of the trip to be removed from favorites.
     * @return ResponseEntity with a status message.
     */
    public ResponseEntity<String> deleteFromFavourite(Long tripId) {
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

    /**
     * Retrieves all favorite trips.
     *
     * @return ResponseEntity containing a list of favorite trips.
     */
    public ResponseEntity<List<Trip>> getFavouriteTrips() {
        try {
            List<Trip> favoriteTrips = favoriteTripRepository.getFavouriteTrips();
            return ResponseEntity.ok(favoriteTrips);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Changes the position of a favorite trip in the list.
     *
     * @param tripId The ID of the trip to be moved.
     * @param newPosition The new position for the trip in the favorites list.
     * @return ResponseEntity with a status message.
     */
    @Transactional
    public ResponseEntity<String> dragAndDrop(Long tripId, Long newPosition) {
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

            if (newPosition > maxPosition) {
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

    /**
     * Validates and repairs the positions of favorite trips in the list.
     *
     * @return ResponseEntity with a status message.
     */
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