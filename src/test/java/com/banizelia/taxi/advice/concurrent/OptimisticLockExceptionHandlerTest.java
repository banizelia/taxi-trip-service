package com.banizelia.taxi.advice.concurrent;

import com.banizelia.taxi.util.ApiErrorResponse;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimisticLockExceptionHandlerTest {

    private final OptimisticLockExceptionHandler exceptionHandler = new OptimisticLockExceptionHandler();

    @Test
    void handleOptimisticLockException_ShouldReturnConflictStatus() {
        // Arrange
        String exceptionMessage = "Optimistic lock failure";
        OptimisticLockException exception = new OptimisticLockException(exceptionMessage);

        // Act
        ResponseEntity<ApiErrorResponse> response = exceptionHandler.handleOptimisticLockException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(HttpStatus.CONFLICT.value(), Objects.requireNonNull(response.getBody()).status());
    }
}
