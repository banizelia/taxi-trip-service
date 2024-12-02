package com.banizelia.taxi.advice.position;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.position.PositionOverflowException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionOverflowExceptionHandlerTest {

    private final PositionOverflowExceptionHandler exceptionHandler = new PositionOverflowExceptionHandler();

    @Test
    void handlePositionOverflowException_ShouldReturnInternalServerError() {
        PositionOverflowException exception = new PositionOverflowException(Long.MAX_VALUE, 0.8);

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handlePositionOverflowException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), Objects.requireNonNull(response.getBody()).status());
    }
}
