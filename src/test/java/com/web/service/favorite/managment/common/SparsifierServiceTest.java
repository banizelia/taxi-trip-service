package com.web.service.favorite.managment.common;

import com.web.model.FavoriteTrip;
import com.web.model.enums.FavoriteTripEnum;
import com.web.repository.FavoriteTripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SparsifierServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @InjectMocks
    private SparsifierService sparsifierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSparsify_withTrips() {
        // Arrange
        FavoriteTrip trip1 = new FavoriteTrip();
        FavoriteTrip trip2 = new FavoriteTrip();
        trip1.setPosition(100L);
        trip2.setPosition(200L);
        List<FavoriteTrip> trips = Arrays.asList(trip1, trip2);

        when(favoriteTripRepository.getFavouriteTripsByPositionAsc()).thenReturn(trips);

        // Act
        sparsifierService.sparsify();

        // Assert
        assertEquals(FavoriteTripEnum.INITIAL_POSITION.getValue(), trip1.getPosition());
        assertEquals(FavoriteTripEnum.INITIAL_POSITION.getValue() + FavoriteTripEnum.POSITION_GAP.getValue(), trip2.getPosition());
        verify(favoriteTripRepository).saveAll(trips);
    }

    @Test
    void testSparsify_noTrips() {
        // Arrange
        when(favoriteTripRepository.getFavouriteTripsByPositionAsc()).thenReturn(List.of());

        // Act
        sparsifierService.sparsify();

        // Assert
        verify(favoriteTripRepository, never()).saveAll(anyList());
    }
}
