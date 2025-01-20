package com.banizelia.taxi.error.trip;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FavoriteTripModificationExceptionTest {

    @Test
    void testFavoriteTripModificationException() {
        String message = "Test error message.";
        Throwable cause = new RuntimeException("Cause of the error.");
        FavoriteTripModificationException exception = new FavoriteTripModificationException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
