package com.banizelia.taxi.trip.controller;

import com.banizelia.taxi.favorite.service.DeleteFavoriteTripService;
import com.banizelia.taxi.favorite.service.DragAndDropFavoriteTripService;
import com.banizelia.taxi.favorite.service.SaveFavoriteTripService;
import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.trip.service.DownloadTripService;
import com.banizelia.taxi.trip.service.FilterTripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripControllerTest {

    @Mock
    private FilterTripService filterTripService;

    @Mock
    private DownloadTripService downloadTripService;

    @Mock
    private SaveFavoriteTripService saveFavoriteTripService;

    @Mock
    private DeleteFavoriteTripService deleteFavoriteTripService;

    @Mock
    private DragAndDropFavoriteTripService dragAndDropFavoriteTripService;

    @InjectMocks
    private TripController tripController;

    private TripFilterParams filterParams;
    private Page<TripDto> tripPage;

    @BeforeEach
    void setUp() {
        TripDto tripDto = new TripDto();
        tripDto.setId(1L);
        tripDto.setVendorId(1);

        List<TripDto> trips = List.of(tripDto);
        tripPage = new PageImpl<>(trips);

        filterParams = new TripFilterParams();
        filterParams.setPickupDateTimeFrom(LocalDateTime.now().minusDays(1));
        filterParams.setPickupDateTimeTo(LocalDateTime.now().plusDays(1));
        filterParams.setMinWindSpeed(0.0);
        filterParams.setMaxWindSpeed(10.0);
    }

    @Test
    void filterTrips_ShouldReturnValidResponse() {
        when(filterTripService.execute(any(TripFilterParams.class), any(Pageable.class))).thenReturn(tripPage);

        ResponseEntity<Page<TripDto>> response = tripController.filterTrips(filterParams, mock(Pageable.class));

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tripPage, response.getBody());

        ArgumentCaptor<TripFilterParams> paramsCaptor = ArgumentCaptor.forClass(TripFilterParams.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(filterTripService, times(1)).execute(paramsCaptor.capture(), pageableCaptor.capture());

        assertEquals(filterParams, paramsCaptor.getValue());
    }

    @Test
    void filterTrips_ShouldReturnEmptyPage() {
        Page<TripDto> emptyPage = new PageImpl<>(Collections.emptyList());
        when(filterTripService.execute(any(TripFilterParams.class), any(Pageable.class))).thenReturn(emptyPage);

        ResponseEntity<Page<TripDto>> response = tripController.filterTrips(filterParams, mock(Pageable.class));

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(filterTripService, times(1)).execute(any(TripFilterParams.class), any(Pageable.class));
    }

    @Test
    void addToFavorites_ShouldReturnCreatedStatus() {
        Long tripId = 1L;
        doNothing().when(saveFavoriteTripService).execute(tripId);

        ResponseEntity<String> response = tripController.addToFavorites(tripId);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertEquals("Trip added to favorites", response.getBody());

        verify(saveFavoriteTripService, times(1)).execute(tripId);
    }

    @Test
    void deleteFromFavourite_ShouldReturnOkStatus() {
        Long tripId = 1L;
        doNothing().when(deleteFavoriteTripService).execute(tripId);

        ResponseEntity<String> response = tripController.deleteFromFavourite(tripId);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("Trip deleted successfully", response.getBody());

        verify(deleteFavoriteTripService, times(1)).execute(tripId);
    }

    @Test
    void dragAndDrop_ShouldReturnOkStatus() {
        Long tripId = 1L;
        Long newPosition = 2L;
        doNothing().when(dragAndDropFavoriteTripService).execute(tripId, newPosition);

        ResponseEntity<String> response = tripController.dragAndDrop(tripId, newPosition);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("Position updated successfully", response.getBody());

        verify(dragAndDropFavoriteTripService, times(1)).execute(tripId, newPosition);
    }

    @Test
    void download_WithValidFilenameAndParams_ShouldReturnValidResponse() {
        String filename = "test";
        TripFilterParams params = filterParams;
        StreamingResponseBody streamingResponseBody = outputStream -> {
        };
        when(downloadTripService.execute(any(TripFilterParams.class))).thenReturn(streamingResponseBody);

        ResponseEntity<StreamingResponseBody> response = tripController.download(filename, params);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

        HttpHeaders headers = response.getHeaders();
        String contentDisposition = headers.getFirst(HttpHeaders.CONTENT_DISPOSITION);

        assertTrue(Objects.requireNonNull(contentDisposition).startsWith("attachment; filename="));
        assertTrue(contentDisposition.contains(".xlsx"));

        assertEquals(MediaType.parseMediaType("application/vnd.ms-excel"), headers.getContentType());

        ArgumentCaptor<TripFilterParams> paramsCaptor = ArgumentCaptor.forClass(TripFilterParams.class);
        verify(downloadTripService, times(1)).execute(paramsCaptor.capture());
        assertEquals(params, paramsCaptor.getValue());
    }

    @Test
    void download_ShouldIncludeTimestampInFilename() {
        String filename = "test";
        TripFilterParams params = filterParams;
        StreamingResponseBody streamingResponseBody = outputStream -> {
        };
        when(downloadTripService.execute(any(TripFilterParams.class))).thenReturn(streamingResponseBody);

        ResponseEntity<StreamingResponseBody> response = tripController.download(filename, params);

        String contentDisposition = response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION);
        assertNotNull(contentDisposition);
        assertTrue(contentDisposition.contains(".xlsx"));

        assertTrue(contentDisposition.matches("attachment; filename=test_\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2}\\.xlsx"));
    }

    @Test
    void download_ShouldHaveCorrectContentType() {

        String filename = "test";
        TripFilterParams params = filterParams;
        StreamingResponseBody streamingResponseBody = outputStream -> {
        };
        when(downloadTripService.execute(any(TripFilterParams.class))).thenReturn(streamingResponseBody);

        ResponseEntity<StreamingResponseBody> response = tripController.download(filename, params);

        assertEquals(MediaType.parseMediaType("application/vnd.ms-excel"), response.getHeaders().getContentType());
    }
}
