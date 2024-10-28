package com.web.service.trip;

import com.web.model.dto.TripDto;
import com.web.service.trip.managment.DownloadTripService;
import com.web.service.trip.managment.FilterTripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private FilterTripService filterTripService;

    @Mock
    private DownloadTripService downloadTripService;

    private TripService tripService;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @BeforeEach
    void setUp() {
        tripService = new TripService(filterTripService, downloadTripService);
        startDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
        endDateTime = LocalDateTime.of(2024, 1, 2, 0, 0);
    }

    @Test
    void filter_ShouldDelegateToFilterTripService() {
        // Arrange
        Double minWindSpeed = 10.0;
        Double maxWindSpeed = 20.0;
        Integer page = 0;
        Integer size = 10;
        String sortBy = "id";
        String direction = "asc";

        Page<TripDto> expectedPage = new PageImpl<>(new ArrayList<>());
        when(filterTripService.execute(
                startDateTime, endDateTime,
                minWindSpeed, maxWindSpeed,
                page, size,
                sortBy, direction))
                .thenReturn(expectedPage);

        // Act
        Page<TripDto> result = tripService.filter(
                startDateTime, endDateTime,
                minWindSpeed, maxWindSpeed,
                page, size,
                sortBy, direction);

        // Assert
        assertSame(expectedPage, result);
        verify(filterTripService).execute(
                startDateTime, endDateTime,
                minWindSpeed, maxWindSpeed,
                page, size,
                sortBy, direction);
        verifyNoMoreInteractions(filterTripService);
    }

    @Test
    void download_ShouldDelegateToDownloadTripService() {
        // Arrange
        StreamingResponseBody expectedResponse = outputStream -> {};
        when(downloadTripService.execute()).thenReturn(expectedResponse);

        // Act
        StreamingResponseBody result = tripService.download();

        // Assert
        assertSame(expectedResponse, result);
        verify(downloadTripService).execute();
        verifyNoMoreInteractions(downloadTripService);
    }

    @Test
    void filter_ShouldPassNullParameters() {
        // Arrange
        Page<TripDto> expectedPage = new PageImpl<>(new ArrayList<>());
        when(filterTripService.execute(
                null, null,
                null, null,
                null, null,
                null, null))
                .thenReturn(expectedPage);

        // Act
        Page<TripDto> result = tripService.filter(
                null, null,
                null, null,
                null, null,
                null, null);

        // Assert
        assertSame(expectedPage, result);
        verify(filterTripService).execute(
                null, null,
                null, null,
                null, null,
                null, null);
    }

    @Test
    void service_ShouldNotInteractWithWrongService() {
        // Arrange
        when(filterTripService.execute(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act
        tripService.filter(
                startDateTime, endDateTime,
                10.0, 20.0,
                0, 10,
                "id", "asc");

        // Assert
        verify(filterTripService, times(1))
                .execute(any(), any(), any(), any(), any(), any(), any(), any());
        verifyNoInteractions(downloadTripService);
    }
}