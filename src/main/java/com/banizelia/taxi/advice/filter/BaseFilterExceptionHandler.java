package com.banizelia.taxi.advice.filter;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.filter.FilterValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BaseFilterExceptionHandler {
    @ExceptionHandler(FilterValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleFilterValidationException(FilterValidationException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Filter Validation Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}