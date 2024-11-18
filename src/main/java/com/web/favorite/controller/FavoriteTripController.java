package com.web.favorite.controller;

import com.web.favorite.service.*;
import com.web.favorite.service.DragAndDropFavoriteTripService;
import com.web.trip.model.TripDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing favorite trips.
 * This controller handles operations related to favorite trips such as
 * retrieving, adding, removing, and reordering favorite trips.
 */
@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/favorite-trips")
public class FavoriteTripController {
    private SaveFavoriteTripService saveFavoriteTripService;
    private DeleteFavoriteTripService deleteFavoriteTripService;
    private DragAndDropFavoriteTripService dragAndDropFavoriteTripService;
    private GetFavoriteTripService getFavoriteTripService;
    private PagedResourcesAssembler<TripDto> pagedResourcesAssembler;

    @Operation(summary = "Get all favorite trips", description = "Returns a list of all trips added to favorites.")
    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<TripDto>>> getFavouriteTrips(
            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") @Min(0) Integer page,

            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") @Min(1) @Max(200) Integer size,

            @Parameter(description = "Field to sort by")
            @RequestParam(required = false, defaultValue = "position") String sort,

            @Parameter(description = "Sorting direction (asc or desc)")
            @RequestParam(required = false, defaultValue = "asc") String direction) {

        Page<TripDto> trips = getFavoriteTripService.execute(page, size, sort, direction);
        PagedModel<EntityModel<TripDto>> pagedModel = pagedResourcesAssembler.toModel(trips);
        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Add a trip to favorites", description = "Adds a trip with the specified ID to favorites.")
    @PutMapping()
    public ResponseEntity<String> saveToFavourite(
            @Parameter(description = "Trip ID")
            @RequestParam("tripId") @NotNull @Min(1) @Max(Long.MAX_VALUE) Long id) {

        saveFavoriteTripService.execute(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Trip added to favorites");
    }

    @Operation(summary = "Remove a trip from favorites", description = "Removes a trip with the specified ID from favorites.")
    @DeleteMapping()
    public ResponseEntity<String> deleteFromFavourite(
            @Parameter(description = "Trip ID")
            @RequestParam("tripId") @NotNull @Min(1) @Max(Long.MAX_VALUE) Long id) {

        deleteFavoriteTripService.execute(id);
        return ResponseEntity.ok("Trip deleted successfully");
    }

    @Operation(summary = "Change the position of a trip in the favorites list", description = "Allows moving a trip to a new position in the favorites list.")
    @PutMapping("/drag-and-drop")
    public ResponseEntity<String> dragAndDrop(
            @Parameter(description = "Trip ID")
            @RequestParam(value = "tripId") @NotNull @Min(1) @Max(Long.MAX_VALUE) Long tripId,

            @Parameter(description = "New position")
            @RequestParam(value = "newPosition") @NotNull @Min(1) @Max(Long.MAX_VALUE) Long newPosition) {

        dragAndDropFavoriteTripService.execute(tripId, newPosition);
        return ResponseEntity.ok("Position updated successfully");

    }
}