package com.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.model.Trip;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TripService {
    @Autowired
    TripsRepository tripsRepository;

    //очень страшное решение, мне оно совершенно не нравится, но пока так, тут в целом все как будто страшно сделано
    public Optional<List<Trip>> filterTrips(String startDateTime, String endDateTime, Double minWindSpeed, Double maxWindSpeed, String sortBy, String direction) {
        List<Trip> trips = new ArrayList<>();

        for (Trip trip : tripsRepository.filterTrips(startDateTime, endDateTime, minWindSpeed, maxWindSpeed)) {
            trip.setAverage_wind_speed(tripsRepository.getWindSpeedById(trip.getId()));
        }

        switch (sortBy){
            case "pickup_datetime":
                trips = trips.stream().sorted(Comparator.comparing(a -> LocalDateTime.parse(a.getPickupDatetime()))).toList();
                break;
            case "average_wind_speed":
                trips = trips.stream().sorted(Comparator.comparing(a -> a.getAverage_wind_speed())).toList();
                break;
        }
        
        if (direction.equalsIgnoreCase("desc")){
            return Optional.of(trips.reversed());
        }

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
