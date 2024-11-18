package com.web.common.exception.initialization.handler;

import com.web.common.exception.ApiErrorResponse;
import com.web.common.exception.initialization.ApplicationInitializationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class ApplicationExceptionHandler {
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