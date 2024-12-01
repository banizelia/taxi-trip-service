//package com.web.trip.service.filter;
//
//import com.web.trip.mapper.TripMapper;
//import com.web.trip.model.Trip;
//import com.web.trip.model.TripDto;
//import com.web.trip.model.TripFilterParams;
//import com.web.trip.repository.TripsRepository;
//import com.web.trip.service.FilterTripService;
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
//import static org.mockito.ArgumentMatchers.*;
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
//    private Pageable pageable;
//
//    @BeforeEach
//    void setUp() {
//        startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
//        endDateTime = LocalDateTime.of(2024, 1, 31, 23, 59);
//
//        // Настройка базовых параметров фильтрации
//        when(filterParams.getStartDateTime()).thenReturn(startDateTime);
//        when(filterParams.getEndDateTime()).thenReturn(endDateTime);
//        when(filterParams.getMinWindSpeed()).thenReturn(0.0);
//        when(filterParams.getMaxWindSpeed()).thenReturn(10.0);
//        when(filterParams.getIsFavorite()).thenReturn(null); // Без фильтрации по избранному
//    }
//
//    @Test
//    void execute_WithValidParams_ShouldReturnFilteredTrips() {
//        // Arrange
//        pageable = PageRequest.of(0, 20, Sort.by("id").ascending());
//        List<Trip> tripList = Arrays.asList(trip1, trip2);
//        Page<Trip> tripPage = new PageImpl<>(tripList, pageable, tripList.size());
//
//        when(tripsRepository.filter(
//                eq(filterParams.getIsFavorite()),
//                eq(startDateTime),
//                eq(endDateTime),
//                eq(filterParams.getMinWindSpeed()),
//                eq(filterParams.getMaxWindSpeed()),
//                eq(pageable)
//        )).thenReturn(tripPage);
//
//        try (MockedStatic<TripMapper> mockedMapper = mockStatic(TripMapper.class)) {
//            // Настройка поведения TripMapper
//            mockedMapper.when(() -> TripMapper.INSTANCE.tripToTripDto(trip1)).thenReturn(tripDto1);
//            mockedMapper.when(() -> TripMapper.INSTANCE.tripToTripDto(trip2)).thenReturn(tripDto2);
//
//            // Act
//            Page<TripDto> result = filterTripService.execute(filterParams, pageable);
//
//            // Assert
//            assertNotNull(result, "Результат не должен быть null");
//            assertEquals(2, result.getContent().size(), "Должно быть возвращено 2 поездки");
//            assertEquals(tripDto1, result.getContent().get(0), "Первая поездка должна соответствовать tripDto1");
//            assertEquals(tripDto2, result.getContent().get(1), "Вторая поездка должна соответствовать tripDto2");
//
//            // Верификация вызова репозитория
//            verify(tripsRepository).filter(
//                    eq(filterParams.getIsFavorite()),
//                    eq(startDateTime),
//                    eq(endDateTime),
//                    eq(filterParams.getMinWindSpeed()),
//                    eq(filterParams.getMaxWindSpeed()),
//                    eq(pageable)
//            );
//
//            // Верификация вызова TripMapper
//            mockedMapper.verify(() -> TripMapper.INSTANCE.tripToTripDto(trip1), times(1));
//            mockedMapper.verify(() -> TripMapper.INSTANCE.tripToTripDto(trip2), times(1));
//            mockedMapper.verifyNoMoreInteractions();
//        }
//    }
//
//    @Test
//    void execute_WithEmptyResult_ShouldReturnEmptyPage() {
//        // Arrange
//        pageable = PageRequest.of(0, 20, Sort.by("id").ascending());
//        Page<Trip> emptyPage = new PageImpl<>(List.of(), pageable, 0);
//
//        when(tripsRepository.filter(
//                eq(filterParams.getIsFavorite()),
//                eq(startDateTime),
//                eq(endDateTime),
//                eq(filterParams.getMinWindSpeed()),
//                eq(filterParams.getMaxWindSpeed()),
//                eq(pageable)
//        )).thenReturn(emptyPage);
//
//        try (MockedStatic<TripMapper> mockedMapper = mockStatic(TripMapper.class)) {
//            // Act
//            Page<TripDto> result = filterTripService.execute(filterParams, pageable);
//
//            // Assert
//            assertNotNull(result, "Результат не должен быть null");
//            assertTrue(result.getContent().isEmpty(), "Содержимое страницы должно быть пустым");
//            assertEquals(0, result.getTotalElements(), "Общее количество элементов должно быть 0");
//
//            // Верификация вызова репозитория
//            verify(tripsRepository).filter(
//                    eq(filterParams.getIsFavorite()),
//                    eq(startDateTime),
//                    eq(endDateTime),
//                    eq(filterParams.getMinWindSpeed()),
//                    eq(filterParams.getMaxWindSpeed()),
//                    eq(pageable)
//            );
//
//            // Верификация отсутствия вызовов TripMapper
//            mockedMapper.verifyNoInteractions();
//        }
//    }
//
//    @Test
//    void execute_VerifyPaginationAndSorting() {
//        // Arrange
//        when(filterParams.getgetPage()).thenReturn(2);
//        when(filterParams.getSize()).thenReturn(15);
//        when(filterParams.getSort()).thenReturn("distance");
//        when(filterParams.getDirection()).thenReturn("desc");
//
//        pageable = PageRequest.of(2, 15, Sort.by("distance").descending());
//
//        Page<Trip> tripPage = Page.empty();
//
//        when(tripsRepository.filter(
//                eq(filterParams.getIsFavorite()),
//                eq(startDateTime),
//                eq(endDateTime),
//                eq(filterParams.getMinWindSpeed()),
//                eq(filterParams.getMaxWindSpeed()),
//                eq(pageable)
//        )).thenReturn(tripPage);
//
//        try (MockedStatic<TripMapper> mockedMapper = mockStatic(TripMapper.class)) {
//            // Act
//            Page<TripDto> result = filterTripService.execute(filterParams, pageable);
//
//            // Assert
//            assertNotNull(result, "Результат не должен быть null");
//            assertTrue(result.getContent().isEmpty(), "Содержимое страницы должно быть пустым");
//
//            // Верификация вызова репозитория с правильным Pageable
//            verify(tripsRepository).filter(
//                    eq(filterParams.getIsFavorite()),
//                    eq(startDateTime),
//                    eq(endDateTime),
//                    eq(filterParams.getMinWindSpeed()),
//                    eq(filterParams.getMaxWindSpeed()),
//                    eq(pageable)
//            );
//
//            // Верификация отсутствия вызовов TripMapper
//            mockedMapper.verifyNoInteractions();
//        }
//    }
//
//    @Test
//    void execute_ShouldPreservePageMetadata() {
//        // Arrange
//        pageable = PageRequest.of(1, 2, Sort.by("id").ascending());
//        List<Trip> trips = Arrays.asList(trip1, trip2);
//        Page<Trip> tripPage = new PageImpl<>(trips, pageable, 10);
//
//        when(tripsRepository.filter(
//                eq(filterParams.getIsFavorite()),
//                eq(startDateTime),
//                eq(endDateTime),
//                eq(filterParams.getMinWindSpeed()),
//                eq(filterParams.getMaxWindSpeed()),
//                eq(pageable)
//        )).thenReturn(tripPage);
//
//        try (MockedStatic<TripMapper> mockedMapper = mockStatic(TripMapper.class)) {
//            // Настройка поведения TripMapper
//            mockedMapper.when(() -> TripMapper.INSTANCE.tripToTripDto(trip1)).thenReturn(tripDto1);
//            mockedMapper.when(() -> TripMapper.INSTANCE.tripToTripDto(trip2)).thenReturn(tripDto2);
//
//            // Act
//            Page<TripDto> result = filterTripService.execute(filterParams, pageable);
//
//            // Assert
//            assertEquals(1, result.getNumber(), "Номер страницы должен быть 1");
//            assertEquals(2, result.getSize(), "Размер страницы должен быть 2");
//            assertEquals(10, result.getTotalElements(), "Общее количество элементов должно быть 10");
//            assertEquals(5, result.getTotalPages(), "Общее количество страниц должно быть 5");
//            assertTrue(result.hasNext(), "Должна быть следующая страница");
//            assertTrue(result.hasPrevious(), "Должна быть предыдущая страница");
//
//            // Верификация вызова репозитория
//            verify(tripsRepository).filter(
//                    eq(filterParams.getIsFavorite()),
//                    eq(startDateTime),
//                    eq(endDateTime),
//                    eq(filterParams.getMinWindSpeed()),
//                    eq(filterParams.getMaxWindSpeed()),
//                    eq(pageable)
//            );
//
//            // Верификация вызова TripMapper
//            mockedMapper.verify(() -> TripMapper.INSTANCE.tripToTripDto(trip1), times(1));
//            mockedMapper.verify(() -> TripMapper.INSTANCE.tripToTripDto(trip2), times(1));
//            mockedMapper.verifyNoMoreInteractions();
//        }
//    }
//}
