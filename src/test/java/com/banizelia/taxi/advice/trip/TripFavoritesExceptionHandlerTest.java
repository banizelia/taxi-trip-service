package com.banizelia.taxi.advice.trip;

import com.banizelia.taxi.error.trip.TripAlreadyInFavoritesException;
import com.banizelia.taxi.util.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TripFavoritesExceptionHandlerTest {

    private final TripFavoritesExceptionHandler exceptionHandler = new TripFavoritesExceptionHandler();

    @Test
    void handleTripAlreadyInFavoritesException_ShouldReturnConflict() {
        long tripId = 123;
        TripAlreadyInFavoritesException exception = new TripAlreadyInFavoritesException(tripId);

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleTripAlreadyInFavoritesException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(HttpStatus.CONFLICT.value(), Objects.requireNonNull(response.getBody()).status());
    }
}
