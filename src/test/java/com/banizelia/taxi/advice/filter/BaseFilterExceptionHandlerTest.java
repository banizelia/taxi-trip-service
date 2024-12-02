package com.banizelia.taxi.advice.filter;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.filter.FilterValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaseFilterExceptionHandlerTest {

    private final BaseFilterExceptionHandler exceptionHandler = new BaseFilterExceptionHandler();

    @Test
    void handleFilterValidationException_ShouldReturnBadRequest() {
        FilterValidationException exception = new FilterValidationException("Validation error");

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleFilterValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
