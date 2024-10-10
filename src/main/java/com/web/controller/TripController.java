package com.web.controller;

import com.web.model.Trip;
import com.web.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing trips.
 * This controller handles operations related to trips such as
 * filtering, sorting, pagination, and exporting to Excel.
 */
@RestController
@RequestMapping("/api/v1/trips")
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * Filters trips based on various criteria, with sorting and pagination.
     *
     * @param startDateTime Start date and time for filtering trips.
     * @param endDateTime End date and time for filtering trips.
     * @param minWindSpeed Minimum wind speed for filtering trips.
     * @param maxWindSpeed Maximum wind speed for filtering trips.
     * @param direction Sorting direction (asc or desc).
     * @param sortBy Field to sort by.
     * @param page Page number for pagination.
     * @param pageSize Page size for pagination.
     * @return ResponseEntity containing a list of filtered trips.
     */
    @Operation(summary = "Filter trips", description = "Allows filtering trips by date, wind speed, as well as sorting and pagination.")
    @GetMapping
    public ResponseEntity<List<Trip>> filterTrips(
            @Parameter(description = "Start date and time of the trip", example = "2016-01-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2016-01-01T00:00:00.000") LocalDateTime startDateTime,

            @Parameter(description = "End date and time of the trip", example = "2016-02-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2016-02-01T00:00:00.000") LocalDateTime endDateTime,

            @Parameter(description = "Minimum wind speed")
            @RequestParam(required = false, defaultValue = "0") Double minWindSpeed,

            @Parameter(description = "Maximum wind speed")
            @RequestParam(required = false, defaultValue = "9999") Double maxWindSpeed,

            @Parameter(description = "Sorting direction (asc or desc)")
            @RequestParam(required = false, defaultValue = "asc") String direction,

            @Parameter(description = "Field to sort by")
            @RequestParam(required = false, defaultValue = "id") String sortBy,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") Integer page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize);
    }

    /**
     * Exports trips to Excel format.
     *
     * @param sheetLimit The maximum number of sheets in the Excel file.
     * @return ResponseEntity containing the Excel file as a Resource.
     */
    @Operation(summary = "Export trips in Excel format", description = "Exports the list of trips in Excel format, with the option to set a limit on the number of sheets.")
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@Parameter(description = "Sheet limit") @RequestParam(value = "sheetLimit", defaultValue = "2") Integer sheetLimit) {
        return tripService.download(sheetLimit);
    }
}