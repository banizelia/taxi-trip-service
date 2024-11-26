package com.web.common.exception.filter.handler;

import com.web.common.exception.ApiErrorResponse;
import com.web.common.exception.filter.FilterValidationException;
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