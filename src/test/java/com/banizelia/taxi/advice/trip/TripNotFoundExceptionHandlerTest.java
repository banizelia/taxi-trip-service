package com.banizelia.taxi.advice.trip;

import com.banizelia.taxi.error.trip.TripNotFoundException;
import com.banizelia.taxi.util.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TripNotFoundExceptionHandlerTest {

    private final TripNotFoundExceptionHandler exceptionHandler = new TripNotFoundExceptionHandler();

    @Test
    void handleTripNotFoundException_ShouldReturnNotFound() {
        TripNotFoundException exception = new TripNotFoundException(1L);

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleTripNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), Objects.requireNonNull(response.getBody()).status());
    }
}
