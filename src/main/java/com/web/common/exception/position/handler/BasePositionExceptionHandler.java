package com.web.common.exception.position.handler;

import com.web.common.exception.ApiErrorResponse;
import com.web.common.exception.position.PositionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BasePositionExceptionHandler {
    @ExceptionHandler(PositionException.class)
    public ResponseEntity<ApiErrorResponse> handlePositionException(PositionException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Position Processing Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}