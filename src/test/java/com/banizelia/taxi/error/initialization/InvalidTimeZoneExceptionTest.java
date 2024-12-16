package com.banizelia.taxi.error.initialization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidTimeZoneExceptionTest {

    @Test
    void testInvalidTimeZoneExceptionMessage() {
        String invalidTimeZone = "UnknownZone";

        InvalidTimeZoneException exception = new InvalidTimeZoneException(invalidTimeZone);

        String expectedMessage = "Invalid timezone specified: " + invalidTimeZone;
        assertEquals(expectedMessage, exception.getMessage());
    }
}
