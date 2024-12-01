package com.web.favorite.model;

import com.web.trip.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

public class FavoriteTripTest {

    private FavoriteTrip favoriteTrip;

    @BeforeEach
    void setUp() {
        favoriteTrip = new FavoriteTrip();
    }

    @Test
    void testNoArgsConstructor() {
        // Assert
        assertNotNull(favoriteTrip);
        assertNull(favoriteTrip.getId());
        assertNull(favoriteTrip.getTripId());
        assertNull(favoriteTrip.getPosition());
        assertNull(favoriteTrip.getVersion());
        assertNull(favoriteTrip.getTrip());
    }

    @Nested
    class BasicFieldsTests {
        @Test
        void testId() {
            // Arrange
            Long id = 1L;

            // Act
            favoriteTrip.setId(id);

            // Assert
            assertEquals(id, favoriteTrip.getId());
        }

        @Test
        void testTripId() {
            // Arrange
            Long tripId = 1L;

            // Act
            favoriteTrip.setTripId(tripId);

            // Assert
            assertEquals(tripId, favoriteTrip.getTripId());
        }

        @Test
        void testPosition() {
            // Arrange
            Long position = 100L;

            // Act
            favoriteTrip.setPosition(position);

            // Assert
            assertEquals(position, favoriteTrip.getPosition());
        }

        @Test
        void testVersion() {
            // Arrange
            Long version = 1L;

            // Act
            favoriteTrip.setVersion(version);

            // Assert
            assertEquals(version, favoriteTrip.getVersion());
        }
    }

    @Nested
    class RelationshipTests {
        @Test
        void testTripRelationship() {
            // Arrange
            Trip trip = new Trip();
            trip.setId(1L);

            // Act
            favoriteTrip.setTrip(trip);

            // Assert
            assertNotNull(favoriteTrip.getTrip());
            assertEquals(trip, favoriteTrip.getTrip());
            assertEquals(trip.getId(), favoriteTrip.getTrip().getId());
        }

        @Test
        void testTripRelationshipNull() {
            // Act
            favoriteTrip.setTrip(null);

            // Assert
            assertNull(favoriteTrip.getTrip());
        }
    }

    @Test
    void testCompleteObject() {
        // Arrange
        Long id = 1L;
        Long tripId = 2L;
        Long position = 100L;
        Long version = 1L;
        Trip trip = new Trip();
        trip.setId(tripId);

        // Act
        favoriteTrip.setId(id);
        favoriteTrip.setTripId(tripId);
        favoriteTrip.setPosition(position);
        favoriteTrip.setVersion(version);
        favoriteTrip.setTrip(trip);

        // Assert
        assertAll(
                () -> assertEquals(id, favoriteTrip.getId()),
                () -> assertEquals(tripId, favoriteTrip.getTripId()),
                () -> assertEquals(position, favoriteTrip.getPosition()),
                () -> assertEquals(version, favoriteTrip.getVersion()),
                () -> assertNotNull(favoriteTrip.getTrip()),
                () -> assertEquals(trip, favoriteTrip.getTrip())
        );
    }
}