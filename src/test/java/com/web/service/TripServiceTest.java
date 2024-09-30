package com.web.service;

import com.web.model.Trip;
import com.web.repository.TripsRepository;
import com.web.util.ExcelHelper;
import com.web.util.FieldUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TripServiceTest {

    @Mock
    private TripsRepository tripsRepository;

    @InjectMocks
    private TripService tripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void filter_validInput_shouldReturnTrips() {
        Timestamp startDateTime = Timestamp.valueOf(LocalDateTime.of(2016, 1, 1, 0, 0));
        Timestamp endDateTime = Timestamp.valueOf(LocalDateTime.of(2016, 1, 31, 23, 59));
        double minWindSpeed = 0;
        double maxWindSpeed = 9999;
        String direction = "asc";
        String sortBy = "id";
        int page = 0;

        List<Trip> expectedTrips = Collections.singletonList(new Trip());
        when(tripsRepository.filter(any(), any(), anyDouble(), anyDouble(), any(Pageable.class)))
                .thenReturn(expectedTrips);

        List<Trip> result = tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy, page);

        assertEquals(expectedTrips, result);
        verify(tripsRepository, times(1)).filter(any(), any(), eq(minWindSpeed), eq(maxWindSpeed), any(Pageable.class));
    }

    @Test
    void filter_invalidEndDateTime_shouldThrowException() {
        Timestamp startDateTime = Timestamp.valueOf(LocalDateTime.of(2016, 1, 31, 23, 59));
        Timestamp endDateTime = Timestamp.valueOf(LocalDateTime.of(2016, 1, 1, 0, 0));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                tripService.filter(startDateTime, endDateTime, 0.0, 9999.0, "asc", "id", 0)
        );

        assertEquals("endDateTime is before startDateTime, startDateTime = " + startDateTime + " endDateTime = " + endDateTime, exception.getMessage());
    }

    @Test
    void filter_invalidDirection_shouldThrowException() {
        Timestamp startDateTime = Timestamp.valueOf(LocalDateTime.of(2016, 1, 1, 0, 0));
        Timestamp endDateTime = Timestamp.valueOf(LocalDateTime.of(2016, 1, 31, 23, 59));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                tripService.filter(startDateTime, endDateTime, 0.0, 9999.0, "invalid", "id", 0)
        );

        assertEquals("Invalid sort direction: invalid", exception.getMessage());
    }

    @Test
    void filter_invalidSortBy_shouldThrowException() {
        Timestamp startDateTime = Timestamp.valueOf(LocalDateTime.of(2016, 1, 1, 0, 0));
        Timestamp endDateTime = Timestamp.valueOf(LocalDateTime.of(2016, 1, 31, 23, 59));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                tripService.filter(startDateTime, endDateTime, 0.0, 9999.0, "asc", "invalidField", 0)
        );

        assertEquals("Invalid sort field: invalidField", exception.getMessage());
    }

    @Test
    void filter_invalidPage_shouldThrowException() {
        Timestamp startDateTime = Timestamp.valueOf(LocalDateTime.of(2016, 1, 1, 0, 0));
        Timestamp endDateTime = Timestamp.valueOf(LocalDateTime.of(2016, 1, 31, 23, 59));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                tripService.filter(startDateTime, endDateTime, 0.0, 9999.0, "asc", "id", -1)
        );

        assertEquals("Invalid page field, smaller than zero: -1", exception.getMessage());
    }
}
