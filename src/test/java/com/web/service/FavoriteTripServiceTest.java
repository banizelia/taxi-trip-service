package com.web.service;

import com.web.model.FavoriteTrip;
import com.web.repository.FavoriteTripRepository;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FavoriteTripServiceTest {

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @InjectMocks
    private FavoriteTripService favoriteTripService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveToFavourite_Success() {
        Long tripId = 1L;
        when(favoriteTripRepository.existsById(tripId)).thenReturn(false);
        when(favoriteTripRepository.findMaxPosition()).thenReturn(1L);

        ResponseEntity<String> response = favoriteTripService.saveToFavourite(tripId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Trip added to favorites", response.getBody());
        verify(favoriteTripRepository, times(1)).save(any(FavoriteTrip.class));
    }

    @Test
    public void testSaveToFavourite_AlreadyExists() {
        Long tripId = 1L;
        when(favoriteTripRepository.existsById(tripId)).thenReturn(true);

        ResponseEntity<String> response = favoriteTripService.saveToFavourite(tripId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Trip already in the table", response.getBody());
        verify(favoriteTripRepository, never()).save(any(FavoriteTrip.class));
    }

    @Test
    public void testSaveToFavourite_OptimisticLockException() {
        Long tripId = 1L;
        when(favoriteTripRepository.existsById(tripId)).thenReturn(false);
        when(favoriteTripRepository.findMaxPosition()).thenReturn(1L);
        doThrow(new OptimisticLockException()).when(favoriteTripRepository).save(any(FavoriteTrip.class));

        ResponseEntity<String> response = favoriteTripService.saveToFavourite(tripId);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict occurred while saving the trip", response.getBody());
    }

    @Test
    public void testDeleteFromFavourite_Success() {
        Long tripId = 1L;
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        when(favoriteTripRepository.findById(tripId)).thenReturn(Optional.of(favoriteTrip));

        ResponseEntity<String> response = favoriteTripService.deleteFromFavourite(tripId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Trip deleted successfully", response.getBody());
        verify(favoriteTripRepository, times(1)).delete(favoriteTrip);
    }

    @Test
    public void testDeleteFromFavourite_NotFound() {
        Long tripId = 1L;
        when(favoriteTripRepository.findById(tripId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = favoriteTripService.deleteFromFavourite(tripId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Trip not found", response.getBody());
        verify(favoriteTripRepository, never()).delete(any(FavoriteTrip.class));
    }

    @Test
    public void testDragAndDrop_Success() {
        Long tripId = 1L;
        Long newPosition = 2L;
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setPosition(1L);
        when(favoriteTripRepository.getReferenceById(tripId)).thenReturn(favoriteTrip);
        when(favoriteTripRepository.findMaxPosition()).thenReturn(3L);

        ResponseEntity<String> response = favoriteTripService.dragAndDrop(tripId, newPosition);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Position updated successfully", response.getBody());
        verify(favoriteTripRepository, times(1)).save(favoriteTrip);
    }

    @Test
    public void testDragAndDrop_OutOfBounds() {
        Long tripId = 1L;
        Long newPosition = 5L;
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setPosition(1L);
        when(favoriteTripRepository.getReferenceById(tripId)).thenReturn(favoriteTrip);
        when(favoriteTripRepository.findMaxPosition()).thenReturn(3L);

        ResponseEntity<String> response = favoriteTripService.dragAndDrop(tripId, newPosition);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("New position is out of bounds.", response.getBody());
        verify(favoriteTripRepository, never()).save(any(FavoriteTrip.class));
    }
}
