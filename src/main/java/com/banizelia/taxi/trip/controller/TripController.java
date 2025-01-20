package com.banizelia.taxi.trip.controller;

import com.banizelia.taxi.favorite.service.DeleteFavoriteTripService;
import com.banizelia.taxi.favorite.service.DragAndDropFavoriteTripService;
import com.banizelia.taxi.favorite.service.SaveFavoriteTripService;
import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.trip.service.DownloadTripService;
import com.banizelia.taxi.trip.service.FilterTripService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    private final SaveFavoriteTripService saveFavoriteTripService;
    private final DeleteFavoriteTripService deleteFavoriteTripService;
    private final DragAndDropFavoriteTripService dragAndDropFavoriteTripService;


    @Operation(
            summary = "Filter trips",
            description = """
                    Filters trips with the option to sort by various fields, including nested properties.
                    
                    Sorting examples:
                    
                        - Sort by pickup date: `sort=pickupDatetime,asc`
                    
                        - Sort by position in favorite trips: `sort=favoriteTrip.position,desc`\s""")
    @GetMapping
    public ResponseEntity<Page<TripDto>> filterTrips(
            @Valid @ParameterObject TripFilterParams params,
            @Valid @ParameterObject Pageable pageable) {

        Page<TripDto> trips = filterTripService.execute(params, pageable);
        return ResponseEntity.ok(trips);
    }

    @Operation(summary = "Add a trip to favorites")
    @PostMapping("/{tripId}/favorite")
    public ResponseEntity<String> addToFavorites(
            @PathVariable @NotNull @Min(1) Long tripId) {

        saveFavoriteTripService.execute(tripId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Trip added to favorites");
    }

    @Operation(summary = "Remove a trip from favorites")
    @DeleteMapping("/{tripId}/favorite")
    public ResponseEntity<String> deleteFromFavourite(
            @PathVariable @NotNull @Min(1) Long tripId) {

        deleteFavoriteTripService.execute(tripId);
        return ResponseEntity.ok("Trip deleted successfully");
    }

    @Operation(summary = "Change the position of a trip in the favorites list")
    @PutMapping("/{tripId}/favorite/drag-and-drop")
    public ResponseEntity<String> dragAndDrop(
            @PathVariable @NotNull @Min(1) Long tripId,
            @RequestParam("newPosition") @NotNull @Min(1) Long newPosition) {

        dragAndDropFavoriteTripService.execute(tripId, newPosition);
        return ResponseEntity.ok("Position updated successfully");
    }

    @Operation(summary = "Export trips in Excel format")
    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(
            @Pattern(regexp = "[a-zA-Z0-9_-]+")
            @RequestParam(required = false, defaultValue = "trips")
            String filename,

            @Valid @ParameterObject TripFilterParams params) {

        filename = String.format("%s_%s.xlsx", filename, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(downloadTripService.execute(params));
    }
}