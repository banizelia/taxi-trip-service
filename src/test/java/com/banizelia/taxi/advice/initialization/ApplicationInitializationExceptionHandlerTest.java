package com.banizelia.taxi.advice.initialization;

import com.banizelia.taxi.error.initialization.ApplicationInitializationException;
import com.banizelia.taxi.util.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.runners.model.InitializationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationInitializationExceptionHandlerTest {

    private final ApplicationInitializationExceptionHandler exceptionHandler = new ApplicationInitializationExceptionHandler();

    @Test
    void handleApplicationInitializationException_ShouldReturnInternalServerError() {
        Exception e = new InitializationError("Initialization failed");
        ApplicationInitializationException exception = new ApplicationInitializationException("Initialization failed", e);

        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleApplicationInitializationException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), Objects.requireNonNull(response.getBody()).status());
    }
}
