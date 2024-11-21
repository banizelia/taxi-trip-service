package com.web.favorite.service;

import com.web.common.exception.filter.InvalidSortDirectionException;
import com.web.common.exception.filter.InvalidSortFieldException;
import com.web.favorite.repository.FavoriteTripRepository;
import com.web.trip.model.Trip;
import com.web.trip.model.TripDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetFavoriteTripServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @Mock
    private Trip trip;

    @Mock
    private TripDto tripDto;

    @InjectMocks
    private GetFavoriteTripService service;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setId(1L);
        tripDto = new TripDto();
        tripDto.setId(1L);
    }

    @Test
    void execute_ShouldReturnPageOfTrips_WhenValidParams() {
        // Arrange
        Page<Trip> tripPage = new PageImpl<>(List.of(trip));
        when(favoriteTripRepository.findAllWithPagination(any(Pageable.class))).thenReturn(tripPage);


        // Act
        Page<TripDto> result = service.execute(0, 10, "position", "asc");

            // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(tripDto, result.getContent().get(0));

    }

    @Test
    void execute_ShouldThrowInvalidSortDirectionException_WhenInvalidDirection() {
        // Act & Assert
        assertThrows(InvalidSortDirectionException.class,
                () -> service.execute(0, 10, "position", "invalid"));
        verify(favoriteTripRepository, never()).findAllWithPagination(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalidField", "name", "date"})
    void execute_ShouldThrowInvalidSortFieldException_WhenInvalidSortField(String invalidField) {
        // Act & Assert
        assertThrows(InvalidSortFieldException.class,
                () -> service.execute(0, 10, invalidField, "asc"));
        verify(favoriteTripRepository, never()).findAllWithPagination(any());
    }

    @Test
    void execute_ShouldHandleEmptyResult() {
        // Arrange
        Page<Trip> emptyPage = new PageImpl<>(List.of());
        when(favoriteTripRepository.findAllWithPagination(any(Pageable.class))).thenReturn(emptyPage);

        // Act
        Page<TripDto> result = service.execute(0, 10, "position", "asc");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void execute_ShouldHandleCaseInsensitiveDirection() {
        // Arrange
        Page<Trip> tripPage = new PageImpl<>(List.of(trip));
        when(favoriteTripRepository.findAllWithPagination(any(Pageable.class))).thenReturn(tripPage);

        // Act & Assert
        assertDoesNotThrow(() -> service.execute(0, 10, "position", "ASC"));
        assertDoesNotThrow(() -> service.execute(0, 10, "position", "DESC"));

    }
}