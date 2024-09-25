package com.api.service;

import com.api.model.FavoriteTrip;
import com.api.model.Trip;
import com.api.repository.FavoriteTripRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            FavoriteTrip favoriteTrip = new FavoriteTrip();
            favoriteTrip.setTripId(id);
            favoriteTripRepository.save(favoriteTrip);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("already in the table");
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
        System.out.println(trips.size());
        return Optional.of(trips);
    }
}
