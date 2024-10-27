package com.web.controller;

import com.web.model.dto.TripDto;
import com.web.service.trip.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripControllerTest {

    @Mock
    private TripService tripService;

    @Mock
    private PagedResourcesAssembler<TripDto> pagedResourcesAssembler;

    @InjectMocks
    private TripController tripController;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private List<TripDto> mockTrips;
    private Page<TripDto> tripPage;
    private PagedModel<EntityModel<TripDto>> pagedModel;

    @BeforeEach
    void setUp() {
        startDateTime = LocalDateTime.parse("2016-01-01T00:00:00.000");
        endDateTime = LocalDateTime.parse("2016-02-01T00:00:00.000");

        mockTrips = Arrays.asList(
                new TripDto(), // Заполните необходимыми данными
                new TripDto()
        );

        tripPage = new PageImpl<>(mockTrips);

        // Mock PagedModel
        pagedModel = mock(PagedModel.class);
    }

    @Test
    void filterTrips_Success() {
        // Arrange
        when(tripService.filter(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                anyDouble(),
                anyDouble(),
                anyInt(),
                anyInt(),
                anyString(),
                anyString()
        )).thenReturn(tripPage);

        when(pagedResourcesAssembler.toModel(tripPage)).thenReturn(pagedModel);

        // Act
        ResponseEntity<PagedModel<EntityModel<TripDto>>> response = tripController.filterTrips(
                startDateTime,
                endDateTime,
                0.0,
                9999.0,
                0,
                20,
                "id",
                "asc"
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pagedModel, response.getBody());
        verify(tripService).filter(
                startDateTime,
                endDateTime,
                0.0,
                9999.0,
                0,
                20,
                "id",
                "asc"
        );
    }

    @Test
    void filterTrips_WhenIllegalArgumentException_ThrowsResponseStatusException() {
        // Arrange
        when(tripService.filter(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                anyDouble(),
                anyDouble(),
                anyInt(),
                anyInt(),
                anyString(),
                anyString()
        )).thenThrow(new IllegalArgumentException("Invalid parameters"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> tripController.filterTrips(
                        startDateTime,
                        endDateTime,
                        0.0,
                        9999.0,
                        0,
                        20,
                        "id",
                        "asc"
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid parameters", exception.getReason());
    }

    @Test
    void filterTrips_WhenGeneralException_ThrowsResponseStatusException() {
        // Arrange
        when(tripService.filter(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                anyDouble(),
                anyDouble(),
                anyInt(),
                anyInt(),
                anyString(),
                anyString()
        )).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> tripController.filterTrips(
                        startDateTime,
                        endDateTime,
                        0.0,
                        9999.0,
                        0,
                        20,
                        "id",
                        "asc"
                )
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Unexpected error", exception.getReason());
    }

    @Test
    void download_Success() {
        // Arrange
        String filename = "trips";
        StreamingResponseBody mockStream = outputStream -> {};
        when(tripService.download()).thenReturn(mockStream);

        // Act
        ResponseEntity<StreamingResponseBody> response = tripController.download(filename);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getHeaders().getContentDisposition().toString()
                .contains("attachment; filename*=UTF-8''trips_"));
        assertEquals(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
                response.getHeaders().getContentType());
        assertNotNull(response.getBody());
        verify(tripService).download();
    }

    @Test
    void download_WithInvalidFilename_ReturnsBadRequest() {
        // Arrange
        String invalidFilename = "trips@#$%";

        // Act
        ResponseEntity<StreamingResponseBody> response = tripController.download(invalidFilename);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(tripService);
    }

    @Test
    void download_WhenServiceThrowsException_ReturnsInternalServerError() {
        // Arrange
        String filename = "trips";
        when(tripService.download()).thenThrow(new RuntimeException("Error generating excel"));

        // Act
        ResponseEntity<StreamingResponseBody> response = tripController.download(filename);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(tripService).download();
    }

    @Test
    void download_ValidatesFilenameFormat() {
        // Arrange
        String[] validFilenames = {"trips", "my-trips", "trips_2023", "TRIPS"};
        String[] invalidFilenames = {"trips!", "my trips", "trips@#", "trips/"};

        // Act & Assert
        for (String validName : validFilenames) {
            ResponseEntity<StreamingResponseBody> response = tripController.download(validName);
            assertNotEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        for (String invalidName : invalidFilenames) {
            ResponseEntity<StreamingResponseBody> response = tripController.download(invalidName);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }
}