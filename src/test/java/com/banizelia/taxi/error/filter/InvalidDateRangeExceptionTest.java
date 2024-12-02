package com.banizelia.taxi.error.filter;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class InvalidDateRangeExceptionTest {

    @Test
    void testInvalidDateRangeExceptionMessage() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 12, 31, 12, 0);

        InvalidDateRangeException exception = new InvalidDateRangeException(start, end);

        assertTrue(exception.getMessage().contains("endDateTime is before startDateTime"));
    }
}
