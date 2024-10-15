package com.web.controller;

import com.web.model.Trip;
import com.web.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 2, 1, 0, 0);
        Double minWindSpeed = 0.0;
        Double maxWindSpeed = 9999.0;
        String direction = "asc";
        String sortBy = "id";
        int page = 0;
        int pageSize = 20;

        PagedModel<EntityModel<Trip>> pagedModel = mock(PagedModel.class);
        when(tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize))
                .thenReturn(ResponseEntity.ok(pagedModel));

        ResponseEntity<PagedModel<EntityModel<Trip>>> response = tripController.filterTrips(
                startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize);

        assertEquals(200, response.getStatusCodeValue());
        verify(tripService, times(1)).filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize);
    }

    @Test
    void testDownload() {
        Integer sheetLimit = 2;
        Resource resource = mock(Resource.class);

        when(tripService.download(sheetLimit)).thenReturn(ResponseEntity.ok(resource));

        ResponseEntity<Resource> response = tripController.download(sheetLimit);

        assertEquals(200, response.getStatusCodeValue());
        verify(tripService, times(1)).download(sheetLimit);
    }
}
