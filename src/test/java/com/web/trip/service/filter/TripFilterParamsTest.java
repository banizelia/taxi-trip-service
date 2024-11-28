//package com.web.trip.service.filter;
//
//import com.web.common.FieldNamesExtractor;
//import com.web.common.exception.filter.InvalidDateRangeException;
//import com.web.common.exception.filter.InvalidSortDirectionException;
//import com.web.common.exception.filter.InvalidSortFieldException;
//import com.web.common.exception.filter.InvalidWindSpeedRangeException;
//import com.web.trip.model.TripDto;
//import com.web.weather.model.WeatherDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.time.LocalDateTime;
//import java.util.Set;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TripFilterParamsTest {
//
//    private static final LocalDateTime DEFAULT_START = LocalDateTime.parse("2016-01-01T00:00:00.000");
//    private static final LocalDateTime DEFAULT_END = LocalDateTime.parse("2016-02-01T00:00:00.000");
//
//    private Set<String> mockTripFields;
//    private Set<String> mockWeatherFields;
//
//    @BeforeEach
//    void setUp() {
//        mockTripFields = Set.of("id", "distance", "fare");
//        mockWeatherFields = Set.of("temperature", "windSpeed");
//    }
//
//    @Test
//    void constructor_WithAllValidParameters_ShouldCreateInstance() {
//        // Arrange
//        LocalDateTime start = LocalDateTime.now();
//        LocalDateTime end = start.plusDays(1);
//
//        // Act
//        TripFilterParams params = new TripFilterParams(
//                start, end, 0.0, 10.0, 0, 20, "id", "asc"
//        );
//
//        // Assert
//        assertAll(
//                () -> assertEquals(start, params.startDateTime()),
//                () -> assertEquals(end, params.endDateTime()),
//                () -> assertEquals(0.0, params.minWindSpeed()),
//                () -> assertEquals(10.0, params.maxWindSpeed()),
//                () -> assertEquals(0, params.page()),
//                () -> assertEquals(20, params.size()),
//                () -> assertEquals("id", params.sort()),
//                () -> assertEquals("asc", params.direction())
//        );
//    }
//
//    @Test
//    void constructor_WithNullValues_ShouldUseDefaults() {
//        // Act
//        TripFilterParams params = new TripFilterParams(
//                null, null, null, null, null, null, null, null
//        );
//
//        // Assert
//        assertAll(
//                () -> assertEquals(DEFAULT_START, params.startDateTime()),
//                () -> assertEquals(DEFAULT_END, params.endDateTime()),
//                () -> assertEquals(0.0, params.minWindSpeed()),
//                () -> assertEquals(9999.0, params.maxWindSpeed()),
//                () -> assertEquals(0, params.page()),
//                () -> assertEquals(20, params.size()),
//                () -> assertEquals("id", params.sort()),
//                () -> assertEquals("asc", params.direction())
//        );
//    }
//
//    @Test
//    void validate_WithValidParameters_ShouldNotThrowException() {
//        try (MockedStatic<FieldNamesExtractor> mockedStatic = mockStatic(FieldNamesExtractor.class)) {
//            // Arrange
//            mockedStatic.when(() -> FieldNamesExtractor.getFields(TripDto.class))
//                    .thenReturn(mockTripFields);
//            mockedStatic.when(() -> FieldNamesExtractor.getFields(WeatherDto.class))
//                    .thenReturn(mockWeatherFields);
//
//            TripFilterParams params = new TripFilterParams(
//                    DEFAULT_START,
//                    DEFAULT_END,
//                    0.0,
//                    10.0,
//                    0,
//                    20,
//                    "id",
//                    "asc"
//            );
//
//            // Act & Assert
//            assertDoesNotThrow(params::validate);
//        }
//    }
//
//    @Test
//    void validate_WithInvalidDateRange_ShouldThrowException() {
//        // Arrange
//        TripFilterParams params = new TripFilterParams(
//                LocalDateTime.now().plusDays(1),
//                LocalDateTime.now(),
//                0.0,
//                10.0,
//                0,
//                20,
//                "id",
//                "asc"
//        );
//
//        // Act & Assert
//        assertThrows(InvalidDateRangeException.class, params::validate);
//    }
//
//    @Test
//    void validate_WithInvalidWindSpeedRange_ShouldThrowException() {
//        // Arrange
//        TripFilterParams params = new TripFilterParams(
//                DEFAULT_START,
//                DEFAULT_END,
//                10.0,
//                5.0,
//                0,
//                20,
//                "id",
//                "asc"
//        );
//
//        // Act & Assert
//        assertThrows(InvalidWindSpeedRangeException.class, params::validate);
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"invalid", "UP", "DOWN", "ascending"})
//    void validate_WithInvalidSortDirection_ShouldThrowException(String direction) {
//        // Arrange
//        TripFilterParams params = new TripFilterParams(
//                DEFAULT_START,
//                DEFAULT_END,
//                0.0,
//                10.0,
//                0,
//                20,
//                "id",
//                direction
//        );
//
//        // Act & Assert
//        assertThrows(InvalidSortDirectionException.class, params::validate);
//    }
//
//    @Test
//    void validate_WithInvalidSortField_ShouldThrowException() {
//        try (MockedStatic<FieldNamesExtractor> mockedStatic = mockStatic(FieldNamesExtractor.class)) {
//            // Arrange
//            mockedStatic.when(() -> FieldNamesExtractor.getFields(TripDto.class))
//                    .thenReturn(mockTripFields);
//            mockedStatic.when(() -> FieldNamesExtractor.getFields(WeatherDto.class))
//                    .thenReturn(mockWeatherFields);
//
//            TripFilterParams params = new TripFilterParams(
//                    DEFAULT_START,
//                    DEFAULT_END,
//                    0.0,
//                    10.0,
//                    0,
//                    20,
//                    "invalidField",
//                    "asc"
//            );
//
//            // Act & Assert
//            assertThrows(InvalidSortFieldException.class, params::validate);
//        }
//    }
//
//    @Test
//    void validate_WithValidSortDirectionCaseInsensitive_ShouldNotThrowException() {
//        try (MockedStatic<FieldNamesExtractor> mockedStatic = mockStatic(FieldNamesExtractor.class)) {
//            // Arrange
//            mockedStatic.when(() -> FieldNamesExtractor.getFields(TripDto.class))
//                    .thenReturn(mockTripFields);
//            mockedStatic.when(() -> FieldNamesExtractor.getFields(WeatherDto.class))
//                    .thenReturn(mockWeatherFields);
//
//            // Test both "ASC" and "DESC" in different cases
//            TripFilterParams paramsAsc = new TripFilterParams(
//                    DEFAULT_START, DEFAULT_END, 0.0, 10.0, 0, 20, "id", "ASC"
//            );
//            TripFilterParams paramsDesc = new TripFilterParams(
//                    DEFAULT_START, DEFAULT_END, 0.0, 10.0, 0, 20, "id", "DESC"
//            );
//
//            // Act & Assert
//            assertAll(
//                    () -> assertDoesNotThrow(paramsAsc::validate),
//                    () -> assertDoesNotThrow(paramsDesc::validate)
//            );
//        }
//    }
//
//    @Test
//    void validate_WithNullWindSpeeds_ShouldUseDefaults() {
//        try (MockedStatic<FieldNamesExtractor> mockedStatic = mockStatic(FieldNamesExtractor.class)) {
//            // Arrange
//            mockedStatic.when(() -> FieldNamesExtractor.getFields(TripDto.class))
//                    .thenReturn(mockTripFields);
//            mockedStatic.when(() -> FieldNamesExtractor.getFields(WeatherDto.class))
//                    .thenReturn(mockWeatherFields);
//
//            TripFilterParams params = new TripFilterParams(
//                    DEFAULT_START,
//                    DEFAULT_END,
//                    null,
//                    null,
//                    0,
//                    20,
//                    "id",
//                    "asc"
//            );
//
//            // Act & Assert
//            assertDoesNotThrow(params::validate);
//            assertEquals(0.0, params.minWindSpeed());
//            assertEquals(9999.0, params.maxWindSpeed());
//        }
//    }
//}