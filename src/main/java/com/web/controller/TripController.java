package com.web.controller;

import com.web.model.Trip;
import com.web.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/filter")
    public List<Trip> filter(
            @RequestParam(value = "startDateTime", required = false, defaultValue = "2016-01-01 00:00:00") Timestamp startDateTime,
            @RequestParam(value = "endDateTime", required = false, defaultValue = "2016-01-31 23:59:59") Timestamp endDateTime,
            @RequestParam(value = "minWindSpeed", required = false, defaultValue = "0") Double minWindSpeed,
            @RequestParam(value = "maxWindSpeed", required = false, defaultValue = "9999") Double maxWindSpeed,
            @RequestParam(value = "direction", required = false, defaultValue = "asc") String direction, /* asc or desc */
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy, /* pickupDatetime or averageWindSpeed */
            @RequestParam(value = "page") Integer page){

        return tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download() {
        String filename = "trips.xlsx";

        InputStreamResource file = new InputStreamResource(tripService.download());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}