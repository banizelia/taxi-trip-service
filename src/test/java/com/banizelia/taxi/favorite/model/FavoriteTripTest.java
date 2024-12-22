package com.banizelia.taxi.favorite.model;

import com.banizelia.taxi.trip.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteTripTest {

    private FavoriteTrip favoriteTrip;

    @BeforeEach
    void setUp() {
        favoriteTrip = new FavoriteTrip();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(favoriteTrip);
        assertNull(favoriteTrip.getId());
        assertNull(favoriteTrip.getTripId());
        assertNull(favoriteTrip.getPosition());
        assertNull(favoriteTrip.getVersion());
        assertNull(favoriteTrip.getTrip());
    }

    @Test
    void testCompleteObject() {
        Long id = 1L;
        Long tripId = 2L;
        Long position = 100L;
        Long version = 1L;
        Trip trip = new Trip();
        trip.setId(tripId);

        favoriteTrip.setId(id);
        favoriteTrip.setTripId(tripId);
        favoriteTrip.setPosition(position);
        favoriteTrip.setVersion(version);
        favoriteTrip.setTrip(trip);

        assertAll(
                () -> assertEquals(id, favoriteTrip.getId()),
                () -> assertEquals(tripId, favoriteTrip.getTripId()),
                () -> assertEquals(position, favoriteTrip.getPosition()),
                () -> assertEquals(version, favoriteTrip.getVersion()),
                () -> assertNotNull(favoriteTrip.getTrip()),
                () -> assertEquals(trip, favoriteTrip.getTrip())
        );
    }

    @Nested
    class BasicFieldsTests {
        @Test
        void testId() {
            Long id = 1L;

            favoriteTrip.setId(id);

            assertEquals(id, favoriteTrip.getId());
        }

        @Test
        void testTripId() {
            Long tripId = 1L;

            favoriteTrip.setTripId(tripId);

            assertEquals(tripId, favoriteTrip.getTripId());
        }

        @Test
        void testPosition() {
            Long position = 100L;

            favoriteTrip.setPosition(position);

            assertEquals(position, favoriteTrip.getPosition());
        }

        @Test
        void testVersion() {
            Long version = 1L;

            favoriteTrip.setVersion(version);

            assertEquals(version, favoriteTrip.getVersion());
        }
    }

    @Nested
    class RelationshipTests {
        @Test
        void testTripRelationship() {
            Trip trip = new Trip();
            trip.setId(1L);

            favoriteTrip.setTrip(trip);

            assertNotNull(favoriteTrip.getTrip());
            assertEquals(trip, favoriteTrip.getTrip());
            assertEquals(trip.getId(), favoriteTrip.getTrip().getId());
        }

        @Test
        void testTripRelationshipNull() {
            favoriteTrip.setTrip(null);

            assertNull(favoriteTrip.getTrip());
        }
    }
}