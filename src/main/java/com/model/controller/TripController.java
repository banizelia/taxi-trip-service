package com.model.controller;

import com.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.model.service.TripService;

import java.util.*;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/filter")
    public Optional<List<Trip>> filterTrips(
            @RequestParam(value = "startDateTime", required = false) String startDateTime,
            @RequestParam(value = "endDateTime", required = false) String endDateTime,
            @RequestParam(value = "minWindSpeed", required = false) Double minWindSpeed,
            @RequestParam(value = "maxWindSpeed", required = false) Double maxWindSpeed,
            @RequestParam(value = "sortBy", required = false, defaultValue = "pickup_datetime") String sortBy, // pickup_datetime or average_wind_speed
            @RequestParam(value = "direction", required = false, defaultValue = "asc") String direction) { // asc or desc

        return tripService.filterTrips(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, sortBy, direction);
    }

    @GetMapping("/addToFavourite")
    public void addToFavourite(@RequestParam(value = "id") Long id){
        tripService.addToFavourite(id);
    }

    @GetMapping("/removeFromFavourite")
    public void removeFromFavourite(@RequestParam(value = "id") Long id){
        tripService.removeFromFavourite(id);
    }

    @GetMapping("/getFavouriteList")
    public Optional<List<Trip>> getFavouriteList(){
        return  tripService.getFavouriteList();
    }
}