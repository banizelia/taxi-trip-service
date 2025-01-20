package com.banizelia.taxi.favorite.service;

import com.banizelia.taxi.error.trip.TripNotFoundException;
import com.banizelia.taxi.favorite.model.FavoriteTrip;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteFavoriteTripServiceTest {

    private static final Long TRIP_ID = 1L;
    @Mock
    private FavoriteTripRepository favoriteTripRepository;
    @InjectMocks
    private DeleteFavoriteTripService service;
    private FavoriteTrip favoriteTrip;

    @BeforeEach
    void setUp() {
        favoriteTrip = new FavoriteTrip();
        favoriteTrip.setId(1L);
        favoriteTrip.setTripId(TRIP_ID);
        favoriteTrip.setPosition(1L);
    }

    @Test
    void execute_ShouldDeleteFavoriteTrip_WhenTripExists() {
        when(favoriteTripRepository.findByTripId(TRIP_ID)).thenReturn(Optional.of(favoriteTrip));

        service.execute(TRIP_ID);

        verify(favoriteTripRepository).findByTripId(TRIP_ID);
        verify(favoriteTripRepository).delete(favoriteTrip);
    }

    @Test
    void execute_ShouldThrowTripNotFoundException_WhenTripDoesNotExist() {
        when(favoriteTripRepository.findByTripId(TRIP_ID)).thenReturn(Optional.empty());

        assertThrows(TripNotFoundException.class, () -> service.execute(TRIP_ID));
        verify(favoriteTripRepository, never()).delete(any());
    }
}