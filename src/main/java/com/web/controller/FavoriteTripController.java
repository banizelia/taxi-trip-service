package com.web.controller;

import com.web.model.Trip;
import com.web.service.FavoriteTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/favorite-trips")
public class FavoriteTripController {
    private final FavoriteTripService favoriteTripService;

    @Autowired
    public FavoriteTripController(FavoriteTripService favoriteTripService) {
        this.favoriteTripService = favoriteTripService;
    }

    @GetMapping()
    public List<Trip> getFavouriteTrips(){
        return favoriteTripService.getFavouriteTrips();
    }

    @PutMapping()
    public ResponseEntity<String> saveToFavourite(@RequestParam(value = "tripId") Long id){
        return favoriteTripService.saveToFavourite(id);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteFromFavourite(@RequestParam(value = "tripId") Long id){
        return favoriteTripService.deleteFromFavourite(id);
    }
    
    @PutMapping("/dragAndDrop")
    public ResponseEntity<String> dragAndDrop(@RequestParam(value = "tripId") Long tripId, @RequestParam(value = "newPosition") Long newPosition) {
        return favoriteTripService.dragAndDrop(tripId, newPosition);
    }
}
