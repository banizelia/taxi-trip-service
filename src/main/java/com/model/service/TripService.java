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
        // эффективно ли такое решение, оно дважды проходится по списку, в иделе сразу выгружать нужные данные

        List<Trip> trips = new ArrayList<>();

        List<Object[]> results = tripsRepository.test();
        for (Object[] row : results) {
            System.out.println(row.length);
        }

        return Optional.of(trips);
    }


//    public Optional<List<Trip>> filterTrips(String startDateTime, String endDateTime, Double minWindSpeed, Double maxWindSpeed, String direction, String sortBy) {
//
//

    // todo понять, почнму соритровка выдает ошибку

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
