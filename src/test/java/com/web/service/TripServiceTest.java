package com.web.service;

import com.web.model.Trip;
import com.web.repository.TripsRepository;
import com.web.util.ExcelHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    void testFilterValidInput() {
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 2, 1, 0, 0);
        Double minWindSpeed = 0.0;
        Double maxWindSpeed = 10.0;
        String direction = "asc";
        String sortBy = "id";
        Integer page = 0;
        Integer pageSize = 20;

        List<Trip> expectedTrips = Arrays.asList(new Trip(), new Trip());
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy));

        when(tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable))
                .thenReturn(expectedTrips);

        ResponseEntity<List<Trip>> response = tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed,
                direction, sortBy, page, pageSize);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedTrips, response.getBody());
    }

    @Test
    void testFilterInvalidDateRange() {
        LocalDateTime startDateTime = LocalDateTime.of(2016, 2, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);

        assertThrows(IllegalArgumentException.class, () ->
                tripService.filter(startDateTime, endDateTime, 0.0, 10.0, "asc", "id", 0, 20));
    }

    @Test
    void testFilterInvalidPage() {
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 2, 1, 0, 0);

        assertThrows(IllegalArgumentException.class, () ->
                tripService.filter(startDateTime, endDateTime, 0.0, 10.0, "asc", "id", -1, 20));
    }

    @Test
    void testFilterInvalidDirection() {
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 2, 1, 0, 0);

        assertThrows(IllegalArgumentException.class, () ->
                tripService.filter(startDateTime, endDateTime, 0.0, 10.0, "invalid", "id", 0, 20));
    }

    @Test
    void testFilterInvalidSortBy() {
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 2, 1, 0, 0);

        assertThrows(IllegalArgumentException.class, () ->
                tripService.filter(startDateTime, endDateTime, 0.0, 10.0, "asc", "invalidField", 0, 20));
    }

    @Test
    void testDownloadValidInput() {
        Integer sheetLimit = 2;
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);

        // Мокируем статический метод ExcelHelper.tripsToExcel
        try (MockedStatic<ExcelHelper> excelHelperMockedStatic = mockStatic(ExcelHelper.class)) {
            excelHelperMockedStatic.when(() -> ExcelHelper.tripsToExcel(tripsRepository, sheetLimit)).thenReturn(bais);

            ResponseEntity<Resource> response = tripService.download(sheetLimit);

            assertEquals(200, response.getStatusCodeValue());
            assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0).contains("trips.xlsx"));
            assertEquals(MediaType.parseMediaType("application/vnd.ms-excel"), response.getHeaders().getContentType());
            assertTrue(response.getBody() instanceof InputStreamResource);
        }
    }

    @Test
    void testDownloadInvalidSheetLimit() {
        assertThrows(IllegalArgumentException.class, () -> tripService.download(0));
    }

    @Test
    void testFilterInvalidWindSpeedRange() {
        LocalDateTime startDateTime = LocalDateTime.of(2016, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2016, 2, 1, 0, 0);
        Double minWindSpeed = 10.0;
        Double maxWindSpeed = 5.0;

        assertThrows(IllegalArgumentException.class, () ->
                tripService.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, "asc", "id", 0, 20));
    }
}