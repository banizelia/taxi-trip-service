package com.banizelia.taxi.advice.initialization;

import com.banizelia.taxi.error.initialization.ApplicationInitializationException;
import com.banizelia.taxi.error.initialization.InvalidTimeZoneException;
import com.banizelia.taxi.util.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InitializationExceptionHandler {

    @ExceptionHandler(ApplicationInitializationException.class)
    public ResponseEntity<ApiErrorResponse> handleApplicationInitializationException(ApplicationInitializationException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Application Initialization Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidTimeZoneException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidTimeZoneException(InvalidTimeZoneException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Timezone Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
