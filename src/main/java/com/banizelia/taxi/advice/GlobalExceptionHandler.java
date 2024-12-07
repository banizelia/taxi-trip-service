package com.banizelia.taxi.advice;

import com.banizelia.taxi.error.export.ExportException;
import com.banizelia.taxi.error.filter.FilterValidationException;
import com.banizelia.taxi.error.filter.InvalidSortDirectionException;
import com.banizelia.taxi.error.filter.InvalidSortFieldException;
import com.banizelia.taxi.error.filter.InvalidWindSpeedRangeException;
import com.banizelia.taxi.error.initialization.ApplicationInitializationException;
import com.banizelia.taxi.error.initialization.InvalidTimeZoneException;
import com.banizelia.taxi.error.position.PositionException;
import com.banizelia.taxi.error.position.PositionOverflowException;
import com.banizelia.taxi.error.trip.FavoriteTripModificationException;
import com.banizelia.taxi.error.trip.TripAlreadyInFavoritesException;
import com.banizelia.taxi.error.trip.TripNotFoundException;
import com.banizelia.taxi.util.ApiErrorResponse;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ApiErrorResponse> handleOptimisticLockException(OptimisticLockException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.CONFLICT.value(),"Concurrent Modification Error",ex.getMessage()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExportException.class)
    public ResponseEntity<ApiErrorResponse> handleExportException(ExportException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Export Operation Failed",ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FilterValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleFilterValidationException(FilterValidationException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(),"Filter Validation Error",ex.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidSortDirectionException.class, InvalidSortFieldException.class})
    public ResponseEntity<ApiErrorResponse> handleSortExceptions(RuntimeException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(),"Sort Error",ex.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidWindSpeedRangeException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidWindSpeedRangeException(InvalidWindSpeedRangeException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(),"Invalid Wind Speed Range",ex.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error","An unexpected error occurred."),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationInitializationException.class)
    public ResponseEntity<ApiErrorResponse> handleApplicationInitializationException(ApplicationInitializationException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Application Initialization Error",ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidTimeZoneException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTimeZoneException(InvalidTimeZoneException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(),"Invalid Timezone Error",ex.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PositionException.class)
    public ResponseEntity<ApiErrorResponse> handlePositionException(PositionException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Position Processing Error",ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PositionOverflowException.class)
    public ResponseEntity<ApiErrorResponse> handlePositionOverflowException(PositionOverflowException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Position Overflow Error",ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FavoriteTripModificationException.class)
    public ResponseEntity<ApiErrorResponse> handleFavoriteTripModificationException(FavoriteTripModificationException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.CONFLICT.value(),"Concurrent Modification Error",ex.getMessage()),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TripAlreadyInFavoritesException.class)
    public ResponseEntity<ApiErrorResponse> handleTripAlreadyInFavoritesException(TripAlreadyInFavoritesException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.CONFLICT.value(),"Trip Already In Favorites",String.format("Trip with ID %d is already in favorites",ex.getTripId())),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTripNotFoundException(TripNotFoundException ex) {
        return new ResponseEntity<>(new ApiErrorResponse(HttpStatus.NOT_FOUND.value(),"Trip Not Found",ex.getMessage()),HttpStatus.NOT_FOUND);
    }
}
