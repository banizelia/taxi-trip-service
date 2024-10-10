package com.web.service;

import com.web.model.FavoriteTrip;
import com.web.model.Trip;
import com.web.repository.FavoriteTripRepository;
import com.web.repository.TripsRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FavoriteTripServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @Mock
    private TripsRepository tripsRepository;

    @InjectMocks
    private FavoriteTripService favoriteTripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveToFavourite_TripIdLessThanOne_ReturnsBadRequest() {
        ResponseEntity<String> response = favoriteTripService.saveToFavourite(0L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ID can't be smaller than 1", response.getBody());
    }

    @Test
    void saveToFavourite_TripAlreadyExists_ReturnsBadRequest() {
        when(favoriteTripRepository.findByTripId(anyLong())).thenReturn(Optional.of(new FavoriteTrip()));

        ResponseEntity<String> response = favoriteTripService.saveToFavourite(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Trip already in the table", response.getBody());
    }

    @Test
    void saveToFavourite_TripDoesNotExist_ReturnsBadRequest() {
        when(tripsRepository.existsById(anyLong())).thenReturn(false);

        ResponseEntity<String> response = favoriteTripService.saveToFavourite(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Such trip doesn't exist", response.getBody());
    }

    @Test
    void saveToFavourite_OptimisticLockException_ReturnsConflict() {
        when(tripsRepository.existsById(anyLong())).thenReturn(true);
        when(favoriteTripRepository.findByTripId(anyLong())).thenReturn(Optional.empty());
        when(favoriteTripRepository.findMaxPosition()).thenThrow(OptimisticLockException.class);

        ResponseEntity<String> response = favoriteTripService.saveToFavourite(1L);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict occurred while saving the trip", response.getBody());
    }

    @Test
    void saveToFavourite_Success_ReturnsCreated() {
        when(tripsRepository.existsById(anyLong())).thenReturn(true);
        when(favoriteTripRepository.findByTripId(anyLong())).thenReturn(Optional.empty());
        when(favoriteTripRepository.findMaxPosition()).thenReturn(1L);

        ResponseEntity<String> response = favoriteTripService.saveToFavourite(1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Trip added to favorites", response.getBody());
    }

    @Test
    void deleteFromFavourite_TripNotFound_ReturnsNotFound() {
        when(favoriteTripRepository.findByTripId(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<String> response = favoriteTripService.deleteFromFavourite(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trip not found", response.getBody());
    }

    @Test
    void deleteFromFavourite_Success_ReturnsOk() {
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setPosition(1L);

        when(favoriteTripRepository.findByTripId(anyLong())).thenReturn(Optional.of(favoriteTrip));

        ResponseEntity<String> response = favoriteTripService.deleteFromFavourite(1L);

        verify(favoriteTripRepository, times(1)).delete(favoriteTrip);
        verify(favoriteTripRepository, times(1)).decrementPositionsAfter(favoriteTrip.getPosition());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Trip deleted successfully", response.getBody());
    }

    @Test
    void getFavouriteTrips_ReturnsListOfTrips() {
        List<Trip> trips = List.of(new Trip(), new Trip());
        when(favoriteTripRepository.getFavouriteTrips()).thenReturn(trips);

        ResponseEntity<List<Trip>> response = favoriteTripService.getFavouriteTrips();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(trips, response.getBody());
    }

    @Test
    void dragAndDrop_TripNotFound_ReturnsNotFound() {
        when(favoriteTripRepository.findByTripId(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<String> response = favoriteTripService.dragAndDrop(1L, 2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trip not found", response.getBody());
    }

    @Test
    void dragAndDrop_PositionIsTheSame_ReturnsBadRequest() {
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setPosition(1L);

        when(favoriteTripRepository.findByTripId(anyLong())).thenReturn(Optional.of(favoriteTrip));

        ResponseEntity<String> response = favoriteTripService.dragAndDrop(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Position remains unchanged", response.getBody());
    }

    @Test
    void dragAndDrop_NewPositionOutOfBounds_ReturnsBadRequest() {
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setPosition(1L);

        when(favoriteTripRepository.findByTripId(anyLong())).thenReturn(Optional.of(favoriteTrip));
        when(favoriteTripRepository.findMaxPosition()).thenReturn(2L);

        ResponseEntity<String> response = favoriteTripService.dragAndDrop(1L, 3L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("New position is out of bounds.", response.getBody());
    }

    @Test
    void dragAndDrop_Success_ReturnsOk() {
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setPosition(1L);

        when(favoriteTripRepository.findByTripId(anyLong())).thenReturn(Optional.of(favoriteTrip));
        when(favoriteTripRepository.findMaxPosition()).thenReturn(3L);

        ResponseEntity<String> response = favoriteTripService.dragAndDrop(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Position updated successfully", response.getBody());
    }
}
