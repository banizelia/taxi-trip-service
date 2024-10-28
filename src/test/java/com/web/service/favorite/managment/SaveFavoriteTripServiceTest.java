package com.web.service.favorite.managment;

import com.web.exceptions.TripNotFoundException;
import com.web.model.FavoriteTrip;
import com.web.repository.FavoriteTripRepository;
import com.web.repository.TripsRepository;
import com.web.service.favorite.managment.common.SparsifierService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SaveFavoriteTripServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @Mock
    private TripsRepository tripsRepository;

    @Mock
    private SparsifierService sparsifier;

    @InjectMocks
    private SaveFavoriteTripService saveFavoriteTripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute_tripAlreadyInFavorites() {
        // Arrange
        Long tripId = 1L;
        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.of(new FavoriteTrip()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> saveFavoriteTripService.execute(tripId));
        verify(favoriteTripRepository, never()).save(any());
    }

    @Test
    void testExecute_tripNotFoundInTripsRepository() {
        // Arrange
        Long tripId = 1L;
        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.empty());
        when(tripsRepository.existsById(tripId)).thenReturn(false);

        // Act & Assert
        assertThrows(TripNotFoundException.class, () -> saveFavoriteTripService.execute(tripId));
        verify(favoriteTripRepository, never()).save(any());
    }

    @Test
    void testExecute_addFirstFavoriteTrip() throws BadRequestException {
        // Arrange
        Long tripId = 1L;
        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.empty());
        when(tripsRepository.existsById(tripId)).thenReturn(true);
        when(favoriteTripRepository.findMaxPosition()).thenReturn(0L);

        // Act
        saveFavoriteTripService.execute(tripId);

        // Assert
        verify(favoriteTripRepository).save(any(FavoriteTrip.class));
        verify(sparsifier, never()).sparsify();
    }

    @Test
    void testExecute_additionalSparsificationDueToOverflow() {
        // Arrange
        Long tripId = 1L;
        long maxPosition = Long.MAX_VALUE - 1;
        when(favoriteTripRepository.findByTripId(tripId)).thenReturn(Optional.empty());
        when(tripsRepository.existsById(tripId)).thenReturn(true);
        when(favoriteTripRepository.findMaxPosition()).thenReturn(maxPosition);

        doThrow(ArithmeticException.class).when(favoriteTripRepository).save(any(FavoriteTrip.class));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> saveFavoriteTripService.execute(tripId));
        verify(sparsifier, atLeastOnce()).sparsify();
    }
}
