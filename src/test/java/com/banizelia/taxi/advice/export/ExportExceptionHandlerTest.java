package com.banizelia.taxi.advice.export;

import com.banizelia.taxi.error.export.ExportException;
import com.banizelia.taxi.util.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExportExceptionHandlerTest {

    private final ExportExceptionHandler exportExceptionHandler = new ExportExceptionHandler();

    @Test
    void handleExportException_ShouldReturnInternalServerErrorResponse() {
        ExportException exception = new ExportException("Export operation encountered an error", new IOException());

        ResponseEntity<ApiErrorResponse> response = exportExceptionHandler.handleExportException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, Objects.requireNonNull(response.getBody()).status());
        assertEquals("Export Operation Failed", response.getBody().error());
        assertEquals("Export operation encountered an error", response.getBody().message());
    }
}
