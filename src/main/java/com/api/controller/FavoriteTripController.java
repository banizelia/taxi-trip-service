package com.api.controller;

import com.api.model.Trip;
import com.api.service.FavoriteTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favouriteTrips")
public class FavoriteTripController {
    private final FavoriteTripService favoriteTripService;

    @Autowired
    public FavoriteTripController(FavoriteTripService favoriteTripService) {
        this.favoriteTripService = favoriteTripService;
    }

    @GetMapping("/getFavouriteTrips")
    public Optional<List<Trip>> getFavouriteTrips(){
        return favoriteTripService.getFavouriteTrips();
    }

    @PutMapping("/saveToFavourite")
    public ResponseEntity<String> saveToFavourite(@RequestParam(value = "tripId") Long id){
        return favoriteTripService.saveToFavourite(id);
    }

    @DeleteMapping("/deleteFromFavouriteByTripId")
    public ResponseEntity<String> deleteFromFavourite(@RequestParam(value = "tripId") Long id){
        return favoriteTripService.deleteFromFavourite(id);
    }


    @PutMapping("/dragAndDrop")
    public ResponseEntity<String> dragAndDrop(@RequestParam(value = "tripId") Long tripId, @RequestParam(value = "newPosition") Long newPosition) {
        return favoriteTripService.dragAndDrop(tripId, newPosition);
    }
}
