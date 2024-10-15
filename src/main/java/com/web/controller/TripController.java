package com.web.controller;

import com.web.model.Trip;
import com.web.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

/**
 * REST controller for managing trips.
 * This controller handles operations related to trips such as
 * filtering, sorting, pagination, and exporting to Excel.
 */
@Validated
@RestController
@RequestMapping("/api/v1/trips")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @Operation(summary = "Filter trips", description = "Allows filtering trips by date, wind speed, as well as sorting and pagination.")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Trip>>> filterTrips(
            @Parameter(description = "Start date and time of the trip", example = "2016-01-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2016-01-01T00:00:00.000") LocalDateTime startDateTime,

            @Parameter(description = "End date and time of the trip", example = "2016-02-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2016-02-01T00:00:00.000") LocalDateTime endDateTime,

            @Parameter(description = "Minimum wind speed")
            @RequestParam(required = false, defaultValue = "0") @Min(0) Double minWindSpeed,

            @Parameter(description = "Maximum wind speed")
            @RequestParam(required = false, defaultValue = "9999") @Min(0) Double maxWindSpeed,

            @Parameter(description = "Sorting direction (asc or desc)")
            @RequestParam(required = false, defaultValue = "asc") String direction,

            @Parameter(description = "Field to sort by")
            @RequestParam(required = false, defaultValue = "id") String sortBy,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") @Min(0) Integer page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") @Min(1) @Max(200) Integer pageSize) {
        return tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize);
    }

    @Operation(summary = "Export trips in Excel format", description = "Exports the list of trips in xlsx format, with the option to set a limit on the number of sheets.")
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@Parameter(description = "Sheet limit") @RequestParam(value = "sheetLimit", defaultValue = "2") @Min(1) Integer sheetLimit) {
        return tripService.download(sheetLimit);
    }
}