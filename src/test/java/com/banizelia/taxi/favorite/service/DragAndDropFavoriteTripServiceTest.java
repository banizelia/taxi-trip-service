package com.banizelia.taxi.favorite.service;

import com.banizelia.taxi.error.position.PositionException;
import com.banizelia.taxi.error.trip.TripNotFoundException;
import com.banizelia.taxi.favorite.model.FavoriteTrip;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import com.banizelia.taxi.favorite.service.position.PositionCalculator;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DragAndDropFavoriteTripServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @Mock
    private PositionCalculator positionCalculator;

    @InjectMocks
    private DragAndDropFavoriteTripService dragAndDropFavoriteTripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_Successful() {
        // Arrange
        Long tripId = 1L;
        Long targetPosition = 5L;
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setId(10L);
        favoriteTrip.setTripId(tripId);
        favoriteTrip.setPosition(3L);

        Long newPosition = 5L;

        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.of(favoriteTrip));
        when(positionCalculator.calculateNewPosition(targetPosition)).thenReturn(newPosition);
        when(favoriteTripRepository.save(favoriteTrip)).thenReturn(favoriteTrip);

        // Act
        assertDoesNotThrow(() -> dragAndDropFavoriteTripService.execute(tripId, targetPosition));

        // Assert
        assertEquals(newPosition, favoriteTrip.getPosition());
        verify(favoriteTripRepository, times(1)).findByTripId(tripId);
        verify(positionCalculator, times(1)).calculateNewPosition(targetPosition);
        verify(favoriteTripRepository, times(1)).save(favoriteTrip);
    }

    @Test
    void execute_TargetPositionLessThanOne_ThrowsPositionException() {
        Long tripId = 1L;
        Long targetPosition = 0L;

        assertThrows(PositionException.class, () -> dragAndDropFavoriteTripService.execute(tripId, targetPosition));
        verify(favoriteTripRepository, never()).findByTripId(anyLong());
        verify(positionCalculator, never()).calculateNewPosition(anyLong());
        verify(favoriteTripRepository, never()).save(any(FavoriteTrip.class));
    }

    @Test
    void execute_TripNotFound_ThrowsTripNotFoundException() {
        Long tripId = 999L;
        Long targetPosition = 5L;

        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.empty());

        assertThrows(TripNotFoundException.class, () -> dragAndDropFavoriteTripService.execute(tripId, targetPosition));
        verify(favoriteTripRepository, times(1)).findByTripId(tripId);
        verify(positionCalculator, never()).calculateNewPosition(anyLong());
        verify(favoriteTripRepository, never()).save(any(FavoriteTrip.class));
    }

    @Test
    void execute_OptimisticLockException_ThrowsOptimisticLockException() {
        Long tripId = 1L;
        Long targetPosition = 5L;
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setId(10L);
        favoriteTrip.setTripId(tripId);
        favoriteTrip.setPosition(3L);

        Long newPosition = 5L;

        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.of(favoriteTrip));
        when(positionCalculator.calculateNewPosition(targetPosition)).thenReturn(newPosition);
        when(favoriteTripRepository.save(favoriteTrip)).thenThrow(new OptimisticLockException("Optimistic lock failed"));

        assertThrows(OptimisticLockException.class, () -> dragAndDropFavoriteTripService.execute(tripId, targetPosition));
        verify(favoriteTripRepository, times(1)).findByTripId(tripId);
        verify(positionCalculator, times(1)).calculateNewPosition(targetPosition);
        verify(favoriteTripRepository, times(1)).save(favoriteTrip);
    }
}