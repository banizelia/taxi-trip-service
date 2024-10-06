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

import static org.junit.jupiter.api.Assertions.*;
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
    void saveToFavourite_tripIdLessThanOne_returnsBadRequest() {
        ResponseEntity<String> response = favoriteTripService.saveToFavourite(0L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ID cant be smaller than 1", response.getBody());
    }

    @Test
    void saveToFavourite_tripAlreadyInFavorites_returnsBadRequest() {
        when(favoriteTripRepository.findByTripId(1L)).thenReturn(Optional.of(new FavoriteTrip()));
        ResponseEntity<String> response = favoriteTripService.saveToFavourite(1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Trip already in the table", response.getBody());
    }

    @Test
    void saveToFavourite_tripDoesNotExist_returnsBadRequest() {
        when(favoriteTripRepository.findByTripId(1L)).thenReturn(Optional.empty());
        when(tripsRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<String> response = favoriteTripService.saveToFavourite(1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Such trip doesn't exist", response.getBody());
    }

    @Test
    void saveToFavourite_successfulSave_returnsCreated() {
        when(favoriteTripRepository.findByTripId(1L)).thenReturn(Optional.empty());
        when(tripsRepository.existsById(1L)).thenReturn(true);
        when(favoriteTripRepository.findMaxPosition()).thenReturn(1L);

        ResponseEntity<String> response = favoriteTripService.saveToFavourite(1L);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Trip added to favorites", response.getBody());
        verify(favoriteTripRepository, times(1)).save(any(FavoriteTrip.class));
    }

    @Test
    void saveToFavourite_optimisticLockException_returnsConflict() {
        when(favoriteTripRepository.findByTripId(1L)).thenThrow(OptimisticLockException.class);

        ResponseEntity<String> response = favoriteTripService.saveToFavourite(1L);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict occurred while saving the trip", response.getBody());
    }

    @Test
    void deleteFromFavourite_tripNotFound_returnsNotFound() {
        when(favoriteTripRepository.findByTripId(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = favoriteTripService.deleteFromFavourite(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trip not found", response.getBody());
    }

    @Test
    void deleteFromFavourite_successfulDelete_returnsOk() {
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setPosition(1L);
        when(favoriteTripRepository.findByTripId(1L)).thenReturn(Optional.of(favoriteTrip));

        ResponseEntity<String> response = favoriteTripService.deleteFromFavourite(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Trip deleted successfully", response.getBody());
        verify(favoriteTripRepository, times(1)).delete(favoriteTrip);
        verify(favoriteTripRepository, times(1)).decrementPositionsAfter(1L);
    }

    @Test
    void getFavouriteTrips_returnsList() {
        List<Trip> trips = List.of(new Trip(), new Trip());
        when(favoriteTripRepository.getFavouriteTrips()).thenReturn(trips);

        List<Trip> result = favoriteTripService.getFavouriteTrips();
        assertEquals(trips.size(), result.size());
    }

    @Test
    void dragAndDrop_tripNotFound_returnsNotFound() {
        when(favoriteTripRepository.findByTripId(1L)).thenReturn(Optional.empty());

        ResponseEntity<String> response = favoriteTripService.dragAndDrop(1L, 2L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trip not found", response.getBody());
    }

    @Test
    void dragAndDrop_positionOutOfBounds_returnsBadRequest() {
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setPosition(3L);
        when(favoriteTripRepository.findByTripId(1L)).thenReturn(Optional.of(favoriteTrip));
        when(favoriteTripRepository.findMaxPosition()).thenReturn(4L);

        ResponseEntity<String> response = favoriteTripService.dragAndDrop(1L, 5L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("New position is out of bounds.", response.getBody());
    }

    @Test
    void dragAndDrop_successfulUpdate_returnsOk() {
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setPosition(1L);
        when(favoriteTripRepository.findByTripId(1L)).thenReturn(Optional.of(favoriteTrip));
        when(favoriteTripRepository.findMaxPosition()).thenReturn(3L);

        ResponseEntity<String> response = favoriteTripService.dragAndDrop(1L, 2L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Position updated successfully", response.getBody());
        verify(favoriteTripRepository, times(1)).save(favoriteTrip);
    }
}
