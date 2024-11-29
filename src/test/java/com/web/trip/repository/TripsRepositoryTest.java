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
        @DisplayName("Должен возвращать поездки в заданном диапазоне дат и скорости ветра")
        void filter_ReturnsCorrectTrips() {
            LocalDateTime startDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 2, 23, 59);
            double minWindSpeed = 0.0;
            double maxWindSpeed = 10.0;
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

            Page<Trip> result = tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

            assertFalse(result.isEmpty());
            assertEquals(1000, result.getTotalElements());
            Trip trip = result.getContent().get(0);
            double windSpeed = trip.getWeather().getAverageWindSpeed();
            assertEquals(1, (long) trip.getId());
            assertTrue(windSpeed >= minWindSpeed && windSpeed <= maxWindSpeed);
        }

        @Test
        @DisplayName("Должен возвращать пустую страницу, если нет соответствующих поездок")
        void filter_NoMatchingTrips_ReturnsEmptyPage() {
            LocalDateTime startDateTime = LocalDateTime.of(2023, 1, 3, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(2023, 1, 4, 23, 59);
            Double minWindSpeed = 15.0;
            Double maxWindSpeed = 20.0;
            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

            Page<Trip> result = tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Тесты для метода findAllStream")
    class FindAllStreamMethodTests {

        @Test
        @DisplayName("Должен возвращать все поездки как стрим")
        void findAllStream_ReturnsAllTrips() {
            List<Trip> trips = StreamSupport.stream(tripsRepository.findAllStream().spliterator(), false)
                    .toList();

            assertEquals(1000, trips.size());
        }
    }
}