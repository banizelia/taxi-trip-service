//package com.web.repository;
//
//import com.web.model.FavoriteTrip;
//import com.web.model.Trip;
//import com.web.repository.FavoriteTripRepository;
//import com.web.service.favorite.FavoriteTripService;
//import com.web.service.trip.TripService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class FavoriteTripRepositoryTest {
//
//    @Autowired
//    private FavoriteTripRepository favoriteTripRepository;
//
//    @Test
//    void testGetFavouriteTrips() {
//        // Вызов метода для получения списка любимых поездок
//        List<Trip> favoriteTrips = favoriteTripRepository.getFavouriteTrips();
//
//        // Проверка корректности полученных данных
//        assertNotNull(favoriteTrips);
//        assertTrue(favoriteTrips.isEmpty());
//    }
//
//    @Test
//    void testFindMaxPosition() {
//        // Вызов метода для получения максимальной позиции
//        Long maxPosition = favoriteTripRepository.findMaxPosition();
//
//        // Проверка, что позиция корректна
//        assertNotNull(maxPosition);
//        assertTrue(maxPosition >= 0);
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    void testIncrementPositions() {
//        Long newPosition = 1L;
//        Long oldPosition = 5L;
//
//        // Выполнение обновления позиций
//        favoriteTripRepository.incrementPositions(newPosition, oldPosition);
//
//        List<FavoriteTrip> favoriteTrips = favoriteTripRepository.findAll();
//        favoriteTrips.forEach(trip -> {
//            if (trip.getPosition() >= newPosition && trip.getPosition() < oldPosition) {
//                assertEquals(trip.getPosition(), trip.getPosition() + 1);
//            }
//        });
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    void testDecrementPositions() {
//        Long oldPosition = 5L;
//        Long newPosition = 1L;
//
//        // Выполнение обновления позиций
//        favoriteTripRepository.decrementPositions(oldPosition, newPosition);
//
//        // Проверка, что позиции были уменьшены корректно
//        List<FavoriteTrip> favoriteTrips = favoriteTripRepository.findAll();
//        favoriteTrips.forEach(trip -> {
//            if (trip.getPosition() > oldPosition && trip.getPosition() <= newPosition) {
//                assertEquals(trip.getPosition(), trip.getPosition() - 1);
//            }
//        });
//    }
//}
