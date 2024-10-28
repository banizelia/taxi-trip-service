package com.web.service.favorite.managment;

import com.web.exceptions.TripNotFoundException;
import com.web.model.FavoriteTrip;
import com.web.repository.FavoriteTripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteFavoriteTripServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @InjectMocks
    private DeleteFavoriteTripService deleteFavoriteTripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute_tripExists() {
        // Arrange
        Long tripId = 1L;
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.of(favoriteTrip));

        // Act
        deleteFavoriteTripService.execute(tripId);

        // Assert
        verify(favoriteTripRepository).delete(favoriteTrip);
    }

    @Test
    void testExecute_tripNotFound() {
        // Arrange
        Long tripId = 1L;
        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TripNotFoundException.class, () -> deleteFavoriteTripService.execute(tripId));
        verify(favoriteTripRepository, never()).delete(any());
    }
}
