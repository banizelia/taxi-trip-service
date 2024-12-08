package com.banizelia.taxi.advice.trip;

import com.banizelia.taxi.error.trip.FavoriteTripModificationException;
import com.banizelia.taxi.error.trip.TripAlreadyInFavoritesException;
import com.banizelia.taxi.error.trip.TripNotFoundException;
import com.banizelia.taxi.util.ApiErrorResponse;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TripExceptionHandlerTest {

    private final TripExceptionHandler tripExceptionHandler = new TripExceptionHandler();

    @Test
    void handleFavoriteTripModificationException_ShouldReturnConflictResponse() {
        FavoriteTripModificationException exception = new FavoriteTripModificationException("Concurrent modification detected", new OptimisticLockException());

        ResponseEntity<ApiErrorResponse> response = tripExceptionHandler.handleFavoriteTripModificationException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().status());
        assertEquals("Concurrent Modification Error", response.getBody().error());
        assertEquals("Concurrent modification detected", response.getBody().message());
    }

    @Test
    void handleTripAlreadyInFavoritesException_ShouldReturnConflictResponse() {
        TripAlreadyInFavoritesException exception = new TripAlreadyInFavoritesException(42L);

        ResponseEntity<ApiErrorResponse> response = tripExceptionHandler.handleTripAlreadyInFavoritesException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().status());
        assertEquals("Trip Already In Favorites", response.getBody().error());
        assertEquals("Trip with ID 42 is already in favorites", response.getBody().message());
    }

    @Test
    void handleTripNotFoundException_ShouldReturnNotFoundResponse() {
        TripNotFoundException exception = new TripNotFoundException(12L);

        ResponseEntity<ApiErrorResponse> response = tripExceptionHandler.handleTripNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().status());
        assertEquals("Trip Not Found", response.getBody().error());
    }
}
