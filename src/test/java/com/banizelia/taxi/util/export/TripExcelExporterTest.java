package com.banizelia.taxi.util.export;

import com.banizelia.taxi.config.ExcelExporterConfig;
import com.banizelia.taxi.trip.model.Trip;
import com.banizelia.taxi.trip.repository.TripsRepository;
import com.banizelia.taxi.trip.model.TripFilterParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TripExcelExporterTest {

    @Mock
    private TripsRepository tripsRepository;

    private ExcelExporterConfig conf;
    private TripExcelExporter exporter;
    private ByteArrayOutputStream outputStream;
    private TripFilterParams filterParams;

    @BeforeEach
    void setUp() {
        conf = new ExcelExporterConfig("Sheet_", 1000, 100);
        exporter = new TripExcelExporter(tripsRepository, conf);
        outputStream = new ByteArrayOutputStream();
        filterParams = new TripFilterParams();
    }

    @Test
    void shouldExportEmptyWorkbookWhenNoTrips() throws IOException {
        when(tripsRepository.streamFilter(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()))
                .thenReturn(Stream.<Trip>empty());

        exporter.tripsToExcelStream(outputStream, filterParams);

        assertTrue(outputStream.size() > 0);
        verify(tripsRepository).streamFilter(
                any(),
                any(),
                any(),
                any(),
                any(),
                any());
    }

    @Test
    void shouldExportTripsToExcel() throws IOException {
        Trip trip1 = createTrip(1L, LocalDateTime.now());
        Trip trip2 = createTrip(2L, LocalDateTime.now().plusHours(1));

        when(tripsRepository.streamFilter(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()))
                .thenReturn(Stream.of(trip1, trip2));

        exporter.tripsToExcelStream(outputStream, filterParams);

        assertTrue(outputStream.size() > 0);

        verify(tripsRepository).streamFilter(
                any(),
                any(),
                any(),
                any(),
                any(),
                any());
    }

    @Test
    void shouldCreateNewSheetWhenExceedingMaxRows() throws IOException {

        conf = new ExcelExporterConfig("Sheet_", 1, 100);
        exporter = new TripExcelExporter(tripsRepository, conf);

        Trip trip1 = createTrip(1L, LocalDateTime.now());
        Trip trip2 = createTrip(2L, LocalDateTime.now().plusHours(1));

        when(tripsRepository.streamFilter(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()))
                .thenReturn(Stream.of(trip1, trip2));

        exporter.tripsToExcelStream(outputStream, filterParams);

        assertTrue(outputStream.size() > 0);

        verify(tripsRepository).streamFilter(
                any(),
                any(),
                any(),
                any(),
                any(),
                any());
    }

    private Trip createTrip(Long id, LocalDateTime pickupTime) {
        Trip trip = new Trip();
        trip.setId(id);
        trip.setPickupDatetime(pickupTime);
        trip.setDropoffDatetime(pickupTime.plusHours(1));
        trip.setPassengerCount(2);
        trip.setTripDistance(10.5);
        trip.setFareAmount(25.0);
        trip.setTipAmount(5.0);
        trip.setTotalAmount(30.0);

        return trip;
    }
}
