package com.web.service;

import com.web.model.FavoriteTrip;
import com.web.model.Trip;
import com.web.repository.FavoriteTripRepository;
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


    public ResponseEntity<String> saveToFavourite(Long id){
        try {
            if (favoriteTripRepository.existsById(id)) {
                return ResponseEntity.badRequest().body("Trip already in the table");
            }

            Long maxPosition = favoriteTripRepository.findMaxPosition();

            FavoriteTrip favoriteTrip = new FavoriteTrip();
            favoriteTrip.setTripId(id);
            favoriteTrip.setPosition(maxPosition + 1);

            favoriteTripRepository.save(favoriteTrip);

        } catch (OptimisticLockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict occurred while saving the trip");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred while saving to favorite trips");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Trip added to favorites");
    }

    public ResponseEntity<String> deleteFromFavourite(Long id) {
        Optional<FavoriteTrip> favoriteTripOptional = favoriteTripRepository.findById(id);

        if (favoriteTripOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trip not found");
        }

        favoriteTripRepository.delete(favoriteTripOptional.get());
        return ResponseEntity.ok("Trip deleted successfully");
    }

    public List<Trip> getFavouriteTrips() {
        return favoriteTripRepository.getFavouriteTrips();
    }


    @Transactional
    public ResponseEntity<String> dragAndDrop(Long tripId, Long newPosition) {
        try {
            FavoriteTrip favoriteTrip = favoriteTripRepository.getReferenceById(tripId);
            Long oldPosition = favoriteTrip.getPosition();

            if (newPosition.equals(oldPosition)) {
                return ResponseEntity.badRequest().body("Position is the same");
            }

            Long maxPosition = favoriteTripRepository.findMaxPosition();

            if (newPosition < 0 || newPosition >= maxPosition) {
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred while updating position");
        }
    }

}
