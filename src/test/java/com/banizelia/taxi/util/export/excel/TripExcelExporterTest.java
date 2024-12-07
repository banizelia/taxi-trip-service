package com.banizelia.taxi.util.export.excel;

import com.banizelia.taxi.config.ExcelExporterConfig;
import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.util.export.TripDataProvider;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import static org.mockito.Mockito.when;

class TripExcelExporterTest {
    @Test
    void testExportTrips() throws Exception {
        TripDataProvider provider = Mockito.mock(TripDataProvider.class);
        ExcelExporterConfig conf = Mockito.mock(ExcelExporterConfig.class);
        TripExcelWriter writer = Mockito.mock(TripExcelWriter.class);

        when(conf.getSheetPrefix()).thenReturn("trips_");
        when(conf.getMaxRowsPerSheet()).thenReturn(100);
        when(conf.getBatchSize()).thenReturn(10);

        when(writer.createSheet(Mockito.any(Workbook.class), Mockito.anyString()))
                .thenAnswer(invocation -> {
                    Workbook workbook = invocation.getArgument(0);
                    String sheetName = invocation.getArgument(1);
                    return workbook.newWorksheet(sheetName);
                });

        Iterator<TripDto> it = new Iterator<>() {
            int count = 0;

            @Override
            public boolean hasNext() {
                return count < 5;
            }

            @Override
            public TripDto next() {
                if (!hasNext()) throw new NoSuchElementException();
                count++;
                TripDto dto = new TripDto();
                dto.setId((long) count);
                return dto;
            }
        };
        when(provider.provide(Mockito.any())).thenReturn(it);

        TripExcelExporter exporter = new TripExcelExporter(provider, conf, writer);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        exporter.exportTrips(out, new TripFilterParams());

        Mockito.verify(writer, Mockito.atLeastOnce()).writeHeaders(Mockito.any(Worksheet.class));
        Mockito.verify(writer, Mockito.atLeastOnce()).writeRow(Mockito.any(Worksheet.class), Mockito.anyInt(), Mockito.any());
    }
}
