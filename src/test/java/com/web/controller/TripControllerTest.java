package com.web.controller;

import com.web.model.Trip;
import com.web.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TripControllerTest {

    @Mock
    private TripService tripService;

    @InjectMocks
    private TripController tripController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFilterTrips() {
        // Arrange
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 2, 1, 0, 0);
        Double minWindSpeed = 0.0;
        Double maxWindSpeed = 9999.0;
        String direction = "asc";
        String sortBy = "id";
        Integer page = 0;
        Integer pageSize = 20;

        Page<Trip> expectedTrips = Page.empty();
        ResponseEntity<Page<Trip>> expectedResponse = ResponseEntity.ok(expectedTrips);

        when(tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed,
                direction, sortBy, page, pageSize))
                .thenReturn(expectedResponse);

        // Act
        ResponseEntity<Page<Trip>> actualResponse = tripController.filterTrips(
                startDateTime, endDateTime, minWindSpeed, maxWindSpeed,
                direction, sortBy, page, pageSize);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(tripService).filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed,
                direction, sortBy, page, pageSize);
    }

    @Test
    void testDownload() {
        // Arrange
        Integer sheetLimit = 2;
        Resource mockResource = mock(Resource.class);
        ResponseEntity<Resource> expectedResponse = ResponseEntity.ok(mockResource);

        when(tripService.download(sheetLimit)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<Resource> actualResponse = tripController.download(sheetLimit);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(tripService).download(sheetLimit);
    }

    @Test
    void testFilterTripsEmptyResult() {
        // Arrange
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 2, 1, 0, 0);
        Double minWindSpeed = 0.0;
        Double maxWindSpeed = 9999.0;
        String direction = "asc";
        String sortBy = "id";
        Integer page = 0;
        Integer pageSize = 20;

        Page<Trip> emptyList = Page.empty();
        ResponseEntity<Page<Trip>> expectedResponse = ResponseEntity.ok(emptyList);

        when(tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed,
                direction, sortBy, page, pageSize))
                .thenReturn(expectedResponse);

        // Act
        ResponseEntity<Page<Trip>> actualResponse = tripController.filterTrips(
                startDateTime, endDateTime, minWindSpeed, maxWindSpeed,
                direction, sortBy, page, pageSize);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        assertEquals(0, actualResponse.getBody().getSize());
        verify(tripService).filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed,
                direction, sortBy, page, pageSize);
    }
}