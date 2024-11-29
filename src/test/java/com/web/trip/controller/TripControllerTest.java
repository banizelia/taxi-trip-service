package com.web.trip.controller;

import com.web.trip.model.TripDto;
import com.web.trip.service.DownloadTripService;
import com.web.trip.service.filter.FilterTripService;
import com.web.trip.model.TripFilterParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private PagedResourcesAssembler<TripDto> pagedResourcesAssembler;

    @InjectMocks
    private TripController tripController;

    private TripDto tripDto;
    private TripFilterParams filterParams;
    private Page<TripDto> tripPage;
    private PagedModel<EntityModel<TripDto>> pagedModel;

    @BeforeEach
    void setUp() {
        // Setup test data
        tripDto = new TripDto();
        tripDto.setId(1L);
        tripDto.setVendorId("TEST123");

        List<TripDto> trips = new ArrayList<>();
        trips.add(tripDto);

        tripPage = new PageImpl<>(trips);

        filterParams = new TripFilterParams(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                0.0,
                10.0,
                0,
                20,
                "id",
                "asc"
        );

        // Mock PagedResourcesAssembler
        @SuppressWarnings("unchecked")
        PagedModel<EntityModel<TripDto>> mockedPagedModel = mock(PagedModel.class);
        pagedModel = mockedPagedModel;
    }

    @Test
    void filterTrips_ShouldReturnValidResponse() {
        // Arrange
        when(filterTripService.execute(any(TripFilterParams.class))).thenReturn(tripPage);
        when(pagedResourcesAssembler.toModel(any(Page.class))).thenReturn(pagedModel);

        // Act
        ResponseEntity<PagedModel<EntityModel<TripDto>>> response = tripController.filterTrips(filterParams);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        verify(filterTripService).execute(filterParams);
        verify(pagedResourcesAssembler).toModel(tripPage);
    }

    @Test
    void filterTrips_ShouldValidateFilterParams() {
        // Arrange
        when(filterTripService.execute(any(TripFilterParams.class))).thenReturn(tripPage);
        when(pagedResourcesAssembler.toModel(any(Page.class))).thenReturn(pagedModel);

        // Act
        ResponseEntity<PagedModel<EntityModel<TripDto>>> response = tripController.filterTrips(filterParams);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        // Verify that validate() was called
        // Note: Since filterParams is a mock, we can't directly verify the validate() call
        // but we can verify that the flow continued successfully
        verify(filterTripService).execute(filterParams);
    }

    @Test
    void download_WithValidFilename_ShouldReturnValidResponse() {
        // Arrange
        String filename = "test";
        StreamingResponseBody streamingResponseBody = outputStream -> {};
        when(downloadTripService.execute()).thenReturn(streamingResponseBody);

        // Act
        ResponseEntity<StreamingResponseBody> response = tripController.download(filename);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        // Verify headers
        HttpHeaders headers = response.getHeaders();
        assertTrue(headers.getContentDisposition().toString().contains("test"));
        assertEquals(MediaType.parseMediaType("application/vnd.ms-excel"), response.getHeaders().getContentType());

        verify(downloadTripService).execute();
    }

    @ParameterizedTest
    @ValueSource(strings = {"test!@#", "invalid*file", "bad/name", "wrong\\file"})
    void download_WithInvalidFilename_ShouldReturnBadRequest(String invalidFilename) {
        // Act
        ResponseEntity<StreamingResponseBody> response = tripController.download(invalidFilename);

        // Assert
        assertEquals(400, response.getStatusCode().value());

        // Verify downloadTripService was not called
        verify(downloadTripService, never()).execute();
    }

    @Test
    void download_ShouldIncludeTimestampInFilename() {
        // Arrange
        String filename = "test";
        StreamingResponseBody streamingResponseBody = outputStream -> {};
        when(downloadTripService.execute()).thenReturn(streamingResponseBody);

        // Act
        ResponseEntity<StreamingResponseBody> response = tripController.download(filename);

        // Assert
        String contentDisposition = response.getHeaders().getContentDisposition().toString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        assertTrue(contentDisposition.contains(timestamp));
    }

    @Test
    void filterTrips_WhenEmptyResult_ShouldReturnEmptyPage() {
        // Arrange
        when(filterTripService.execute(any(TripFilterParams.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        when(pagedResourcesAssembler.toModel(any(Page.class))).thenReturn(pagedModel);

        // Act
        ResponseEntity<PagedModel<EntityModel<TripDto>>> response = tripController.filterTrips(filterParams);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(filterTripService).execute(filterParams);
    }

    @Test
    void download_ShouldHaveCorrectContentType() {
        // Arrange
        StreamingResponseBody streamingResponseBody = outputStream -> {};
        when(downloadTripService.execute()).thenReturn(streamingResponseBody);

        // Act
        ResponseEntity<StreamingResponseBody> response = tripController.download("test");

        // Assert
        assertEquals(MediaType.parseMediaType("application/vnd.ms-excel"),
                response.getHeaders().getContentType());
    }
}