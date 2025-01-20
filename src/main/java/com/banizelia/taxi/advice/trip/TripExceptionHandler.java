package com.banizelia.taxi.advice.trip;

import com.banizelia.taxi.error.trip.FavoriteTripModificationException;
import com.banizelia.taxi.error.trip.TripAlreadyInFavoritesException;
import com.banizelia.taxi.error.trip.TripNotFoundException;
import com.banizelia.taxi.util.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TripExceptionHandler {

    @ExceptionHandler(FavoriteTripModificationException.class)
    public ResponseEntity<ApiErrorResponse> handleFavoriteTripModificationException(FavoriteTripModificationException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Concurrent Modification Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TripAlreadyInFavoritesException.class)
    public ResponseEntity<ApiErrorResponse> handleTripAlreadyInFavoritesException(TripAlreadyInFavoritesException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Trip Already In Favorites",
                String.format("Trip with ID %d is already in favorites", ex.getTripId())
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTripNotFoundException(TripNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Trip Not Found",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
