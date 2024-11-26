package com.web.common.exception.handler;

import com.web.common.exception.ApiErrorResponse;
import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class OptimisticLockExceptionHandler {
    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ApiErrorResponse> handleOptimisticLockException(OptimisticLockException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Concurrent Modification Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}