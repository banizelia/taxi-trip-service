package com.web.trip.controller;

import com.web.trip.model.TripFilterParams;
import com.web.trip.model.TripDto;
import com.web.trip.service.DownloadTripService;
import com.web.trip.service.filter.FilterTripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
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

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/trips")
public class TripController {
    private final FilterTripService filterTripService;
    private final DownloadTripService downloadTripService;
    private final PagedResourcesAssembler<TripDto> pagedResourcesAssembler;

    @Operation(summary = "Filter trips", description = "Allows filtering trips by date, wind speed, as well as sorting and pagination.")
    @GetMapping
    public ResponseEntity<Page<TripDto>> filterTrips(
            @Parameter(description = "Start date and time of the trip", example = "2000-01-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2000-01-01T00:00:00.000") LocalDateTime startDateTime,

            @Parameter(description = "End date and time of the trip", example = "2020-02-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2020-02-01T00:00:00.000") LocalDateTime endDateTime,

            @Parameter(description = "Minimum wind speed")
            @RequestParam(required = false, defaultValue = "0") @Min(0) Double minWindSpeed,

            @Parameter(description = "Maximum wind speed")
            @RequestParam(required = false, defaultValue = "20") @Min(0) Double maxWindSpeed,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") @Min(0) Integer page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") @Min(1) @Max(200) Integer size,

            @Parameter(description = "Field to sort by")
            @RequestParam(required = false, defaultValue = "id") String sort,

            @Parameter(description = "Sorting direction (asc or desc)")
            @RequestParam(required = false, defaultValue = "asc") String direction) {

        TripFilterParams filterParams = new TripFilterParams(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, page, size, sort, direction);

        Page<TripDto> trips = filterTripService.execute(filterParams);
        return ResponseEntity.ok(trips);
    }

    @Operation(summary = "Export trips in Excel format")
    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(
            @Parameter(description = "Filename")
            @RequestParam(required = false, defaultValue = "trips") String filename,

            @Parameter(description = "Start date and time of the trip", example = "2000-01-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2000-01-01T00:00:00.000") LocalDateTime startDateTime,

            @Parameter(description = "End date and time of the trip", example = "2020-02-01T00:00:00.000")
            @RequestParam(required = false, defaultValue = "2020-02-01T00:00:00.000") LocalDateTime endDateTime,

            @Parameter(description = "Minimum wind speed")
            @RequestParam(required = false, defaultValue = "0") @Min(0) Double minWindSpeed,

            @Parameter(description = "Maximum wind speed")
            @RequestParam(required = false, defaultValue = "20") @Min(0) Double maxWindSpeed,

            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") @Min(0) Integer page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") @Min(1) @Max(200) Integer size,

            @Parameter(description = "Field to sort by")
            @RequestParam(required = false, defaultValue = "id") String sort,

            @Parameter(description = "Sorting direction (asc or desc)")
            @RequestParam(required = false, defaultValue = "asc") String direction
    ) {
        if (!filename.matches("[a-zA-Z0-9_-]+")) {
            return ResponseEntity.badRequest().build();
        }

        TripFilterParams filterParams = new TripFilterParams(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, page, size, sort, direction);

        filename = String.format("%s_%s.xlsx", filename, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(downloadTripService.execute(filterParams));
    }
}