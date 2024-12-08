package com.banizelia.taxi.advice.position;

import com.banizelia.taxi.error.position.PositionException;
import com.banizelia.taxi.error.position.PositionOverflowException;
import com.banizelia.taxi.util.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionExceptionHandlerTest {

    private final PositionExceptionHandler positionExceptionHandler = new PositionExceptionHandler();

    @Test
    void handlePositionException_ShouldReturnInternalServerErrorResponse() {
        PositionException exception = new PositionException("Position processing failed");

        ResponseEntity<ApiErrorResponse> response = positionExceptionHandler.handlePositionException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().status());
        assertEquals("Position Processing Error", response.getBody().error());
        assertEquals("Position processing failed", response.getBody().message());
    }

    @Test
    void handlePositionOverflowException_ShouldReturnInternalServerErrorResponse() {
        PositionOverflowException exception = new PositionOverflowException(1000000L, 0.8);

        ResponseEntity<ApiErrorResponse> response = positionExceptionHandler.handlePositionOverflowException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().status());
        assertEquals("Position Overflow Error", response.getBody().error());
    }
}
