package com.web.favorite.service;

import com.web.common.exception.trip.TripNotFoundException;
import com.web.favorite.model.FavoriteTrip;
import com.web.favorite.repository.FavoriteTripRepository;
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
class DeleteFavoriteTripServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @InjectMocks
    private DeleteFavoriteTripService service;

    private FavoriteTrip favoriteTrip;
    private static final Long TRIP_ID = 1L;

    @BeforeEach
    void setUp() {
        favoriteTrip = new FavoriteTrip();
        favoriteTrip.setId(1L);
        favoriteTrip.setTripId(TRIP_ID);
        favoriteTrip.setPosition(1L);
    }

    @Test
    void execute_ShouldDeleteFavoriteTrip_WhenTripExists() {
        // Arrange
        when(favoriteTripRepository.findByTripId(TRIP_ID)).thenReturn(Optional.of(favoriteTrip));

        // Act
        service.execute(TRIP_ID);

        // Assert
        verify(favoriteTripRepository).findByTripId(TRIP_ID);
        verify(favoriteTripRepository).delete(favoriteTrip);
    }

    @Test
    void execute_ShouldThrowTripNotFoundException_WhenTripDoesNotExist() {
        // Arrange
        when(favoriteTripRepository.findByTripId(TRIP_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TripNotFoundException.class, () -> service.execute(TRIP_ID));
        verify(favoriteTripRepository, never()).delete(any());
    }
}