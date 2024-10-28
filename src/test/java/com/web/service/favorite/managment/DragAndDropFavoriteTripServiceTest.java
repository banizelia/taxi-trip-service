package com.web.service.favorite.managment;

import com.web.exceptions.TripNotFoundException;
import com.web.model.FavoriteTrip;
import com.web.model.enums.FavoriteTripEnum;
import com.web.repository.FavoriteTripRepository;
import com.web.service.favorite.managment.common.SparsifierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DragAndDropFavoriteTripServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @Mock
    private SparsifierService sparsifier;

    @InjectMocks
    private DragAndDropFavoriteTripService dragAndDropFavoriteTripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute_moveToFirstPosition() {
        // Arrange
        Long tripId = 1L;
        Long targetPosition = 1L;
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.of(favoriteTrip));
        when(favoriteTripRepository.findMinPosition()).thenReturn(Optional.of(100L));
        when(favoriteTripRepository.count()).thenReturn(10L);

        // Act
        dragAndDropFavoriteTripService.execute(tripId, targetPosition);

        // Assert
        verify(favoriteTripRepository).save(favoriteTrip);
        verify(sparsifier, atLeastOnce()).sparsify();
    }

    @Test
    void testExecute_moveToMiddlePosition_withRebalance() {
        // Arrange
        Long tripId = 1L;
        Long targetPosition = 5L;
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        long smallGap = FavoriteTripEnum.MIN_GAP.getValue() - 1;

        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.of(favoriteTrip));
        when(favoriteTripRepository.findPositionByIndex(targetPosition - 2)).thenReturn(Optional.of(200L));
        when(favoriteTripRepository.findPositionByIndex(targetPosition - 1)).thenReturn(Optional.of(200L + smallGap));
        when(favoriteTripRepository.count()).thenReturn(10L);

        // Act
        dragAndDropFavoriteTripService.execute(tripId, targetPosition);

        // Assert
        verify(sparsifier).sparsify();
        verify(favoriteTripRepository).save(favoriteTrip);
    }

    @Test
    void testExecute_tripNotFound() {
        // Arrange
        Long tripId = 1L;
        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TripNotFoundException.class, () -> dragAndDropFavoriteTripService.execute(tripId, 1L));
        verify(favoriteTripRepository, never()).save(any());
    }

    @Test
    void testExecute_targetPositionOutOfBounds() {
        // Arrange
        Long tripId = 1L;
        Long targetPosition = 11L;  // Assume total count is 10
        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.of(new FavoriteTrip()));
        when(favoriteTripRepository.count()).thenReturn(10L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> dragAndDropFavoriteTripService.execute(tripId, targetPosition));
        verify(favoriteTripRepository, never()).save(any());
    }
}
