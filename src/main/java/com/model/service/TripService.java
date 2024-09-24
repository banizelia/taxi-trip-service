package com.model.service;

import com.model.repository.TripsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.model.Trip;

import java.sql.Timestamp;
import java.util.*;

@Service
public class TripService {
    @Autowired
    TripsRepository tripsRepository;


    public Optional<List<Trip>> filter(Timestamp startDateTime, Timestamp endDateTime, Double minWindSpeed, Double maxWindSpeed, Sort sort) {
        List<Trip> trips = tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, sort, Limit.of(10));

        return Optional.of(trips);
    }
}
