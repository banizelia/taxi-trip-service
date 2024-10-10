package com.model.service;

import com.model.repository.TripsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.model.Trip;

import java.util.*;

@Service
public class TripService {
    @Autowired
    TripsRepository tripsRepository;

    public Optional<List<Trip>> filterTrips(String startDateTime, String endDateTime, Double minWindSpeed, Double maxWindSpeed, String direction, String sortBy) {


        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

        List<Trip> trips = tripsRepository.filterTrips(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, sort);

        return Optional.of(trips);
    }


    public void addToFavourite(Long id) {
        tripsRepository.addToFavourite(id);
    }

    public void removeFromFavourite(Long id) {
        tripsRepository.removeFromFavourite(id);
    }

    public Optional<List<Trip>> getFavouriteList() {
        List<Trip> trips = tripsRepository.getFavouriteList();

        return Optional.of(trips);
    }
}
