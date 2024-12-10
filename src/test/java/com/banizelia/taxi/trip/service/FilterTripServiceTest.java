package com.banizelia.taxi.trip.service;

import com.banizelia.taxi.trip.model.Trip;
import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.trip.repository.TripsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilterTripServiceTest {

    @Mock
    private TripsRepository tripsRepository;

    private TripFilterParams filterParams;

    @Mock
    private Trip trip1, trip2;

    @InjectMocks
    private FilterTripService filterTripService;

    private LocalDateTime pickupDateTimeFrom;
    private LocalDateTime pickupDateTimeTo;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pickupDateTimeFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
        pickupDateTimeTo = LocalDateTime.of(2024, 1, 31, 23, 59);

        filterParams = new TripFilterParams();
        filterParams.setPickupDateTimeFrom(pickupDateTimeFrom);
        filterParams.setPickupDateTimeTo(pickupDateTimeTo);
        filterParams.setMinWindSpeed(0.0);
        filterParams.setMaxWindSpeed(10.0);
        filterParams.setIsFavorite(null);
    }

    @Test
    void execute_WithValidParams_ShouldReturnFilteredTrips() {
        pageable = PageRequest.of(0, 20, Sort.by("id").ascending());
        List<Trip> tripList = Arrays.asList(trip1, trip2);
        Page<Trip> tripPage = new PageImpl<>(tripList, pageable, tripList.size());

        when(tripsRepository.filter(
                null,
                pickupDateTimeFrom,
                pickupDateTimeTo,
                0.0,
                10.0,
                pageable
        )).thenReturn(tripPage);

        Page<TripDto> result = filterTripService.execute(filterParams, pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        verify(tripsRepository).filter(
                null,
                pickupDateTimeFrom,
                pickupDateTimeTo,
                0.0,
                10.0,
                pageable
        );
    }

    @Test
    void execute_WithEmptyResult_ShouldReturnEmptyPage() {
        pageable = PageRequest.of(0, 20, Sort.by("id").ascending());
        Page<Trip> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(tripsRepository.filter(
                null,
                pickupDateTimeFrom,
                pickupDateTimeTo,
                0.0,
                10.0,
                pageable
        )).thenReturn(emptyPage);

        Page<TripDto> result = filterTripService.execute(filterParams, pageable);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(tripsRepository).filter(
                null,
                pickupDateTimeFrom,
                pickupDateTimeTo,
                0.0,
                10.0,
                pageable
        );
    }

    @Test
    void execute_WithPaginationAndSorting_ShouldReturnCorrectPage() {
        pageable = PageRequest.of(2, 15, Sort.by("distance").descending());
        Page<Trip> tripPage = Page.empty();

        when(tripsRepository.filter(
                null,
                pickupDateTimeFrom,
                pickupDateTimeTo,
                0.0,
                10.0,
                pageable
        )).thenReturn(tripPage);

        // Act
        Page<TripDto> result = filterTripService.execute(filterParams, pageable);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());

        verify(tripsRepository).filter(
                null,
                pickupDateTimeFrom,
                pickupDateTimeTo,
                0.0,
                10.0,
                pageable);
    }

    @Test
    void execute_ShouldPreservePageMetadata() {
        pageable = PageRequest.of(1, 2, Sort.by("id").ascending());
        List<Trip> trips = Arrays.asList(trip1, trip2);
        Page<Trip> tripPage = new PageImpl<>(trips, pageable, 10);

        when(tripsRepository.filter(
                null,
                pickupDateTimeFrom,
                pickupDateTimeTo,
                0.0,
                10.0,
                pageable
        )).thenReturn(tripPage);

        Page<TripDto> result = filterTripService.execute(filterParams, pageable);

        assertEquals(1, result.getNumber());
        assertEquals(2, result.getSize());
        assertEquals(10, result.getTotalElements());
        assertEquals(5, result.getTotalPages());
        assertTrue(result.hasNext());
        assertTrue(result.hasPrevious());

        verify(tripsRepository).filter(
                null,
                pickupDateTimeFrom,
                pickupDateTimeTo,
                0.0,
                10.0,
                pageable
        );
    }

    @Test
    void execute_WithIsFavoriteFilter_ShouldPassCorrectParameter() {
        filterParams.setIsFavorite(true);
        pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        List<Trip> tripList = Arrays.asList(trip1);
        Page<Trip> tripPage = new PageImpl<>(tripList, pageable, tripList.size());

        when(tripsRepository.filter(
                true,
                pickupDateTimeFrom,
                pickupDateTimeTo,
                0.0,
                10.0,
                pageable
        )).thenReturn(tripPage);

        Page<TripDto> result = filterTripService.execute(filterParams, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(tripsRepository).filter(
                true,
                pickupDateTimeFrom,
                pickupDateTimeTo,
                0.0,
                10.0,
                pageable
        );
    }
}
