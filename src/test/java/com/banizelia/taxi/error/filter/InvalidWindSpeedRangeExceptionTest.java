package com.banizelia.taxi.error.filter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidWindSpeedRangeExceptionTest {
    @Test
    void testInvalidWindSpeedRangeExceptionMessage() {
        Double minWindSpeed = 10.0;
        Double maxWindSpeed = 5.0;

        InvalidWindSpeedRangeException exception = new InvalidWindSpeedRangeException(minWindSpeed, maxWindSpeed);

        assertTrue(exception.getMessage().contains("maxWindSpeed is smaller or equal to minWindSpeed"));
    }
}
