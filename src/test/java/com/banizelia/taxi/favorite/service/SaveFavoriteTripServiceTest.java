package com.banizelia.taxi.favorite.service;

import com.banizelia.taxi.favorite.model.FavoriteTrip;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import com.banizelia.taxi.favorite.service.position.PositionCalculator;
import com.banizelia.taxi.trip.repository.TripsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveFavoriteTripServiceTest {

    private static final Long TRIP_ID = 1L;
    private static final Long MAX_POSITION = 100L;
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

    @BeforeEach
    void setUp() {
        when(favoriteTripRepository.findByTripId(TRIP_ID)).thenReturn(Optional.empty());
        when(tripsRepository.existsById(TRIP_ID)).thenReturn(true);
        when(positionCalculator.lastPosition()).thenReturn(MAX_POSITION);
    }

    @Test
    void execute_ShouldSaveFavoriteTripFavoriteTrip_WhenValidInput() {
        service.execute(TRIP_ID);

        verify(favoriteTripRepository).save(favoriteTripCaptor.capture());

        FavoriteTrip savedTrip = favoriteTripCaptor.getValue();
        assertEquals(TRIP_ID, savedTrip.getTripId());
        assertEquals(MAX_POSITION, savedTrip.getPosition());

        verify(favoriteTripRepository).findByTripId(TRIP_ID);
        verify(tripsRepository).existsById(TRIP_ID);
        verify(positionCalculator).lastPosition();
    }

    @Test
    void execute_ShouldSetCorrectPosition_WhenSaving() {
        Long expectedPosition = 42L;
        when(positionCalculator.lastPosition()).thenReturn(expectedPosition);

        service.execute(TRIP_ID);

        verify(favoriteTripRepository).save(favoriteTripCaptor.capture());
        FavoriteTrip savedTrip = favoriteTripCaptor.getValue();

        assertEquals(expectedPosition, savedTrip.getPosition());
        assertEquals(TRIP_ID, savedTrip.getTripId());
    }
}