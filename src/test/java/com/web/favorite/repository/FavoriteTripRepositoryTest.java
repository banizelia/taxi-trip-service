//package com.web.favorite.repository;
//
//import com.web.favorite.model.FavoriteTrip;
//import com.web.trip.model.Trip;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestPropertySource(
//        properties = {
//                "spring.datasource.url=jdbc:postgresql://localhost:5432/taxi_trips_test"
//        }
//)
//@DataJpaTest
//@ActiveProfiles("test")
//class FavoriteTripRepositoryTest {
//
//    @Autowired
//    private FavoriteTripRepository repository;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    private Trip trip1;
//    private Trip trip2;
//    private FavoriteTrip favoriteTrip1;
//    private FavoriteTrip favoriteTrip2;
//
//    @BeforeEach
//    void setUp() {
//        // Create and persist trips
//        trip1 = new Trip();
//        trip1.setVendorId("vendor1");
//        trip1.setPickupDatetime(LocalDateTime.now());
//        entityManager.persist(trip1);
//
//        trip2 = new Trip();
//        trip2.setVendorId("vendor2");
//        trip2.setPickupDatetime(LocalDateTime.now());
//        entityManager.persist(trip2);
//
//        // Create and persist favorite trips
//        favoriteTrip1 = new FavoriteTrip();
//        favoriteTrip1.setTripId(trip1.getId());
//        favoriteTrip1.setPosition(1L);
//        entityManager.persist(favoriteTrip1);
//
//        favoriteTrip2 = new FavoriteTrip();
//        favoriteTrip2.setTripId(trip2.getId());
//        favoriteTrip2.setPosition(2L);
//        entityManager.persist(favoriteTrip2);
//
//        entityManager.flush();
//        entityManager.clear();
//    }
//
//    @Test
//    void findAllWithPagination() {
//        PageRequest pageRequest = PageRequest.of(0, 10);
//        Page<Trip> result = repository.findAllWithPagination(pageRequest);
//
//        assertAll(
//                () -> assertNotNull(result),
//                () -> assertEquals(2, result.getTotalElements())
//        );
//    }
//
//    @Test
//    void findMaxPosition() {
//        Optional<Long> maxPosition = repository.findMaxPosition();
//
//        assertTrue(maxPosition.isPresent());
//        assertEquals(2L, maxPosition.get());
//    }
//
//    @Test
//    void findMinPosition() {
//        Optional<Long> minPosition = repository.findMinPosition();
//
//        assertTrue(minPosition.isPresent());
//        assertEquals(1L, minPosition.get());
//    }
//
//    @Test
//    void findPositionByIndex() {
//        Optional<Long> position = repository.findPositionByIndex(1L);
//
//        assertTrue(position.isPresent());
//        assertEquals(2L, position.get());
//    }
//
//    @Test
//    void findAllByOrderByPositionAsc() {
//        List<FavoriteTrip> trips = repository.findAllByOrderByPositionAsc();
//
//        assertAll(
//                () -> assertEquals(2, trips.size()),
//                () -> assertEquals(1L, trips.get(0).getPosition()),
//                () -> assertEquals(2L, trips.get(1).getPosition())
//        );
//    }
//
//    @Test
//    void findByTripId() {
//        Optional<FavoriteTrip> trip = repository.findByTripId(trip1.getId());
//
//        assertTrue(trip.isPresent());
//        assertEquals(trip1.getId(), trip.get().getTripId());
//    }
//
//    @Test
//    void findByTripId_NotFound() {
//        Optional<FavoriteTrip> trip = repository.findByTripId(999L);
//        assertTrue(trip.isEmpty());
//    }
//}