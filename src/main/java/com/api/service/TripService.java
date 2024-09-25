package com.api.service;

import com.api.repository.TripsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.api.model.Trip;
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


}
