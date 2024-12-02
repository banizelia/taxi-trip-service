package com.banizelia.taxi.error.export;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExportExceptionTest {

    @Test
    void testExceptionWithMessageAndCause() {
        String message = "Test export exception";
        Throwable cause = new IllegalArgumentException("Invalid argument");

        ExportException exception = new ExportException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionWithoutCause() {
        String message = "Test export exception";

        ExportException exception = new ExportException(message, null);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
}
