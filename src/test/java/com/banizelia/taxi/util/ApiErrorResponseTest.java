package com.banizelia.taxi.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiErrorResponseTest {

    @Test
    void testApiErrorResponseCreation() {
        ApiErrorResponse response = new ApiErrorResponse(404, "Not Found", "The resource was not found");

        assertEquals(404, response.status());
        assertEquals("Not Found", response.error());
        assertEquals("The resource was not found", response.message());
    }

    @Test
    void testApiErrorResponseWithDifferentValues() {
        ApiErrorResponse response = new ApiErrorResponse(500, "Internal Server Error", "An unexpected error occurred");

        assertEquals(500, response.status());
        assertEquals("Internal Server Error", response.error());
        assertEquals("An unexpected error occurred", response.message());
    }
}

