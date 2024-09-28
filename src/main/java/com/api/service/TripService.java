package com.api.service;

import com.api.excelHelper.ExcelHelper;
import com.api.repository.TripsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.api.model.Trip;
import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.*;

@Service
public class TripService {
    @Autowired
    TripsRepository tripsRepository;

    public Optional<List<Trip>> filter(Timestamp startDateTime, Timestamp endDateTime, Double minWindSpeed, Double maxWindSpeed, Sort sort) {
        List<Trip> trips = tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, sort, Limit.of(500));

        return Optional.of(trips);
    }

    public ByteArrayInputStream download() {
        return ExcelHelper.tripsToExcel(tripsRepository);
    }

}
