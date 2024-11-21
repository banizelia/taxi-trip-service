package com.web.favorite.service;

import com.web.common.exception.position.PositionException;
import com.web.common.exception.trip.TripNotFoundException;
import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
import com.web.favorite.service.common.PositionCalculator;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DragAndDropFavoriteTripServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @Mock
    private PositionCalculator positionCalculator;

    @InjectMocks
    private DragAndDropFavoriteTripService service;

    private FavoriteTrip favoriteTrip;
    private static final Long TRIP_ID = 1L;
    private static final Long TARGET_POSITION = 2L;
    private static final Long NEW_POSITION = 3L;

    @BeforeEach
    void setUp() {
        favoriteTrip = new FavoriteTrip();
        favoriteTrip.setId(1L);
        favoriteTrip.setTripId(TRIP_ID);
        favoriteTrip.setPosition(1L);
    }

    @Test
    void execute_ShouldUpdatePosition_WhenValidInput() {
        // Arrange
        when(favoriteTripRepository.findByTripId(TRIP_ID)).thenReturn(Optional.of(favoriteTrip));
        when(positionCalculator.calculateNewPosition(TARGET_POSITION)).thenReturn(NEW_POSITION);

        // Act
        service.execute(TRIP_ID, TARGET_POSITION);

        // Assert
        assertEquals(NEW_POSITION, favoriteTrip.getPosition());
        verify(favoriteTripRepository).findByTripId(TRIP_ID);
        verify(favoriteTripRepository, times(2)).save(favoriteTrip);
        verify(positionCalculator).calculateNewPosition(TARGET_POSITION);
    }

    @Test
    void execute_ShouldThrowPositionException_WhenTargetPositionIsLessThanOne() {
        // Act & Assert
        assertThrows(PositionException.class, () -> service.execute(TRIP_ID, 0L));
        verify(favoriteTripRepository, never()).findByTripId(any());
        verify(positionCalculator, never()).calculateNewPosition(any());
    }

    @Test
    void execute_ShouldThrowTripNotFoundException_WhenTripNotFound() {
        // Arrange
        when(favoriteTripRepository.findByTripId(TRIP_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TripNotFoundException.class, () -> service.execute(TRIP_ID, TARGET_POSITION));
        verify(positionCalculator, never()).calculateNewPosition(any());
        verify(favoriteTripRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowOptimisticLockException_WhenConcurrentModification() {
        // Arrange
        when(favoriteTripRepository.findByTripId(TRIP_ID)).thenReturn(Optional.of(favoriteTrip));
        when(positionCalculator.calculateNewPosition(TARGET_POSITION)).thenReturn(NEW_POSITION);
        when(favoriteTripRepository.save(favoriteTrip)).thenThrow(OptimisticLockException.class);

        // Act & Assert
        assertThrows(OptimisticLockException.class, () -> service.execute(TRIP_ID, TARGET_POSITION));
        verify(favoriteTripRepository).findByTripId(TRIP_ID);
        verify(positionCalculator).calculateNewPosition(TARGET_POSITION);
    }
}