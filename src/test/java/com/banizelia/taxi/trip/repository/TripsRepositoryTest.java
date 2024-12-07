package com.banizelia.taxi.trip.repository;

import com.banizelia.taxi.trip.model.Trip;
import com.banizelia.taxi.weather.repository.WeatherRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TripsRepositoryTest {
    @Autowired
    private TripsRepository tripsRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("12345678");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Nested
    class FilterMethodTests {

        @Test
        void filter_ReturnsCorrectTrips_NoFavoriteFilter() {
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 2, 23, 59);
            double minWindSpeed = 0.0;
            double maxWindSpeed = 10.0;
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

            Page<Trip> result = tripsRepository.filter(null, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

            assertFalse(result.isEmpty());
            assertEquals(1000, result.getTotalElements());
            Trip trip = result.getContent().getFirst();
            double windSpeed = trip.getWeather().getAverageWindSpeed();
            assertEquals(1, trip.getId());
            assertTrue(windSpeed >= minWindSpeed && windSpeed <= maxWindSpeed);
        }

        @Test
        void filter_ReturnsFavoriteTrips() {
            Boolean isFavorite = true;
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 1, 23, 59);
            double minWindSpeed = 0.0;
            double maxWindSpeed = 10.0;
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

            Page<Trip> result = tripsRepository.filter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

            assertFalse(result.isEmpty());
            assertEquals(99, result.getTotalElements());
            Trip trip = result.getContent().getFirst();
            assertNotNull(trip.getFavoriteTrip());
        }

        @Test
        void filter_ReturnsNonFavoriteTrips() {
            Boolean isFavorite = false;
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 1, 23, 59);
            double minWindSpeed = 0.0;
            double maxWindSpeed = 10.0;
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

            Page<Trip> result = tripsRepository.filter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

            assertFalse(result.isEmpty());
            assertEquals(901, result.getTotalElements());
            Trip trip = result.getContent().getFirst();
            assertNull(trip.getFavoriteTrip());
        }

        @Test
        void filter_NoMatchingTrips_ReturnsEmptyPage() {
            LocalDateTime startDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 2, 23, 59);
            Double minWindSpeed = 15.0;
            Double maxWindSpeed = 20.0;
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

            Page<Trip> result = tripsRepository.filter(null, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    class StreamFilterMethodTests {

        @Test
        void streamFilter_ReturnsAllTrips_NoFavoriteFilter() {
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 1, 23, 59);
            Double minWindSpeed = 0.0;
            Double maxWindSpeed = 100.0;

            List<Trip> trips = StreamSupport.stream(tripsRepository.streamFilter(null, startDateTime, endDateTime, minWindSpeed, maxWindSpeed).spliterator(), false)
                    .toList();

            assertEquals(1000, trips.size());
        }

        @Test
        void streamFilter_ReturnsFavoriteTrips() {
            Boolean isFavorite = true;
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 1, 23, 59);
            Double minWindSpeed = 0.0;
            Double maxWindSpeed = 100.0;

            List<Trip> trips = StreamSupport.stream(tripsRepository.streamFilter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed).spliterator(), false)
                    .toList();

            assertEquals(99, trips.size());
            trips.forEach(trip -> assertNotNull(trip.getFavoriteTrip()));
        }

        @Test
        void streamFilter_ReturnsNonFavoriteTrips() {
            Boolean isFavorite = false;
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 1, 23, 59);
            Double minWindSpeed = 0.0;
            Double maxWindSpeed = 100.0;

            List<Trip> trips = StreamSupport.stream(tripsRepository.streamFilter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed).spliterator(), false)
                    .toList();

            assertEquals(901, trips.size());
            trips.forEach(trip -> assertNull(trip.getFavoriteTrip()));
        }

        @Test
        void streamFilter_NoMatchingTrips_ReturnsEmptyStream() {
            LocalDateTime startDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 2, 23, 59);
            Double minWindSpeed = 15.0;
            Double maxWindSpeed = 20.0;

            List<Trip> trips = StreamSupport.stream(tripsRepository.streamFilter(null, startDateTime, endDateTime, minWindSpeed, maxWindSpeed).spliterator(), false)
                    .toList();

            assertTrue(trips.isEmpty());
        }
    }
}