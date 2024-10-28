package com.web.service.trip.managment;

import com.web.model.Trip;
import com.web.model.dto.TripDto;
import com.web.repository.TripsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilterTripServiceTest {

    @Mock
    private TripsRepository tripsRepository;

    private FilterTripService filterTripService;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @BeforeEach
    void setUp() {
        filterTripService = new FilterTripService(tripsRepository);
        startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        endDateTime = LocalDateTime.of(2024, 1, 2, 0, 0);
    }

    @Test
    void execute_WithValidParameters_ShouldReturnPageOfTripDto() {
        // Arrange
        Double minWindSpeed = 10.0;
        Double maxWindSpeed = 20.0;
        int page = 0;
        int size = 10;
        String sortBy = "id"; // Предполагаем, что это валидное поле из ColumnAnnotatedFields.getTripFields()
        String direction = "asc";

        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip()); // Добавьте необходимые данные для Trip

        Page<Trip> tripPage = new PageImpl<>(trips);
        when(tripsRepository.filter(any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(tripPage);

        // Act
        Page<TripDto> result = filterTripService.execute(
                startDateTime, endDateTime, minWindSpeed, maxWindSpeed,
                page, size, sortBy, direction
        );

        // Assert
        assertNotNull(result);
        assertEquals(trips.size(), result.getContent().size());
        verify(tripsRepository).filter(
                eq(startDateTime),
                eq(endDateTime),
                eq(minWindSpeed),
                eq(maxWindSpeed),
                any(Pageable.class)
        );
    }

    @Test
    void execute_WhenEndDateIsBeforeStartDate_ShouldThrowIllegalArgumentException() {
        // Arrange
        LocalDateTime invalidEndDateTime = startDateTime.minusDays(1);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> filterTripService.execute(
                        startDateTime,
                        invalidEndDateTime,
                        10.0,
                        20.0,
                        0,
                        10,
                        "id",
                        "asc"
                )
        );

        assertTrue(exception.getMessage().contains("endDateTime is before startDateTime"));
    }

    @Test
    void execute_WhenMaxWindSpeedIsSmallerThanMinWindSpeed_ShouldThrowIllegalArgumentException() {
        // Arrange
        Double minWindSpeed = 20.0;
        Double maxWindSpeed = 10.0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> filterTripService.execute(
                        startDateTime,
                        endDateTime,
                        minWindSpeed,
                        maxWindSpeed,
                        0,
                        10,
                        "id",
                        "asc"
                )
        );

        assertTrue(exception.getMessage().contains("maxWindSpeed is smaller or equal to minWindSpeed"));
    }

    @Test
    void execute_WhenInvalidDirection_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> filterTripService.execute(
                        startDateTime,
                        endDateTime,
                        10.0,
                        20.0,
                        0,
                        10,
                        "id",
                        "invalid_direction"
                )
        );

        assertTrue(exception.getMessage().contains("Invalid direction"));
    }

    @Test
    void execute_WhenInvalidSortField_ShouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> filterTripService.execute(
                        startDateTime,
                        endDateTime,
                        10.0,
                        20.0,
                        0,
                        10,
                        "invalid_field",
                        "asc"
                )
        );

        assertTrue(exception.getMessage().contains("Invalid sort field"));
    }

    @Test
    void execute_WithValidDirectionInDifferentCase_ShouldWork() {
        // Arrange
        when(tripsRepository.filter(any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act & Assert
        assertDoesNotThrow(() -> filterTripService.execute(
                startDateTime,
                endDateTime,
                10.0,
                20.0,
                0,
                10,
                "id",
                "ASC"
        ));

        assertDoesNotThrow(() -> filterTripService.execute(
                startDateTime,
                endDateTime,
                10.0,
                20.0,
                0,
                10,
                "id",
                "desc"
        ));
    }
}