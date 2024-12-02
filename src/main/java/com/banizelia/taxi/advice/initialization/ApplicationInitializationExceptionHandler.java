package com.banizelia.taxi.advice.initialization;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.initialization.ApplicationInitializationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class ApplicationInitializationExceptionHandler {
    @ExceptionHandler(ApplicationInitializationException.class)
    public ResponseEntity<ApiErrorResponse> handleApplicationInitializationException(ApplicationInitializationException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Application Initialization Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}