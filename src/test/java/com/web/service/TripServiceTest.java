package com.web.service;

import com.web.model.Trip;
import com.web.repository.TripsRepository;
import com.web.export.TripExcelExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.data.web.PagedResourcesAssembler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;

class TripServiceTest {
    @Mock
    private TripsRepository tripsRepository;

    @Mock
    private PagedResourcesAssembler<Trip> pagedResourcesAssembler;

    @InjectMocks
    private TripService tripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFilter() {
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 2, 1, 0, 0);
        Double minWindSpeed = 0.0;
        Double maxWindSpeed = 1000.0;
        String direction = "asc";
        String sortBy = "id";
        int page = 0;
        int pageSize = 20;

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Trip> tripPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable)).thenReturn(tripPage);

        PagedModel<EntityModel<Trip>> pagedModel = mock(PagedModel.class);
        when(pagedResourcesAssembler.toModel(tripPage)).thenReturn(pagedModel);

        ResponseEntity<PagedModel<EntityModel<Trip>>> response = tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page, pageSize);

        assertEquals(200, response.getStatusCodeValue());
        verify(tripsRepository, times(1)).filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);
        verify(pagedResourcesAssembler, times(1)).toModel(tripPage);
    }

    @Test
    void testInvalidFilterParams() {
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2015, 1, 1, 0, 0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tripService.filter(startDateTime, endDateTime, 0.0, 1000.0, "asc", "id", 0, 20);
        });

        assertTrue(exception.getMessage().contains("endDateTime is before startDateTime"));
    }
}
