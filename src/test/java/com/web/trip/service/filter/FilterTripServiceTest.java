//package com.web.trip.service.filter;
//
//import com.web.trip.mapper.TripMapper;
//import com.web.trip.model.Trip;
//import com.web.trip.model.TripDto;
//import com.web.trip.repository.TripsRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.*;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class FilterTripServiceTest {
//
//    @Mock
//    private TripsRepository tripsRepository;
//
//    @Mock
//    private TripFilterParams filterParams;
//
//    @Mock
//    private Trip trip1, trip2;
//
//    @Mock
//    private TripDto tripDto1, tripDto2;
//
//    @InjectMocks
//    private FilterTripService filterTripService;
//
//    private LocalDateTime startDateTime;
//    private LocalDateTime endDateTime;
//
//    @BeforeEach
//    void setUp() {
//        startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
//        endDateTime = LocalDateTime.of(2024, 1, 31, 23, 59);
//
//        // Setup basic filter params
//        when(filterParams.startDateTime()).thenReturn(startDateTime);
//        when(filterParams.endDateTime()).thenReturn(endDateTime);
//        when(filterParams.minWindSpeed()).thenReturn(0.0);
//        when(filterParams.maxWindSpeed()).thenReturn(10.0);
//        when(filterParams.page()).thenReturn(0);
//        when(filterParams.size()).thenReturn(20);
//        when(filterParams.sort()).thenReturn("id");
//        when(filterParams.direction()).thenReturn("asc");
//    }
//
//    @Test
//    void execute_WithValidParams_ShouldReturnFilteredTrips() {
//        // Arrange
//        List<Trip> tripList = Arrays.asList(trip1, trip2);
//        Page<Trip> tripPage = new PageImpl<>(tripList);
//
//        when(tripsRepository.filter(
//                eq(startDateTime),
//                eq(endDateTime),
//                eq(0.0),
//                eq(10.0),
//                any(Pageable.class)
//        )).thenReturn(tripPage);
//
//        try (MockedStatic<TripMapper> mockedMapper = mockStatic(TripMapper.class)) {
//            // Setup mapper mock
//            TripMapper mapperInstance = mock(TripMapper.class);
//            when(mapperInstance.tripToTripDto(trip1)).thenReturn(tripDto1);
//            when(mapperInstance.tripToTripDto(trip2)).thenReturn(tripDto2);
//
//            // Act
//            Page<TripDto> result = filterTripService.execute(filterParams);
//
//            // Assert
//            assertNotNull(result);
//            assertEquals(2, result.getContent().size());
//            assertEquals(tripDto1, (TripDto) result.getContent().get(0));
//            assertEquals(tripDto2, result.getContent().get(1));
//
//            // Verify
//            verify(tripsRepository).filter(
//                    eq(startDateTime),
//                    eq(endDateTime),
//                    eq(0.0),
//                    eq(10.0),
//                    any(Pageable.class)
//            );
//        }
//    }
//
//    @Test
//    void execute_WithEmptyResult_ShouldReturnEmptyPage() {
//        // Arrange
//        Page<Trip> emptyPage = new PageImpl<>(List.of());
//
//        when(tripsRepository.filter(
//                any(LocalDateTime.class),
//                any(LocalDateTime.class),
//                any(Double.class),
//                any(Double.class),
//                any(Pageable.class)
//        )).thenReturn(emptyPage);
//
//        try (MockedStatic<TripMapper> mockedMapper = mockStatic(TripMapper.class)) {
//
//
//            // Act
//            Page<TripDto> result = filterTripService.execute(filterParams);
//
//            // Assert
//            assertNotNull(result);
//            assertTrue(result.getContent().isEmpty());
//            assertEquals(0, result.getTotalElements());
//        }
//    }
//
//    @Test
//    void execute_VerifyPaginationAndSorting() {
//        // Arrange
//        when(filterParams.page()).thenReturn(2);
//        when(filterParams.size()).thenReturn(15);
//        when(filterParams.sort()).thenReturn("distance");
//        when(filterParams.direction()).thenReturn("desc");
//
//        when(tripsRepository.filter(
//                any(LocalDateTime.class),
//                any(LocalDateTime.class),
//                any(Double.class),
//                any(Double.class),
//                any(Pageable.class)
//        )).thenReturn(Page.empty());
//
//        try (MockedStatic<TripMapper> mockedMapper = mockStatic(TripMapper.class)) {
//            // Act
//            filterTripService.execute(filterParams);
//
//            // Assert
//            verify(tripsRepository).filter(
//                    any(LocalDateTime.class),
//                    any(LocalDateTime.class),
//                    any(Double.class),
//                    any(Double.class),
//                    argThat(pageable ->
//                            pageable.getPageNumber() == 2 &&
//                                    pageable.getPageSize() == 15 &&
//                                    pageable.getSort().getOrderFor("distance") != null &&
//                                    pageable.getSort().getOrderFor("distance").getDirection() == Sort.Direction.DESC
//                    )
//            );
//        }
//    }
//
//    @Test
//    void execute_WithNullPage_ShouldUseDefaultPagination() {
//        // Arrange
//        when(filterParams.page()).thenReturn(null);
//        when(filterParams.size()).thenReturn(null);
//        when(tripsRepository.filter(
//                any(LocalDateTime.class),
//                any(LocalDateTime.class),
//                any(Double.class),
//                any(Double.class),
//                any(Pageable.class)
//        )).thenReturn(Page.empty());
//
//
//
//            // Act
//            filterTripService.execute(filterParams);
//
//            // Assert
//            verify(tripsRepository).filter(
//                    any(LocalDateTime.class),
//                    any(LocalDateTime.class),
//                    any(Double.class),
//                    any(Double.class),
//                    argThat(pageable ->
//                            pageable.getPageNumber() == 0 &&
//                                    pageable.getPageSize() == 20
//                    )
//            );
//
//    }
//
//    @Test
//    void execute_ShouldPreservePageMetadata() {
//        // Arrange
//        List<Trip> trips = Arrays.asList(trip1, trip2);
//        Page<Trip> tripPage = new PageImpl<>(trips, PageRequest.of(1, 2), 10);
//
//        when(tripsRepository.filter(
//                any(LocalDateTime.class),
//                any(LocalDateTime.class),
//                any(Double.class),
//                any(Double.class),
//                any(Pageable.class)
//        )).thenReturn(tripPage);
//
//        try (MockedStatic<TripMapper> mockedMapper = mockStatic(TripMapper.class)) {
//            // Setup mapper mock
//            TripMapper mapperInstance = mock(TripMapper.class);
//            when(mapperInstance.tripToTripDto(any())).thenReturn(tripDto1);
//
//            // Act
//            Page<TripDto> result = filterTripService.execute(filterParams);
//
//            // Assert
//            assertEquals(1, result.getNumber());
//            assertEquals(2, result.getSize());
//            assertEquals(10, result.getTotalElements());
//            assertEquals(5, result.getTotalPages());
//            assertTrue(result.hasNext());
//            assertTrue(result.hasPrevious());
//        }
//    }
//}