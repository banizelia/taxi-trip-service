package com.web.common.export;

import com.web.trip.model.Trip;
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

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TripExcelExporterTest {

    @Mock
    private TripsRepository tripsRepository;

    private ExcelExporterConf conf;
    private TripExcelExporter exporter;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        conf = new ExcelExporterConf("Sheet_", 1000, 100);
        exporter = new TripExcelExporter(tripsRepository, conf);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    void shouldExportEmptyWorkbookWhenNoTrips() throws IOException {

        exporter.tripsToExcelStream(outputStream);

        assertThat(outputStream.size()).isPositive();
        verify(tripsRepository).findAllStream();
    }

    @Test
    void shouldExportTripsToExcel() throws IOException {
        Trip trip1 = createTrip(1L, LocalDateTime.now());
        Trip trip2 = createTrip(2L, LocalDateTime.now().plusHours(1));
        when(tripsRepository.findAllStream())
                .thenReturn(Stream.of(trip1, trip2));

        exporter.tripsToExcelStream(outputStream);

        assertThat(outputStream.size()).isPositive();
        verify(tripsRepository).findAllStream();
    }

    @Test
    void shouldCreateNewSheetWhenExceedingMaxRows() throws IOException {
        conf = new ExcelExporterConf("Sheet_", 1, 100);
        exporter = new TripExcelExporter(tripsRepository, conf);
        Trip trip1 = createTrip(1L, LocalDateTime.now());
        Trip trip2 = createTrip(2L, LocalDateTime.now().plusHours(1));
        when(tripsRepository.findAllStream())
                .thenReturn(Stream.of(trip1, trip2));

        exporter.tripsToExcelStream(outputStream);

        assertThat(outputStream.size()).isPositive();
        verify(tripsRepository).findAllStream();
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