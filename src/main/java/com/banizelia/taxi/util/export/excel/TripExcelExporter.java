package com.banizelia.taxi.util.export.excel;

import com.banizelia.taxi.config.ExcelExporterConfig;
import com.banizelia.taxi.error.export.ExportException;
import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.util.export.TripDataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripExcelExporter {
    private final TripDataProvider dataProvider;
    private final ExcelExporterConfig conf;
    private final TripExcelWriter writer;

    public void exportTrips(OutputStream outputStream, TripFilterParams filterParams) throws IOException {
        Workbook workbook = new Workbook(outputStream, "Trips", "1.0");

        try {
            Worksheet worksheet = workbook.newWorksheet(conf.getSheetPrefix() + "1");
            writer.writeHeaders(worksheet);

            Iterator<TripDto> iterator = dataProvider.provide(filterParams);

            int row = 1;
            int pageCount = 1;
            while (iterator.hasNext()) {
                if (row > conf.getMaxRowsPerSheet()) {
                    worksheet.finish();
                    outputStream.flush();
                    pageCount++;
                    worksheet = workbook.newWorksheet(conf.getSheetPrefix() + pageCount);
                    writer.writeHeaders(worksheet);
                    row = 1;
                }
                writer.writeRow(worksheet, row++, iterator.next());
                if (row % conf.getBatchSize() == 0) {
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            throw new ExportException("Error while exporting to excel", e);
        } finally {
            workbook.finish();
            outputStream.flush();
        }
    }
}