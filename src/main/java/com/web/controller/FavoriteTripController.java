package com.web.controller;

import com.web.exceptions.TripNotFoundException;
import com.web.model.dto.TripDto;
import com.web.service.favorite.FavoriteTripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for managing favorite trips.
 * This controller handles operations related to favorite trips such as
 * retrieving, adding, removing, and reordering favorite trips.
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/favorite-trips")
public class FavoriteTripController {
    private FavoriteTripService favoriteTripService;

    @Operation(summary = "Get all favorite trips", description = "Returns a list of all trips added to favorites.")
    @GetMapping()
    public ResponseEntity<List<TripDto>> getFavouriteTrips() {
        try {
            List<TripDto> favoriteTrips = favoriteTripService.getFavouriteTrips();
            return ResponseEntity.ok(favoriteTrips);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Add a trip to favorites", description = "Adds a trip with the specified ID to favorites.")
    @PutMapping()
    public ResponseEntity<String> saveToFavourite(@Parameter(description = "Trip ID") @RequestParam("tripId") @Min(1) Long id) {
        try {
            favoriteTripService.saveToFavourite(id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Trip added to favorites");
        } catch (TripNotFoundException | IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (OptimisticLockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict occurred while saving the trip");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while saving to favorite trips: " + e.getMessage());
        }
    }

    @Operation(summary = "Remove a trip from favorites", description = "Removes a trip with the specified ID from favorites.")
    @DeleteMapping()
    public ResponseEntity<String> deleteFromFavourite(@Parameter(description = "Trip ID") @RequestParam("tripId") @Min(1) Long id) {
        try {
            favoriteTripService.deleteFromFavourite(id);
            return ResponseEntity.ok("Trip deleted successfully");
        } catch (TripNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error occurred while deleting from favorite trips: " + e.getMessage());
        }
    }

    @Operation(summary = "Change the position of a trip in the favorites list", description = "Allows moving a trip to a new position in the favorites list.")
    @PutMapping("/drag-and-drop")
    public ResponseEntity<String> dragAndDrop(
            @Parameter(description = "Trip ID") @RequestParam(value = "tripId") @Min(1) Long tripId,
            @Parameter(description = "New position") @RequestParam(value = "newPosition") @Min(1) Long newPosition) {
        try {
            favoriteTripService.dragAndDrop(tripId, newPosition);
            return ResponseEntity.ok("Position updated successfully");
        } catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (OptimisticLockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict occurred while updating position");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating position: " + e.getMessage());
        }
    }
}