package com.web.common.exception.filter.handler;

import com.web.common.exception.ApiErrorResponse;
import com.web.common.exception.filter.InvalidSortDirectionException;
import com.web.common.exception.filter.InvalidSortFieldException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class SortExceptionHandler {
    @ExceptionHandler(InvalidSortDirectionException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidSortDirectionException(InvalidSortDirectionException ex) {
        log.error("Invalid sort direction specified", ex);
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Sort Direction",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidSortFieldException(InvalidSortFieldException ex) {
        log.error("Invalid sort field specified", ex);
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Sort Field",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}