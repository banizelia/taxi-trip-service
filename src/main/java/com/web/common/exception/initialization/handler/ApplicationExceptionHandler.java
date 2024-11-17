package com.web.common.exception.initialization.handler;

import com.web.common.exception.initialization.ApplicationInitializationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class ApplicationExceptionHandler {
    @ExceptionHandler(ApplicationInitializationException.class)
    public ResponseEntity<String> handleApplicationInitializationException(ApplicationInitializationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}