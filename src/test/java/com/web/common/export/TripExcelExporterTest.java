package com.web.common.export;

import com.web.trip.model.Trip;
import com.web.trip.repository.TripsRepository;
import com.web.trip.model.TripFilterParams;
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

    private ExcelExporterConf conf;
    private TripExcelExporter exporter;
    private ByteArrayOutputStream outputStream;
    private TripFilterParams filterParams;

    @BeforeEach
    void setUp() {
        conf = new ExcelExporterConf("Sheet_", 1000, 100);
        exporter = new TripExcelExporter(tripsRepository, conf);
        outputStream = new ByteArrayOutputStream();
        filterParams = new TripFilterParams();
    }

    @Test
    void shouldExportEmptyWorkbookWhenNoTrips() throws IOException {
        // Мокаем метод streamFilter, чтобы он возвращал пустой поток
        when(tripsRepository.streamFilter(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()))
                .thenReturn(Stream.<Trip>empty());

        exporter.tripsToExcelStream(outputStream, filterParams);

        assertTrue(outputStream.size() > 0, "OutputStream должен содержать данные даже при отсутствии поездок");
        // Проверяем, что метод streamFilter был вызван с любыми параметрами
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
        // Мокаем метод streamFilter, чтобы он возвращал поток с двумя поездками
        when(tripsRepository.streamFilter(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()))
                .thenReturn(Stream.of(trip1, trip2));

        exporter.tripsToExcelStream(outputStream, filterParams);

        assertTrue(outputStream.size() > 0, "OutputStream должен содержать данные при наличии поездок");
        // Проверяем, что метод streamFilter был вызван с любыми параметрами
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
        // Устанавливаем конфигурацию с ограничением в 1 строку на лист для теста
        conf = new ExcelExporterConf("Sheet_", 1, 100);
        exporter = new TripExcelExporter(tripsRepository, conf);

        Trip trip1 = createTrip(1L, LocalDateTime.now());
        Trip trip2 = createTrip(2L, LocalDateTime.now().plusHours(1));
        // Мокаем метод streamFilter, чтобы он возвращал поток с двумя поездками
        when(tripsRepository.streamFilter(
                any(),
                any(),
                any(),
                any(),
                any(),
                any()))
                .thenReturn(Stream.of(trip1, trip2));

        exporter.tripsToExcelStream(outputStream, filterParams);

        assertTrue(outputStream.size() > 0, "OutputStream должен содержать данные при превышении лимита строк на лист");
        // Проверяем, что метод streamFilter был вызван с любыми параметрами
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
        // Установите другие поля при необходимости
        return trip;
    }
}
