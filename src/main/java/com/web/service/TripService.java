package com.web.service;

import com.web.model.Trip;
import com.web.repository.TripsRepository;
import com.web.util.ExcelHelper;
import com.web.util.FieldUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class TripService {
    @Autowired
    TripsRepository tripsRepository;

    public List<Trip> filter(Timestamp startDateTime, Timestamp endDateTime,
                             Double minWindSpeed, Double maxWindSpeed,
                             String dir, String sortBy,
                             Integer page, Integer pageSize) {

        if (endDateTime.before(startDateTime)){
            throw new IllegalArgumentException("endDateTime is before startDateTime, startDateTime = " + startDateTime + " endDateTime = " + endDateTime);
        }

        if (page < 0){
            throw new IllegalArgumentException("Invalid page field, smaller than zero: " + page);
        }

        Set<String> weatherAndTripAllowedField = new HashSet<>();

        weatherAndTripAllowedField.addAll(FieldUtil.getTripFields());
        weatherAndTripAllowedField.addAll(FieldUtil.getWeatherFields());

        if (!weatherAndTripAllowedField.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort.Direction direction = null;
        try {
            direction = Sort.Direction.fromString(dir);
        } catch (Exception e) {
            throw e;
        }

        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        return tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);
    }

    public ResponseEntity<Resource> download() {
        String filename = "trips.xlsx";
        InputStreamResource file = new InputStreamResource(ExcelHelper.tripsToExcel(tripsRepository));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
