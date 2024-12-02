package com.banizelia.taxi.advice.trip;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.trip.TripNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TripNotFoundExceptionHandler {
    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTripNotFoundException(TripNotFoundException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Trip Not Found",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}