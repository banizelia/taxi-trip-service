package com.banizelia.taxi.advice.filter;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.filter.InvalidSortDirectionException;
import com.banizelia.taxi.error.filter.InvalidSortFieldException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortExceptionHandlerTest {

    private final SortExceptionHandler exceptionHandler = new SortExceptionHandler();

    @Test
    void handleInvalidSortDirectionException_ShouldReturnBadRequest() {
        InvalidSortDirectionException exception = new InvalidSortDirectionException("Invalid direction");

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleInvalidSortDirectionException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleInvalidSortFieldException_ShouldReturnBadRequest() {
        InvalidSortFieldException exception = new InvalidSortFieldException("Invalid field");

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleInvalidSortFieldException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
