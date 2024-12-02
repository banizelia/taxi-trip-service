package com.banizelia.taxi.advice.filter;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.filter.InvalidWindSpeedRangeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WindSpeedExceptionHandler {
    @ExceptionHandler(InvalidWindSpeedRangeException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidWindSpeedRangeException(InvalidWindSpeedRangeException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Wind Speed Range",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
