package com.banizelia.taxi.error.filter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidSortFieldExceptionTest {

    @Test
    void testInvalidSortFieldExceptionMessage() {
        String sortBy = "invalidField";

        InvalidSortFieldException exception = new InvalidSortFieldException(sortBy);

        String expectedMessage = String.format("Invalid sort field: %s", sortBy);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
