package com.banizelia.taxi.advice.position;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.position.PositionException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasePositionExceptionHandlerTest {

    private final BasePositionExceptionHandler exceptionHandler = new BasePositionExceptionHandler();

    @Test
    void handlePositionException_ShouldReturnInternalServerError() {
        PositionException exception = new PositionException("Position processing failed");

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handlePositionException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), Objects.requireNonNull(response.getBody()).status());
    }
}
