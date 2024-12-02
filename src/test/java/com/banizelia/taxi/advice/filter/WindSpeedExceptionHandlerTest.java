package com.banizelia.taxi.advice.filter;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.filter.InvalidWindSpeedRangeException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WindSpeedExceptionHandlerTest {

    private final WindSpeedExceptionHandler exceptionHandler = new WindSpeedExceptionHandler();

    @Test
    void handleInvalidWindSpeedRangeException_ShouldReturnBadRequest() {
        InvalidWindSpeedRangeException exception = new InvalidWindSpeedRangeException(1.0, 0.0);

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleInvalidWindSpeedRangeException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
