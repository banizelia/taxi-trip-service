package com.banizelia.taxi.advice.trip;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.trip.TripAlreadyInFavoritesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TripFavoritesExceptionHandler {
    @ExceptionHandler(TripAlreadyInFavoritesException.class)
    public ResponseEntity<ApiErrorResponse> handleTripAlreadyInFavoritesException(TripAlreadyInFavoritesException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Trip Already In Favorites",
                String.format("Trip with ID %d is already in favorites", ex.getTripId())
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}