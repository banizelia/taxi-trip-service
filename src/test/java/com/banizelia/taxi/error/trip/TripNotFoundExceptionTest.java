package com.banizelia.taxi.error.trip;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TripNotFoundExceptionTest {

    @Test
    void testTripNotFoundExceptionMessage() {
        Long tripId = 456L;

        TripNotFoundException exception = new TripNotFoundException(tripId);

        String expectedMessage = String.format("Such trip doesn't exist: %d", tripId);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
