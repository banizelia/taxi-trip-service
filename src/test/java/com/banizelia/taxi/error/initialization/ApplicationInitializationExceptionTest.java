package com.banizelia.taxi.error.initialization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApplicationInitializationExceptionTest {

    @Test
    void testExceptionWithMessageAndCause() {
        String message = "Initialization failed";
        Throwable cause = new RuntimeException("Underlying cause");

        ApplicationInitializationException exception = new ApplicationInitializationException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionWithoutCause() {
        String message = "Initialization failed";

        ApplicationInitializationException exception = new ApplicationInitializationException(message, null);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
}
