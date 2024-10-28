//package com.web.repository;
//
//import com.web.model.FavoriteTrip;
//import com.web.model.Trip;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@Testcontainers
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class FavoriteTripRepositoryTest {
//
//    @Container
//    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14")
//            .withDatabaseName("testdb")
//            .withUsername("test")
//            .withPassword("test");
//
//    @DynamicPropertySource
//    static void setProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", postgresContainer::getUsername);
//        registry.add("spring.datasource.password", postgresContainer::getPassword);
//    }
//
//    @Autowired
//    private FavoriteTripRepository favoriteTripRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    private Trip trip1;
//    private Trip trip2;
//    private Trip trip3;
//    private FavoriteTrip favoriteTrip1;
//    private FavoriteTrip favoriteTrip2;
//    private FavoriteTrip favoriteTrip3;
//
//    @BeforeEach
//    void setUp() {
//        // Очищаем таблицы перед каждым тестом
//        entityManager.clear();
//        favoriteTripRepository.deleteAll();
//
//        // Создаем тестовые Trip записи
//        trip1 = new Trip();
//        trip2 = new Trip();
//        trip3 = new Trip();
//
//        entityManager.persist(trip1);
//        entityManager.persist(trip2);
//        entityManager.persist(trip3);
//
//        // Создаем тестовые FavoriteTrip записи
//        favoriteTrip1 = new FavoriteTrip();
//        favoriteTrip1.setTripId(trip1.getId());
//        favoriteTrip1.setPosition(1L);
//
//        favoriteTrip2 = new FavoriteTrip();
//        favoriteTrip2.setTripId(trip2.getId());
//        favoriteTrip2.setPosition(2L);
//
//        favoriteTrip3 = new FavoriteTrip();
//        favoriteTrip3.setTripId(trip3.getId());
//        favoriteTrip3.setPosition(3L);
//
//        entityManager.persist(favoriteTrip1);
//        entityManager.persist(favoriteTrip2);
//        entityManager.persist(favoriteTrip3);
//
//        entityManager.flush();
//    }
//
//    @Test
//    void getTripsByPositionAsc_ShouldReturnTripsInCorrectOrder() {
//        // Act
//        List<Trip> trips = favoriteTripRepository.getTripsByPositionAsc();
//
//        // Assert
//        assertEquals(3, trips.size());
//        assertEquals(trip1.getId(), trips.get(0).getId());
//        assertEquals(trip2.getId(), trips.get(1).getId());
//        assertEquals(trip3.getId(), trips.get(2).getId());
//    }
//
//    @Test
//    void getFavouriteTripsByPositionAsc_ShouldReturnFavoriteTripsInCorrectOrder() {
//        // Act
//        List<FavoriteTrip> favoriteTrips = favoriteTripRepository.getFavouriteTripsByPositionAsc();
//
//        // Assert
//        assertEquals(3, favoriteTrips.size());
//        assertEquals(1L, favoriteTrips.get(0).getPosition());
//        assertEquals(2L, favoriteTrips.get(1).getPosition());
//        assertEquals(3L, favoriteTrips.get(2).getPosition());
//    }
//
//    @Test
//    void findAllByPositionBetweenOrderByPositionAsc_ShouldReturnTripsWithinRange() {
//        // Act
//        List<FavoriteTrip> favoriteTrips = favoriteTripRepository
//                .findAllByPositionBetweenOrderByPositionAsc(1L, 2L);
//
//        // Assert
//        assertEquals(2, favoriteTrips.size());
//        assertEquals(1L, favoriteTrips.get(0).getPosition());
//        assertEquals(2L, favoriteTrips.get(1).getPosition());
//    }
//
//    @Test
//    void findMaxPosition_ShouldReturnHighestPosition() {
//        // Act
//        Long maxPosition = favoriteTripRepository.findMaxPosition();
//
//        // Assert
//        assertEquals(3L, maxPosition);
//    }
//
//    @Test
//    void findMaxPosition_WhenNoRecords_ShouldReturnZero() {
//        // Arrange
//        favoriteTripRepository.deleteAll();
//        entityManager.flush();
//
//        // Act
//        Long maxPosition = favoriteTripRepository.findMaxPosition();
//
//        // Assert
//        assertEquals(0L, maxPosition);
//    }
//
//    @Test
//    void findMinPosition_ShouldReturnLowestPosition() {
//        // Act
//        Optional<Long> minPosition = favoriteTripRepository.findMinPosition();
//
//        // Assert
//        assertTrue(minPosition.isPresent());
//        assertEquals(1L, minPosition.get());
//    }
//
//    @Test
//    void findMinPosition_WhenNoRecords_ShouldReturnEmpty() {
//        // Arrange
//        favoriteTripRepository.deleteAll();
//        entityManager.flush();
//
//        // Act
//        Optional<Long> minPosition = favoriteTripRepository.findMinPosition();
//
//        // Assert
//        assertTrue(minPosition.isEmpty());
//    }
//
//    @Test
//    void findPositionByIndex_ShouldReturnCorrectPosition() {
//        // Act
//        Optional<Long> position = favoriteTripRepository.findPositionByIndex(1L);
//
//        // Assert
//        assertTrue(position.isPresent());
//        assertEquals(2L, position.get());
//    }
//
//    @Test
//    void findPositionByIndex_WhenIndexOutOfRange_ShouldReturnEmpty() {
//        // Act
//        Optional<Long> position = favoriteTripRepository.findPositionByIndex(10L);
//
//        // Assert
//        assertTrue(position.isEmpty());
//    }
//
//    @Test
//    void findByTripId_ShouldReturnCorrectFavoriteTrip() {
//        // Act
//        Optional<FavoriteTrip> foundTrip = favoriteTripRepository.findByTripId(trip1.getId());
//
//        // Assert
//        assertTrue(foundTrip.isPresent());
//        assertEquals(favoriteTrip1.getPosition(), foundTrip.get().getPosition());
//    }
//
//    @Test
//    void findByTripId_WhenTripNotExists_ShouldReturnEmpty() {
//        // Act
//        Optional<FavoriteTrip> foundTrip = favoriteTripRepository.findByTripId(999L);
//
//        // Assert
//        assertTrue(foundTrip.isEmpty());
//    }
//}