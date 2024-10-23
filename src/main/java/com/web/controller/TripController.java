package com.web.controller;

import com.web.model.dto.TripDto;
import com.web.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

/**
 * REST controller for managing trips.
 * This controller handles operations related to trips such as
 * filtering, sorting, pagination, and exporting to Excel.
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/trips")
public class TripController {
    private TripService tripService;
    private PagedResourcesAssembler<TripDto> pagedResourcesAssembler;

    @Operation(summary = "Filter trips", description = "Allows filtering trips by date, wind speed, as well as sorting and pagination.")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<TripDto>>> filterTrips(
            @Parameter(description = "Start date and time of the trip", example = "2016-01-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2016-01-01T00:00:00.000") LocalDateTime startDateTime,

            @Parameter(description = "End date and time of the trip", example = "2016-02-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2016-02-01T00:00:00.000") LocalDateTime endDateTime,

            @Parameter(description = "Minimum wind speed")
            @RequestParam(required = false, defaultValue = "0") @Min(0) Double minWindSpeed,

            @Parameter(description = "Maximum wind speed")
            @RequestParam(required = false, defaultValue = "9999") @Min(0) Double maxWindSpeed,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") @Min(0) Integer page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") @Min(1) @Max(200) Integer size,

            @Parameter(description = "Field to sort by")
            @RequestParam(required = false, defaultValue = "id") String sort,

            @Parameter(description = "Sorting direction (asc or desc)")
            @RequestParam(required = false, defaultValue = "asc") String direction) {

        try {
            Page<TripDto> trips = tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, page, size, sort, direction);

            PagedModel<EntityModel<TripDto>> pagedModel = pagedResourcesAssembler.toModel(trips);

            return ResponseEntity.ok(pagedModel);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Export trips in Excel format", description = "Exports the list of trips in xlsx format, with the option to set a limit on the number of sheets.")
    @GetMapping("/download")
    public ResponseEntity<Resource> download() {
        return tripService.download();
    }
}