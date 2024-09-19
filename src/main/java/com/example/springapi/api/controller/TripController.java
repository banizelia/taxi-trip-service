package com.example.springapi.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.springapi.api.model.Trip;
import com.example.springapi.service.TripService;

@RestController
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripById(@PathVariable Integer tripId) {
        Trip trip = tripService.getTripById(tripId);
        if (trip != null) {
            return ResponseEntity.ok(trip);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("test");
        return "test";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
}