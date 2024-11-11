package com.web.service.favorite.managment;

import com.web.model.Trip;
import com.web.model.dto.TripDto;
import com.web.mapper.TripMapper;
import com.web.repository.FavoriteTripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class GetFavoriteTripServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @InjectMocks
    private GetFavoriteTripService getFavoriteTripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecute_withFavoriteTrips() {
        // Arrange
        Trip trip1 = new Trip();
        trip1.setId(1L);
        Trip trip2 = new Trip();
        trip2.setId(2L);

        TripDto tripDto1 = TripMapper.INSTANCE.tripToTripDto(trip1);
        TripDto tripDto2 = TripMapper.INSTANCE.tripToTripDto(trip2);

        when(favoriteTripRepository.getTripsByPositionAsc()).thenReturn(List.of(trip1, trip2));

        // Act
        List<TripDto> result = getFavoriteTripService.execute();

        // Assert
        assertEquals(List.of(tripDto1, tripDto2), result);
    }

    @Test
    void testExecute_noFavoriteTrips() {
        // Arrange
        when(favoriteTripRepository.getTripsByPositionAsc()).thenReturn(List.of());

        // Act
        List<TripDto> result = getFavoriteTripService.execute();

        // Assert
        assertEquals(List.of(), result);
    }
}
