package com.web.trip.repository;

import com.web.trip.model.Trip;
import com.web.weather.repository.WeatherRepository;
import org.junit.jupiter.api.DisplayName;
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

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("12345678");

    @Autowired
    private TripsRepository tripsRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Nested
    @DisplayName("Тесты для метода filter")
    class FilterMethodTests {

        @Test
        @DisplayName("Должен возвращать поездки в заданном диапазоне дат и скорости ветра без фильтрации по избранному")
        void filter_ReturnsCorrectTrips_NoFavoriteFilter() {
            Boolean isFavorite = null; // Нет фильтрации по избранному
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 2, 23, 59);
            double minWindSpeed = 0.0;
            double maxWindSpeed = 10.0;
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

            Page<Trip> result = tripsRepository.filter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

            assertFalse(result.isEmpty());
            assertEquals(1000, result.getTotalElements());
            Trip trip = result.getContent().get(0);
            double windSpeed = trip.getWeather().getAverageWindSpeed();
            assertEquals(1, trip.getId());
            assertTrue(windSpeed >= minWindSpeed && windSpeed <= maxWindSpeed);
        }

        @Test
        @DisplayName("Должен возвращать поездки в заданном диапазоне дат и скорости ветра с фильтром по избранному (избранные)")
        void filter_ReturnsFavoriteTrips() {
            Boolean isFavorite = true; // Фильтрация только избранных поездок
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 1, 23, 59);
            double minWindSpeed = 0.0;
            double maxWindSpeed = 10.0;
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

            Page<Trip> result = tripsRepository.filter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

            assertFalse(result.isEmpty());
            // Предположим, что в тестовой базе данных есть 500 избранных поездок
            assertEquals(99, result.getTotalElements());
            Trip trip = result.getContent().get(0);
            assertNotNull(trip.getFavoriteTrip());
        }

        @Test
        @DisplayName("Должен возвращать поездки без избранного в заданном диапазоне дат и скорости ветра")
        void filter_ReturnsNonFavoriteTrips() {
            Boolean isFavorite = false; // Фильтрация только не избранных поездок
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 1, 23, 59);
            double minWindSpeed = 0.0;
            double maxWindSpeed = 10.0;
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

            Page<Trip> result = tripsRepository.filter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

            assertFalse(result.isEmpty());
            // Предположим, что в тестовой базе данных есть 500 не избранных поездок
            assertEquals(901, result.getTotalElements());
            Trip trip = result.getContent().get(0);
            assertNull(trip.getFavoriteTrip());
        }

        @Test
        @DisplayName("Должен возвращать пустую страницу, если нет соответствующих поездок")
        void filter_NoMatchingTrips_ReturnsEmptyPage() {
            Boolean isFavorite = null; // Нет фильтрации по избранному
            LocalDateTime startDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 2, 23, 59);
            Double minWindSpeed = 15.0;
            Double maxWindSpeed = 20.0;
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

            Page<Trip> result = tripsRepository.filter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Тесты для метода streamFilter")
    class StreamFilterMethodTests {

        @Test
        @DisplayName("Должен возвращать все поездки как стрим без фильтрации по избранному")
        void streamFilter_ReturnsAllTrips_NoFavoriteFilter() {
            Boolean isFavorite = null; // Нет фильтрации по избранному
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 1, 23, 59);
            Double minWindSpeed = 0.0;
            Double maxWindSpeed = 100.0;
            Pageable pageable = Pageable.unpaged(); // Без пагинации

            List<Trip> trips = StreamSupport.stream(tripsRepository.streamFilter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable).spliterator(), false)
                    .toList();

            assertEquals(1000, trips.size());
        }

        @Test
        @DisplayName("Должен возвращать только избранные поездки как стрим")
        void streamFilter_ReturnsFavoriteTrips() {
            Boolean isFavorite = true; // Фильтрация только избранных поездок
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 1, 23, 59);
            Double minWindSpeed = 0.0;
            Double maxWindSpeed = 100.0;
            Pageable pageable = Pageable.unpaged(); // Без пагинации

            List<Trip> trips = StreamSupport.stream(tripsRepository.streamFilter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable).spliterator(), false)
                    .toList();

            // Предположим, что в тестовой базе данных есть 500 избранных поездок
            assertEquals(99, trips.size());
            trips.forEach(trip -> assertNotNull(trip.getFavoriteTrip()));
        }

        @Test
        @DisplayName("Должен возвращать только не избранные поездки как стрим")
        void streamFilter_ReturnsNonFavoriteTrips() {
            Boolean isFavorite = false; // Фильтрация только не избранных поездок
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 1, 23, 59);
            Double minWindSpeed = 0.0;
            Double maxWindSpeed = 100.0;
            Pageable pageable = Pageable.unpaged(); // Без пагинации

            List<Trip> trips = StreamSupport.stream(tripsRepository.streamFilter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable).spliterator(), false)
                    .toList();

            // Предположим, что в тестовой базе данных есть 500 не избранных поездок
            assertEquals(901, trips.size());
            trips.forEach(trip -> assertNull(trip.getFavoriteTrip()));
        }

        @Test
        @DisplayName("Должен возвращать пустой стрим, если нет соответствующих поездок")
        void streamFilter_NoMatchingTrips_ReturnsEmptyStream() {
            Boolean isFavorite = null; // Нет фильтрации по избранному
            LocalDateTime startDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2025, 1, 2, 23, 59);
            Double minWindSpeed = 15.0;
            Double maxWindSpeed = 20.0;
            Pageable pageable = Pageable.unpaged(); // Без пагинации

            List<Trip> trips = StreamSupport.stream(tripsRepository.streamFilter(isFavorite, startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable).spliterator(), false)
                    .toList();

            assertTrue(trips.isEmpty());
        }
    }
}