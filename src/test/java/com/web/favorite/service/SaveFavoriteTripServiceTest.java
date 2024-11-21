package com.web.favorite.service;

import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
import com.web.favorite.service.common.PositionCalculator;
import com.web.trip.repository.TripsRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveFavoriteTripServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @Mock
    private TripsRepository tripsRepository;

    @Mock
    private PositionCalculator positionCalculator;

    @InjectMocks
    private SaveFavoriteTripService service;

    @Captor
    private ArgumentCaptor<FavoriteTrip> favoriteTripCaptor;

    private static final Long TRIP_ID = 1L;
    private static final Long MAX_POSITION = 100L;

    @BeforeEach
    void setUp() {
        // Common setup for successful case
        when(favoriteTripRepository.findByTripId(TRIP_ID)).thenReturn(Optional.empty());
        when(tripsRepository.existsById(TRIP_ID)).thenReturn(true);
        when(positionCalculator.calculateLastPosition()).thenReturn(MAX_POSITION);
    }

    @Test
    void execute_ShouldSaveFavoriteTrip_WhenValidInput() {
        // Act
        service.execute(TRIP_ID);

        // Assert
        verify(favoriteTripRepository).save(favoriteTripCaptor.capture());

        FavoriteTrip savedTrip = favoriteTripCaptor.getValue();
        assertEquals(TRIP_ID, savedTrip.getTripId());
        assertEquals(MAX_POSITION, savedTrip.getPosition());

        verify(favoriteTripRepository).findByTripId(TRIP_ID);
        verify(tripsRepository).existsById(TRIP_ID);
        verify(positionCalculator).calculateLastPosition();
    }

    @Test
    void execute_ShouldThrowOptimisticLockException_WhenConcurrentModification() {
        // Arrange
        when(favoriteTripRepository.save(any(FavoriteTrip.class)))
                .thenThrow(new OptimisticLockException("Concurrent modification"));

        // Act & Assert
        assertThrows(
                OptimisticLockException.class,
                () -> service.execute(TRIP_ID)
        );

        verify(favoriteTripRepository).findByTripId(TRIP_ID);
        verify(tripsRepository).existsById(TRIP_ID);
        verify(positionCalculator).calculateLastPosition();
        verify(favoriteTripRepository).save(any());
    }

    @Test
    void execute_ShouldSetCorrectPosition_WhenSaving() {
        // Arrange
        Long expectedPosition = 42L;
        when(positionCalculator.calculateLastPosition()).thenReturn(expectedPosition);

        // Act
        service.execute(TRIP_ID);

        // Assert
        verify(favoriteTripRepository).save(favoriteTripCaptor.capture());
        FavoriteTrip savedTrip = favoriteTripCaptor.getValue();

        assertEquals(expectedPosition, savedTrip.getPosition());
        assertEquals(TRIP_ID, savedTrip.getTripId());
    }
}