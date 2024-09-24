package com.model.service;

import com.model.Weather;
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

    public Optional<List<Trip>> test(){


        // эффективно ли такое решение разметки данных?
        List<Trip> trips = new ArrayList<>();


        for (Object[] objects : tripsRepository.test()) {

            Object[] tripParams =  Arrays.copyOfRange(objects, 0, 21);
            Trip trip = new Trip(tripParams);

            Object[] weatherParams =  Arrays.copyOfRange(objects, 21, objects.length);
            Weather weather = new Weather(weatherParams);

            trip.setWeather(weather);
            trips.add(trip);
        }

        return Optional.of(trips);
    }


    //     todo понять, почему соритровка выдает ошибку

    // итоговый запрос выглядит вот так:

    // SELECT trips.*, weather_observations.* FROM weather_observations
    // JOIN trips ON weather_observations.date = trips.pickup_datetime::DATE
    // WHERE (? IS NULL OR pickup_datetime >= ?) AND
    // (? IS NULL OR pickup_datetime <= ?) AND
    // (? IS NULL OR weather_observations.average_wind_speed >= ?) AND
    // (? IS NULL OR weather_observations.average_wind_speed <= ?)
    // order by JOIN.average_wind_speed asc

    // зачем добавляется "JOIN."
    public Optional<List<Trip>> filterTrips(String startDateTime, String endDateTime, Double minWindSpeed, Double maxWindSpeed, String direction, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

        List<Trip> trips = new ArrayList<>();

        for (Object[] objects : tripsRepository.filterTrips(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, sort)) {

            Object[] tripParams =  Arrays.copyOfRange(objects, 0, 21);
            Trip trip = new Trip(tripParams);

            Object[] weatherParams =  Arrays.copyOfRange(objects, 21, objects.length);
            Weather weather = new Weather(weatherParams);

            trip.setWeather(weather);
            trips.add(trip);
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
