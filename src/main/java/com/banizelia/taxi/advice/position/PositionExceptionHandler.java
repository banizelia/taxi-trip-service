package com.banizelia.taxi.advice.position;

import com.banizelia.taxi.error.position.PositionException;
import com.banizelia.taxi.error.position.PositionOverflowException;
import com.banizelia.taxi.util.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PositionExceptionHandler {

    @ExceptionHandler(PositionException.class)
    public ResponseEntity<ApiErrorResponse> handlePositionException(PositionException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Position Processing Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PositionOverflowException.class)
    public ResponseEntity<ApiErrorResponse> handlePositionOverflowException(PositionOverflowException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Position Overflow Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
