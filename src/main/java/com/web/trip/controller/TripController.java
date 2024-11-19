package com.web.trip.controller;

import com.web.trip.TripFilterParams;
import com.web.trip.model.TripDto;
import com.web.trip.service.DownloadTripService;
import com.web.trip.service.FilterTripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private FilterTripService filterTripService;
    private DownloadTripService downloadTripService;
    private PagedResourcesAssembler<TripDto> pagedResourcesAssembler;

    @Operation(summary = "Filter trips", description = "Allows filtering trips by date, wind speed, as well as sorting and pagination.")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<TripDto>>> filterTrips(@Valid TripFilterParams filterParams) {
        filterParams.validate();
        Page<TripDto> trips = filterTripService.execute(filterParams);
        PagedModel<EntityModel<TripDto>> pagedModel = pagedResourcesAssembler.toModel(trips);
        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Export trips in Excel format")
    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(
            @Parameter(description = "Filename")
            @RequestParam(required = false, defaultValue = "trips") String filename) {

        if (!filename.matches("[a-zA-Z0-9_-]+")) {
            return ResponseEntity.badRequest().build();
        }

        filename = String.format("%s_%s.xlsx", filename, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(downloadTripService.execute());
    }
}