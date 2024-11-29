package com.web.favorite.repository;

import com.web.favorite.model.FavoriteTrip;
import com.web.trip.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE) // Prevent replacing with H2
class FavoriteTripRepositoryTest {

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

    @Autowired
    private FavoriteTripRepository favoriteTripRepository;

    @BeforeEach
    void setUp() {
        favoriteTripRepository.deleteAll();

        FavoriteTrip trip1 = new FavoriteTrip();
        trip1.setTripId(101L);
        favoriteTripRepository.save(trip1);

        FavoriteTrip trip2 = new FavoriteTrip();
        trip2.setTripId(102L);
        favoriteTripRepository.save(trip2);
    }

    @Test
    void findAllWithPagination_ShouldReturnPaginatedTrips() {
        Pageable pageable = PageRequest.of(0, 1);

        Page<Trip> page = favoriteTripRepository.findAllWithPagination(pageable);

        assertNotNull(page, "Page should not be null");
        assertEquals(2, page.getTotalElements(), "Total elements should be 2"); // Adjusted based on migration data
        assertEquals(1, page.getContent().size(), "Content size should be 1");
    }

    @Test
    void findAllByOrderByPositionAsc_ShouldReturnAllTripsOrdered() {
        List<FavoriteTrip> trips = favoriteTripRepository.findAllByOrderByPositionAsc();
        assertNotNull(trips);
        assertEquals(2, trips.size());
        assertEquals(101L, trips.get(0).getTripId());
        assertEquals(102L, trips.get(1).getTripId());
    }

    @Test
    void findByTripId_ShouldReturnFavoriteTrip() {
        Optional<FavoriteTrip> tripOpt = favoriteTripRepository.findByTripId(101L);
        assertTrue(tripOpt.isPresent());
        assertEquals(101L, tripOpt.get().getTripId());

        tripOpt = favoriteTripRepository.findByTripId(999L);
        assertFalse(tripOpt.isPresent());
    }
}
