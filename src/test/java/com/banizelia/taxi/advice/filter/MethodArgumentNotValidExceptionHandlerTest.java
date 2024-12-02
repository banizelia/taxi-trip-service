package com.banizelia.taxi.advice.filter;

import com.banizelia.taxi.util.ApiErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MethodArgumentNotValidExceptionHandlerTest {

    @Test
    void testHandleMethodArgumentNotValidException() {
        MethodArgumentNotValidExceptionHandler handler = new MethodArgumentNotValidExceptionHandler();

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        String exceptionMessage = "Test validation error message";
        when(ex.getMessage()).thenReturn(exceptionMessage);

        ResponseEntity<ApiErrorResponse> responseEntity = handler.handleMethodArgumentNotValidException(ex);


        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ApiErrorResponse errorResponse = responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), Objects.requireNonNull(errorResponse).status());
        assertEquals("Filter Validation Error", errorResponse.error());
        assertEquals(exceptionMessage, errorResponse.message());
    }
}
