package com.web.favorite.service;

import com.web.common.exception.position.PositionException;
import com.web.common.exception.trip.TripNotFoundException;
import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
import com.web.favorite.service.position.PositionCalculator;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Execute successfully with valid tripId and targetPosition")
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
    @DisplayName("Execute throws PositionException when targetPosition is less than 1")
    void execute_TargetPositionLessThanOne_ThrowsPositionException() {
        // Arrange
        Long tripId = 1L;
        Long targetPosition = 0L;

        // Act & Assert
        PositionException exception = assertThrows(PositionException.class, () ->
                dragAndDropFavoriteTripService.execute(tripId, targetPosition)
        );

        assertEquals("Target position 0 is out of bounds, id: 1, ", exception.getMessage());
        verify(favoriteTripRepository, never()).findByTripId(anyLong());
        verify(positionCalculator, never()).calculateNewPosition(anyLong());
        verify(favoriteTripRepository, never()).save(any(FavoriteTrip.class));
    }

    @Test
    @DisplayName("Execute throws TripNotFoundException when tripId is not found")
    void execute_TripNotFound_ThrowsTripNotFoundException() {
        // Arrange
        Long tripId = 999L;
        Long targetPosition = 5L;

        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.empty());

        // Act & Assert
        TripNotFoundException exception = assertThrows(TripNotFoundException.class, () ->
                dragAndDropFavoriteTripService.execute(tripId, targetPosition)
        );

        assertEquals("Such trip doesn't exist: 999", exception.getMessage());
        verify(favoriteTripRepository, times(1)).findByTripId(tripId);
        verify(positionCalculator, never()).calculateNewPosition(anyLong());
        verify(favoriteTripRepository, never()).save(any(FavoriteTrip.class));
    }

    @Test
    @DisplayName("Execute throws OptimisticLockException when save fails due to optimistic locking")
    void execute_OptimisticLockException_ThrowsOptimisticLockException() {
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
        when(favoriteTripRepository.save(favoriteTrip)).thenThrow(new OptimisticLockException("Optimistic lock failed"));

        // Act & Assert
        OptimisticLockException exception = assertThrows(OptimisticLockException.class, () ->
                dragAndDropFavoriteTripService.execute(tripId, targetPosition)
        );

        assertEquals("Optimistic lock failed", exception.getMessage());
        verify(favoriteTripRepository, times(1)).findByTripId(tripId);
        verify(positionCalculator, times(1)).calculateNewPosition(targetPosition);
        verify(favoriteTripRepository, times(1)).save(favoriteTrip);
    }
}