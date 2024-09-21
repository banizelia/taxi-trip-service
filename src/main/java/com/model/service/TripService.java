package com.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.model.Trip;

import java.util.*;

@Service
public class TripService {
    @Autowired
    TripsRepository tripsRepository;

    public Optional<Trip> findById(String date) {
        return tripsRepository.findByPickupDate(date);
    }

    public Optional<List<Trip>> filterTrips(String startDateTime, String endDateTime, Double minWindSpeed, Double maxWindSpeed) {
        return tripsRepository.filterTrips(startDateTime, endDateTime, minWindSpeed, maxWindSpeed);
    }

}
