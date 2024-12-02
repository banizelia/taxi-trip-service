package com.banizelia.taxi.error.position;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionExceptionTest {

    @Test
    void testExceptionWithMessage() {
        String message = "Position error occurred";

        PositionException exception = new PositionException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testExceptionWithMessageAndCause() {
        String message = "Position error occurred";
        Throwable cause = new RuntimeException("Underlying cause");

        PositionException exception = new PositionException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
