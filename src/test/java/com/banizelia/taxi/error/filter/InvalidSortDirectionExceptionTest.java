package com.banizelia.taxi.error.filter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidSortDirectionExceptionTest {

    @Test
    void testInvalidSortDirectionExceptionMessage() {
        String direction = "UNKNOWN";

        InvalidSortDirectionException exception = new InvalidSortDirectionException(direction);

        String expectedMessage = String.format("Invalid direction: %s", direction);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
