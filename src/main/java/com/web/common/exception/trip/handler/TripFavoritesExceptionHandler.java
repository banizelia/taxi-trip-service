package com.web.common.exception.trip.handler;

import com.web.common.exception.ApiErrorResponse;
import com.web.common.exception.trip.TripAlreadyInFavoritesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class TripFavoritesExceptionHandler {
    @ExceptionHandler(TripAlreadyInFavoritesException.class)
    public ResponseEntity<ApiErrorResponse> handleTripAlreadyInFavoritesException(TripAlreadyInFavoritesException ex) {
        log.error("Attempt to add already favorited trip with ID: {}", ex.getTripId(), ex);
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Trip Already In Favorites",
                String.format("Trip with ID %d is already in favorites", ex.getTripId())
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}