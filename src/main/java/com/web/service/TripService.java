package com.web.service;

import com.web.model.Trip;
import com.web.repository.TripsRepository;
import com.web.util.ExcelHelper;
import com.web.util.FieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class TripService {
    @Autowired
    TripsRepository tripsRepository;

    public List<Trip> filter(LocalDateTime startDateTime, LocalDateTime endDateTime, Double minWindSpeed, Double maxWindSpeed, String direction, String sortBy, Integer page) {

        if (endDateTime.isBefore(startDateTime)){
            throw new IllegalArgumentException("endDateTime is before startDateTime: startDateTime = " + startDateTime + " endDateTime = " + endDateTime);
        }

        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")){
            throw new IllegalArgumentException("Invalid sort direction: " + direction);
        }

        if (!FieldUtil.getAllAllowedFields().contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        if (page < 0){
            throw new IllegalArgumentException("Invalid page field, smaller than zero: " + page);
        }

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, 500, sort);

        return tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);
    }

    public ByteArrayInputStream download() {
        return ExcelHelper.tripsToExcel(tripsRepository);
    }

}
