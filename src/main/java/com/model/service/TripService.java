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


        // todo понять, как размапить данные на trip, можно чисто в теории сделать конструктор, который будет принимать array и вызывать миллион методов
        // эффективно ли такое решение, оно дважды проходится по списку, в иделе сразу получать нужные данные

        List<Trip> trips = new ArrayList<>();


        for (Object[] objects : tripsRepository.test()) {
            Object[] tripParams =  Arrays.copyOfRange(objects, 0, 21);
            Object[] weatherParams =  Arrays.copyOfRange(objects, 21, objects.length);

            Trip trip = new Trip(tripParams);
            Weather weather = new Weather(weatherParams);
            trip.setWeather(weather);

            trips.add(trip);
        }

        return Optional.of(trips);
    }


//    public Optional<List<Trip>> filterTrips(String startDateTime, String endDateTime, Double minWindSpeed, Double maxWindSpeed, String direction, String sortBy) {
//
//

    // todo понять, почему соритровка выдает ошибку

//        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
//
//        List<Trip> trips = tripsRepository.filterTrips(startDateTime, endDateTime, minWindSpeed, maxWindSpeed /*, sort*/);
//
//        return Optional.of(trips);
//    }
//
//
//    public void addToFavourite(Long id) {
//        tripsRepository.addToFavourite(id);
//    }
//
//    public void removeFromFavourite(Long id) {
//        tripsRepository.removeFromFavourite(id);
//    }
//
//    public Optional<List<Trip>> getFavouriteList() {
//        List<Trip> trips = tripsRepository.getFavouriteList();
//
//        return Optional.of(trips);
//    }
}
