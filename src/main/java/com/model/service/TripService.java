package com.model.service;

import com.model.repository.TripsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.model.Trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TripService {
    @Autowired
    TripsRepository tripsRepository;

    //очень страшное решение, мне оно совершенно не нравится, но пока так, тут в целом все как будто страшно сделано
    public Optional<List<Trip>> filterTrips(String startDateTime, String endDateTime, Double minWindSpeed, Double maxWindSpeed, String sortBy, String direction) {
        List<Trip> trips = tripsRepository.filterTrips(startDateTime, endDateTime, minWindSpeed, maxWindSpeed);

        for (Trip trip : trips) {
            Long id = trip.getId();
            Double windSpeed = tripsRepository.getWindSpeedByTripId(id);
            trip.setAverageWindSpeed(windSpeed);
        }

        if (sortBy != null){
            switch (sortBy){
                case "pickup_datetime":
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    trips = trips.stream().sorted(Comparator.comparing(a -> LocalDateTime.parse(a.getPickupDatetime(), formatter))).toList();
                    break;
                case "average_wind_speed":
                    trips = trips.stream().sorted(Comparator.comparing(a -> a.getAverageWindSpeed())).toList();
                    break;
            }
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
