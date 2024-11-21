package com.web.common.export;

import com.web.trip.mapper.TripMapper;
import com.web.trip.model.Trip;
import com.web.trip.model.TripDto;
import com.web.trip.repository.TripsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripExcelExporterTest {

    @Mock
    private TripsRepository tripsRepository;

    private TripExcelExporter exporter;
    private Trip mockTrip;
    private TripDto mockTripDto;

    @BeforeEach
    void setUp() {
        // Initialize with required constructor parameters
        exporter = new TripExcelExporter(
                tripsRepository,
                "test_",  // sheetPrefix
                100,      // maxRowsPerSheet
                50       // batchSize
        );

        mockTrip = new Trip();
        mockTrip.setId(1L);
        mockTrip.setVendorId("TEST");
        mockTrip.setPickupDatetime(LocalDateTime.now());
        mockTrip.setPassengerCount(2);
        mockTrip.setTripDistance(10.5);

        mockTripDto = TripMapper.INSTANCE.tripToTripDto(mockTrip);
    }

    @Test
    void testTripsToExcelStream_SingleTrip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(tripsRepository.findAllStream()).thenReturn(Stream.of(mockTrip));

        exporter.tripsToExcelStream(outputStream);

        assertTrue(outputStream.size() > 0);
        verify(tripsRepository, times(1)).findAllStream();
    }

    @Test
    void testTripsToExcelStream_EmptyData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(tripsRepository.findAllStream()).thenReturn(Stream.empty());

        exporter.tripsToExcelStream(outputStream);

        assertTrue(outputStream.size() > 0);
        verify(tripsRepository, times(1)).findAllStream();
    }

    @Test
    void testTripsToExcelStream_NullValues() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Trip nullTrip = new Trip();
        when(tripsRepository.findAllStream()).thenReturn(Stream.of(nullTrip));

        assertDoesNotThrow(() -> exporter.tripsToExcelStream(outputStream));
        assertTrue(outputStream.size() > 0);
    }

    @Test
    void testTripsToExcelStream_DifferentDataTypes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setVendorId("TEST");
        trip.setPickupDatetime(LocalDateTime.now());
        trip.setPassengerCount(2);
        trip.setTripDistance(10.5);

        when(tripsRepository.findAllStream()).thenReturn(Stream.of(trip));

        assertDoesNotThrow(() -> exporter.tripsToExcelStream(outputStream));
        assertTrue(outputStream.size() > 0);
    }
}