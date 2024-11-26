package com.web.common.exception.initialization.handler;

import com.web.common.exception.ApiErrorResponse;
import com.web.common.exception.initialization.InvalidTimeZoneException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidTimeZoneExceptionHandler {
    @ExceptionHandler(InvalidTimeZoneException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTimeZoneException(InvalidTimeZoneException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Timezone Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}