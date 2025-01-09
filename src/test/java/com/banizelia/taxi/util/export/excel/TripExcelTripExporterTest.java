package com.banizelia.taxi.util.export.excel;

import com.banizelia.taxi.config.ExcelExporterConfig;
import com.banizelia.taxi.trip.export.excel.TripExcelExporter;
import com.banizelia.taxi.trip.export.excel.TripExcelWriter;
import com.banizelia.taxi.trip.export.provider.TripDataProvider;
import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.trip.model.TripFilterParams;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.stream.Stream;


class TripExcelTripExporterTest {
    private TripDataProvider dataProvider;
    private ExcelExporterConfig conf;
    private TripExcelWriter writer;
    private EntityManager em;
    private TripExcelExporter exporter;

    @BeforeEach
    void setup() throws Exception {
        dataProvider = Mockito.mock(TripDataProvider.class);
        conf = Mockito.mock(ExcelExporterConfig.class);
        writer = Mockito.mock(TripExcelWriter.class);
        em = Mockito.mock(EntityManager.class);

        exporter = new TripExcelExporter(dataProvider, conf, writer);

        Field field = TripExcelExporter.class.getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(exporter, em);

        Mockito.when(conf.getSheetPrefix()).thenReturn("Sheet");
        Mockito.when(conf.getMaxRowsPerSheet()).thenReturn(10);
        Mockito.when(conf.getBatchSize()).thenReturn(5);
    }

    @Test
    void testExportTrips() throws IOException {
        Mockito.when(dataProvider.provide(Mockito.any())).thenReturn(Stream.of(new TripDto(), new TripDto()));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        exporter.export(out, new TripFilterParams());

        Mockito.verify(writer, Mockito.atLeastOnce()).writeHeaders(Mockito.any());
        Mockito.verify(writer, Mockito.atLeastOnce()).writeRow(Mockito.any(), Mockito.anyInt(), Mockito.any(TripDto.class));
    }
}
