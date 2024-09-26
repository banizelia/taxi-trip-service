package com.api.service;

import com.api.model.*;
import com.api.repository.FavoriteTripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class FavoriteTripService {
    @Autowired
    FavoriteTripRepository favoriteTripRepository;


    public ResponseEntity<String> saveToFavourite(Long id){
        try {
            if (favoriteTripRepository.existsById(id)) {
                return ResponseEntity.badRequest().body("Trip already in the table");
            }

            Long maxPosition = favoriteTripRepository.findMaxPosition().orElse(0L);

            FavoriteTrip favoriteTrip = new FavoriteTrip();
            favoriteTrip.setTripId(id);
            favoriteTrip.setPosition(maxPosition+1);
            favoriteTripRepository.save(favoriteTrip);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred while saving to favorite trips");
        }

        return ResponseEntity.ok("success");
    }

    public ResponseEntity<String> deleteFromFavourite(Long id) {
        try {
            FavoriteTrip favoriteTrip = favoriteTripRepository.getReferenceById(id);
            favoriteTripRepository.delete(favoriteTrip);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("not in the table");
        }

        return ResponseEntity.ok("success");
    }

    public Optional<List<Trip>> getFavouriteTrips() {
        List<Trip> trips = favoriteTripRepository.getFavouriteTrips();
        return Optional.of(trips);
    }


    @Transactional
    public ResponseEntity<String> dragAndDrop(Long tripId, Long newPosition) {
        try {
            FavoriteTrip favoriteTrip = favoriteTripRepository.getReferenceById(tripId);
            Long oldPosition = favoriteTrip.getPosition();

            if (newPosition.equals(oldPosition)) {
                return ResponseEntity.badRequest().body("Position is the same");
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
