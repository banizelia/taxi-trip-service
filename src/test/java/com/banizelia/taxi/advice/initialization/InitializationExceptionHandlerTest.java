package com.banizelia.taxi.advice.initialization;

import com.banizelia.taxi.error.initialization.ApplicationInitializationException;
import com.banizelia.taxi.error.initialization.InvalidTimeZoneException;
import com.banizelia.taxi.util.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InitializationExceptionHandlerTest {

    private final InitializationExceptionHandler initializationExceptionHandler = new InitializationExceptionHandler();

    @Test
    void handleApplicationInitializationException_ShouldReturnInternalServerErrorResponse() {
        ApplicationInitializationException exception = new ApplicationInitializationException("Initialization failed", new RuntimeException());

        ResponseEntity<ApiErrorResponse> response = initializationExceptionHandler.handleApplicationInitializationException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, Objects.requireNonNull(response.getBody()).status());
        assertEquals("Application Initialization Error", response.getBody().error());
        assertEquals("Initialization failed", response.getBody().message());
    }

    @Test
    void handleInvalidTimeZoneException_ShouldReturnBadRequestResponse() {
        InvalidTimeZoneException exception = new InvalidTimeZoneException("Timezone is not supported");

        ResponseEntity<ApiErrorResponse> response = initializationExceptionHandler.handleInvalidTimeZoneException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, Objects.requireNonNull(response.getBody()).status());
        assertEquals("Invalid Timezone Error", response.getBody().error());
        assertEquals("Invalid timezone specified: Timezone is not supported", response.getBody().message());
    }
}
