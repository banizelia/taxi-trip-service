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
            @RequestParam(value = "maxWindSpeed", required = false) Double maxWindSpeed) {


        return tripService.filterTrips(startDateTime, endDateTime, minWindSpeed, maxWindSpeed);
    }

}