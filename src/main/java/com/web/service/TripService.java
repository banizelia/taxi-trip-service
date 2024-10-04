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

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class TripService {
    @Autowired
    TripsRepository tripsRepository;

    public List<Trip> filter(LocalDateTime startDateTime, LocalDateTime endDateTime,
                             Double minWindSpeed, Double maxWindSpeed,
                             String direction, String sortBy,
                             Integer page, Integer pageSize) {

        if (endDateTime.isBefore(startDateTime)){
            throw new IllegalArgumentException("endDateTime is before startDateTime, startDateTime = " + startDateTime + " endDateTime = " + endDateTime);
        }

        if (page < 0){
            throw new IllegalArgumentException("Invalid page field, smaller than zero: " + page);
        }

        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")){
            throw new IllegalArgumentException("Invalid direction : " + direction);
        }

        Set<String> weatherAndTripAllowedField = new HashSet<>();

        weatherAndTripAllowedField.addAll(FieldUtil.getTripFields());
        weatherAndTripAllowedField.addAll(FieldUtil.getWeatherFields());

        if (!weatherAndTripAllowedField.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        return tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);
    }

    public ResponseEntity<Resource> download(Integer listsLimit) {
        String filename = "trips.xlsx";
        InputStreamResource file = new InputStreamResource(ExcelHelper.tripsToExcel(tripsRepository, listsLimit));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
