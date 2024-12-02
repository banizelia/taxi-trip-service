package com.banizelia.taxi.error.filter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FilterValidationExceptionTest {

    @Test
    void testExceptionWithMessage() {
        String message = "Validation error";
        FilterValidationException exception = new FilterValidationException(message);

        assertEquals(message, exception.getMessage());
    }
}
