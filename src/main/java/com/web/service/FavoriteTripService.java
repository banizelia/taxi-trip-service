package com.web.service;

import com.web.model.FavoriteTrip;
import com.web.model.Trip;
import com.web.repository.FavoriteTripRepository;
import com.web.repository.TripsRepository;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
        try {
            if (tripId < 1){
                return ResponseEntity.badRequest().body("ID cant be smaller than 1");
            }

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

        } catch (OptimisticLockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict occurred while saving the trip");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred while saving to favorite trips");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Trip added to favorites");
    }

    public ResponseEntity<String> deleteFromFavourite(Long tripId) {
        try {
            Optional<FavoriteTrip> favoriteTripOptional = favoriteTripRepository.findByTripId(tripId);

            if (favoriteTripOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip not found");
            }

            favoriteTripRepository.delete(favoriteTripOptional.get());
            favoriteTripRepository.decrementPositionsAfter(favoriteTripOptional.get().getPosition());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred while deleting from favorite trips");
        }

        return ResponseEntity.ok("Trip deleted successfully");
    }

    public List<Trip> getFavouriteTrips() {
        return favoriteTripRepository.getFavouriteTrips();
    }

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
                return ResponseEntity.badRequest().body("Position is the same");
            }

            Long maxPosition = favoriteTripRepository.findMaxPosition();

            if (newPosition < 1 || newPosition >= maxPosition) {
                return ResponseEntity.badRequest().body("New position is out of bounds.");
            }

            if (newPosition < oldPosition) {
                favoriteTripRepository.incrementPositions(newPosition, oldPosition);
            } else {
                favoriteTripRepository.decrementPositions(oldPosition, newPosition);
            }

            favoriteTrip.setPosition(newPosition);
            favoriteTripRepository.save(favoriteTrip);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred while updating position");
        }

        return ResponseEntity.ok("Position updated successfully");
    }
}
