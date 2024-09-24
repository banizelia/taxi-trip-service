package com.model.controller;

import com.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.model.service.TripService;

import java.sql.Timestamp;
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
    public Optional<List<Trip>> filter(
            @RequestParam(value = "startDateTime", required = false, defaultValue = "2016-01-01 00:00:00") Timestamp startDateTime,
            @RequestParam(value = "endDateTime", required = false, defaultValue = "2016-01-31 23:59:59") Timestamp endDateTime,
            @RequestParam(value = "minWindSpeed", required = false, defaultValue = "0") Double minWindSpeed,
            @RequestParam(value = "maxWindSpeed", required = false, defaultValue = "9999") Double maxWindSpeed,
            @RequestParam(value = "direction", required = false, defaultValue = "asc") String direction, /* asc or desc */
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy /* pickup_datetime or average_wind_speed */){

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

        Optional<List<Trip>> trips = tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, sort);

        return trips;
    }

    @PutMapping("/addToFavourite")
    public ResponseEntity<String> addToFavourite(@RequestParam(value = "id") Long id){

        return tripService.addToFavourite(id);
    }

    @DeleteMapping("/removeFromFavourite")
    public ResponseEntity<String> removeFromFavourite(@RequestParam(value = "id") Long id){

        return tripService.removeFromFavourite(id);
    }

    @GetMapping("/getFavouriteList")
    public Optional<List<Trip>> getFavouriteList(){
        return tripService.getFavouriteList();
    }
}