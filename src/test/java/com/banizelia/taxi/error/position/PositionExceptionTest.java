package com.banizelia.taxi.error.position;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionExceptionTest {

    @Test
    void testExceptionWithMessage() {
        String message = "Position error occurred";

        PositionException exception = new PositionException(message);

        assertEquals(message, exception.getMessage());
    }
}
