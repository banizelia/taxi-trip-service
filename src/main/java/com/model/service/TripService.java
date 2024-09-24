package com.model.service;

import com.model.repository.TripsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.model.Trip;

import java.sql.Timestamp;
import java.util.*;

@Service
public class TripService {
    @Autowired
    TripsRepository tripsRepository;




    public Optional<List<Trip>> filter(Timestamp startDateTime, Timestamp endDateTime, Double minWindSpeed, Double maxWindSpeed, Sort sort) {
        List<Trip> trips = tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, sort, Limit.of(1_000));

        return Optional.of(trips);
    }

    public ResponseEntity<String> addToFavourite(Long id) {
        if (tripsRepository.isTripInFavourite(id)) {
            return ResponseEntity.badRequest().body("Already in the table");
        } else {
            tripsRepository.addToFavourite(id);
            return ResponseEntity.ok("Added");
        }
    }


    public ResponseEntity<String> removeFromFavourite(Long id) {
        if (!tripsRepository.isTripInFavourite(id)) {
            return ResponseEntity.badRequest().body("Not in the table");
        } else {
            tripsRepository.removeFromFavourite(id);
            return ResponseEntity.ok("Added");
        }
    }


    public Optional<List<Trip>> getFavouriteList() {
        List<Trip> trips = tripsRepository.getFavouriteList();
        return Optional.of(trips);
    }
}
