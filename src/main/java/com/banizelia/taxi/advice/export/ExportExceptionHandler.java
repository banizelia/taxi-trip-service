package com.banizelia.taxi.advice.export;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.export.ExportException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExportExceptionHandler {
    @ExceptionHandler(ExportException.class)
    public ResponseEntity<ApiErrorResponse> handleExportException(ExportException ex) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Export Operation Failed",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}