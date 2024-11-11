package com.web.controller;

import com.web.exceptions.TripNotFoundException;
import com.web.model.dto.TripDto;
import com.web.service.favorite.FavoriteTripService;
import jakarta.persistence.OptimisticLockException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteTripControllerTest {

    @Mock
    private FavoriteTripService favoriteTripService;

    @InjectMocks
    private FavoriteTripController favoriteTripController;

    private List<TripDto> mockTrips;

    @BeforeEach
    void setUp() {
        mockTrips = Arrays.asList(
                new TripDto(), // Заполните необходимыми данными
                new TripDto()
        );
    }

    @Test
    void getFavouriteTrips_Success() {
        // Arrange
        when(favoriteTripService.getFavouriteTrips()).thenReturn(mockTrips);

        // Act
        ResponseEntity<List<TripDto>> response = favoriteTripController.getFavouriteTrips();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTrips, response.getBody());
        verify(favoriteTripService).getFavouriteTrips();
    }

    @Test
    void getFavouriteTrips_WhenExceptionThrown_ReturnsInternalServerError() {
        // Arrange
        when(favoriteTripService.getFavouriteTrips()).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<List<TripDto>> response = favoriteTripController.getFavouriteTrips();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(favoriteTripService).getFavouriteTrips();
    }

    @Test
    void saveToFavourite_Success() throws BadRequestException {
        // Arrange
        Long tripId = 1L;
        doNothing().when(favoriteTripService).saveToFavourite(tripId);

        // Act
        ResponseEntity<String> response = favoriteTripController.saveToFavourite(tripId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Trip added to favorites", response.getBody());
        verify(favoriteTripService).saveToFavourite(tripId);
    }


    @Test
    void saveToFavourite_WhenOptimisticLockException_ReturnsConflict() throws BadRequestException {
        // Arrange
        Long tripId = 1L;
        doThrow(new OptimisticLockException()).when(favoriteTripService).saveToFavourite(tripId);

        // Act
        ResponseEntity<String> response = favoriteTripController.saveToFavourite(tripId);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict occurred while saving the trip", response.getBody());
        verify(favoriteTripService).saveToFavourite(tripId);
    }

    @Test
    void deleteFromFavourite_Success() throws TripNotFoundException {
        // Arrange
        Long tripId = 1L;
        doNothing().when(favoriteTripService).deleteFromFavourite(tripId);

        // Act
        ResponseEntity<String> response = favoriteTripController.deleteFromFavourite(tripId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Trip deleted successfully", response.getBody());
        verify(favoriteTripService).deleteFromFavourite(tripId);
    }

    @Test
    void deleteFromFavourite_WhenTripNotFound_ReturnsNotFound() throws TripNotFoundException {
        // Arrange
        Long tripId = 1L;
        String errorMessage = "Trip not found";
        doThrow(new TripNotFoundException(errorMessage)).when(favoriteTripService).deleteFromFavourite(tripId);

        // Act
        ResponseEntity<String> response = favoriteTripController.deleteFromFavourite(tripId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(favoriteTripService).deleteFromFavourite(tripId);
    }

    @Test
    void dragAndDrop_Success() throws BadRequestException, TripNotFoundException {
        // Arrange
        Long tripId = 1L;
        Long newPosition = 2L;
        doNothing().when(favoriteTripService).dragAndDrop(tripId, newPosition);

        // Act
        ResponseEntity<String> response = favoriteTripController.dragAndDrop(tripId, newPosition);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Position updated successfully", response.getBody());
        verify(favoriteTripService).dragAndDrop(tripId, newPosition);
    }

    @Test
    void dragAndDrop_WhenTripNotFound_ReturnsNotFound() throws BadRequestException, TripNotFoundException {
        // Arrange
        Long tripId = 1L;
        Long newPosition = 2L;
        String errorMessage = "Trip not found";
        doThrow(new TripNotFoundException(errorMessage))
                .when(favoriteTripService).dragAndDrop(tripId, newPosition);

        // Act
        ResponseEntity<String> response = favoriteTripController.dragAndDrop(tripId, newPosition);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(favoriteTripService).dragAndDrop(tripId, newPosition);
    }

    @Test
    void dragAndDrop_WhenOptimisticLockException_ReturnsConflict() throws BadRequestException, TripNotFoundException {
        // Arrange
        Long tripId = 1L;
        Long newPosition = 2L;
        doThrow(new OptimisticLockException())
                .when(favoriteTripService).dragAndDrop(tripId, newPosition);

        // Act
        ResponseEntity<String> response = favoriteTripController.dragAndDrop(tripId, newPosition);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict occurred while updating position", response.getBody());
        verify(favoriteTripService).dragAndDrop(tripId, newPosition);
    }
}