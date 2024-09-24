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


    @GetMapping("/test")
    public Optional<List<Trip>> test(){
        return tripService.test();
    }

//    @GetMapping("/filter")
//    public Optional<List<Trip>> filterTrips(
//            @RequestParam(value = "startDateTime", required = false) String startDateTime,
//            @RequestParam(value = "endDateTime", required = false) String endDateTime,
//            @RequestParam(value = "minWindSpeed", required = false) Double minWindSpeed,
//            @RequestParam(value = "maxWindSpeed", required = false) Double maxWindSpeed,
//            @RequestParam(value = "direction", required = false, defaultValue = "asc") String direction, /* asc or desc */
//            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy /* pickup_datetime or average_wind_speed */ ) {
//
//
//        Optional<List<Trip>> result = tripService.filterTrips(startDateTime, endDateTime, minWindSpeed, maxWindSpeed , direction, sortBy);
//
//        return result;
//    }
//
//    @PutMapping("/addToFavourite")
//    public ResponseEntity<String> addToFavourite(@RequestParam(value = "id") Long id){
////        try {
//            tripService.addToFavourite(id);
//            return ResponseEntity.ok("Success");
////        } catch (Exception e) {
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add to favourites");
////        }
//    }
//
//    @DeleteMapping("/removeFromFavourite")
//    public ResponseEntity<String> removeFromFavourite(@RequestParam(value = "id") Long id){
////        try {
//            tripService.removeFromFavourite(id);
//            return ResponseEntity.ok("Success");
////        } catch (Exception e) {
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to remove from favourites");
////        }
//    }
//
//    @GetMapping("/getFavouriteList")
//    public Optional<List<Trip>> getFavouriteList(){
//        return tripService.getFavouriteList();
//    }
}