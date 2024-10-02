package com.web.controller;

import com.web.model.Trip;
import com.web.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    void testFilter() {
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0 ,0);
        LocalDateTime endDateTime =  LocalDateTime.of(2016, 1, 31, 23, 59 ,59);
        Double minWindSpeed = 0.0;
        Double maxWindSpeed = 9999.0;
        String direction = "asc";
        String sortBy = "id";
        int page = 0;
        int pageSize = 500;

        List<Trip> expectedTrips = new ArrayList<>();
        when(tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize))
                .thenReturn(expectedTrips);

        List<Trip> result = tripController.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize);

        assertEquals(expectedTrips, result);
        verify(tripService, times(1)).filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize);
    }

    @Test
    void testDownload() {
        Resource mockResource = mock(Resource.class);
        ResponseEntity<Resource> expectedResponse = ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=trips.xlsx")
                .body(mockResource);

        when(tripService.download()).thenReturn(expectedResponse);

        ResponseEntity<Resource> result = tripController.download();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse.getBody(), result.getBody());
        verify(tripService, times(1)).download();
    }
}
