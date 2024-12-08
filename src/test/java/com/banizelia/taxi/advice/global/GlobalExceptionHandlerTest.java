package com.banizelia.taxi.advice.global;

import com.banizelia.taxi.util.ApiErrorResponse;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleOptimisticLockException_ShouldReturnConflictResponse() {
        OptimisticLockException exception = new OptimisticLockException("Entity is being modified by another transaction");

        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleOptimisticLockException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, Objects.requireNonNull(response.getBody()).status());
        assertEquals("Concurrent Modification Error", response.getBody().error());
        assertEquals("Entity is being modified by another transaction", response.getBody().message());
    }

    @Test
    void handleMethodArgumentNotValidException_ShouldReturnBadRequestResponse() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "must not be null");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, Objects.requireNonNull(response.getBody()).status());
        assertEquals("Validation Error", response.getBody().error());
        assertEquals("fieldName: must not be null", response.getBody().message());
    }

    @Test
    void handleMethodArgumentNotValidException_WithNoFieldErrors_ShouldReturnBadRequestResponse() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.emptyList());

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, Objects.requireNonNull(response.getBody()).status());
        assertEquals("Validation Error", response.getBody().error());
        assertEquals("", response.getBody().message());
    }
}
