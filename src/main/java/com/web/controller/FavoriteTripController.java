package com.web.controller;

import com.web.model.Trip;
import com.web.service.FavoriteTripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * REST controller for managing favorite trips.
 * This controller handles operations related to favorite trips such as
 * retrieving, adding, removing, and reordering favorite trips.
 */
@RestController
@RequestMapping("/api/v1/favorite-trips")
public class FavoriteTripController {
    private final FavoriteTripService favoriteTripService;

    @Autowired
    public FavoriteTripController(FavoriteTripService favoriteTripService) {
        this.favoriteTripService = favoriteTripService;
    }

    /**
     * Retrieves all favorite trips.
     * @return ResponseEntity containing a list of all favorite trips.
     */
    @Operation(summary = "Get all favorite trips", description = "Returns a list of all trips added to favorites.")
    @GetMapping()
    public ResponseEntity<List<Trip>> getFavouriteTrips() {
        return favoriteTripService.getFavouriteTrips();
    }

    /**
     * Adds a trip to favorites.
     * @param id The ID of the trip to be added to favorites.
     * @return ResponseEntity with a message indicating the result of the operation.
     */
    @Operation(summary = "Add a trip to favorites", description = "Adds a trip with the specified ID to favorites.")
    @PutMapping()
    public ResponseEntity<String> saveToFavourite(@Parameter(description = "Trip ID") @RequestParam("tripId") Long id) {
        return favoriteTripService.saveToFavourite(id);
    }

    /**
     * Removes a trip from favorites.
     * @param id The ID of the trip to be removed from favorites.
     * @return ResponseEntity with a message indicating the result of the operation.
     */
    @Operation(summary = "Remove a trip from favorites", description = "Removes a trip with the specified ID from favorites.")
    @DeleteMapping()
    public ResponseEntity<String> deleteFromFavourite(@Parameter(description = "Trip ID") @RequestParam("tripId") Long id) {
        return favoriteTripService.deleteFromFavourite(id);
    }

    /**
     * Changes the position of a trip in the favorites list.
     * @param tripId The ID of the trip to be moved.
     * @param newPosition The new position for the trip in the favorites list.
     * @return ResponseEntity with a message indicating the result of the operation.
     */
    @Operation(summary = "Change the position of a trip in the favorites list", description = "Allows moving a trip to a new position in the favorites list.")
    @PutMapping("/drag-and-drop")
    public ResponseEntity<String> dragAndDrop(
            @Parameter(description = "Trip ID") @RequestParam(value = "tripId") Long tripId,
            @Parameter(description = "New position") @RequestParam(value = "newPosition") Long newPosition) {
        return favoriteTripService.dragAndDrop(tripId, newPosition);
    }

    /**
     * Validates and corrects the order in the favorites list.
     * @return ResponseEntity with a message indicating the result of the operation.
     */
    @Operation(summary = "Validate and correct the order in the favorites list")
    @PutMapping("/validate-positions")
    public ResponseEntity<String> validatePositions() {
        return favoriteTripService.validatePositions();
    }
}