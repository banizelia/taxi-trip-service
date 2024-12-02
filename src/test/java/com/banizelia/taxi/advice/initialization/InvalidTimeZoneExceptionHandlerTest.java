package com.banizelia.taxi.advice.initialization;

import com.banizelia.taxi.error.initialization.InvalidTimeZoneException;
import com.banizelia.taxi.util.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidTimeZoneExceptionHandlerTest {

    private final InvalidTimeZoneExceptionHandler exceptionHandler = new InvalidTimeZoneExceptionHandler();

    @Test
    void handleInvalidTimeZoneException_ShouldReturnBadRequest() {
        InvalidTimeZoneException exception = new InvalidTimeZoneException("Invalid timezone");

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleInvalidTimeZoneException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), Objects.requireNonNull(response.getBody()).status());
    }
}
