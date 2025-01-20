package com.banizelia.taxi.error.trip;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TripAlreadyInFavoritesExceptionTest {

    @Test
    void testTripAlreadyInFavoritesExceptionMessage() {
        Long tripId = 123L;

        TripAlreadyInFavoritesException exception = new TripAlreadyInFavoritesException(tripId);

        String expectedMessage = String.format("Trip with id %d is already in favorites", tripId);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testTripIdGetter() {
        Long tripId = 123L;

        TripAlreadyInFavoritesException exception = new TripAlreadyInFavoritesException(tripId);

        assertEquals(tripId, exception.getTripId());
    }
}
