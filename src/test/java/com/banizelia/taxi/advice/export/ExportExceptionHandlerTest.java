package com.banizelia.taxi.advice.export;

import com.banizelia.taxi.util.ApiErrorResponse;
import com.banizelia.taxi.error.export.ExportException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExportExceptionHandlerTest {

    private final ExportExceptionHandler exceptionHandler = new ExportExceptionHandler();

    @Test
    void handleExportException_ShouldReturnInternalServerError() {
        // Arrange

        ExportException exception = new ExportException("Failed to export data", new IOException());

        // Act
        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleExportException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), Objects.requireNonNull(response.getBody()).status());
    }
}
