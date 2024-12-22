package com.banizelia.taxi.util.export.excel;

import com.banizelia.taxi.config.ExcelExporterConfig;
import com.banizelia.taxi.error.export.ExportException;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.util.export.TripDataProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripExcelExporter {
    private final TripDataProvider dataProvider;
    private final ExcelExporterConfig conf;
    private final TripExcelWriter writer;

    @PersistenceContext
    private EntityManager entityManager;

    public void exportTrips(OutputStream outputStream, TripFilterParams filterParams) throws IOException {
        entityManager.clear();

        Workbook workbook = new Workbook(outputStream, "Trips", "1.0");

        try {
            AtomicInteger pageCount = new AtomicInteger(1);
            AtomicInteger rowCount = new AtomicInteger(1);

            Worksheet worksheet = workbook.newWorksheet(conf.getSheetPrefix() + pageCount.get());
            AtomicReference<Worksheet> currentWorksheet = new AtomicReference<>(worksheet);

            writer.writeHeaders(currentWorksheet.get());

            dataProvider.provide(filterParams).forEach(tripDto -> {

                try {
                    writer.writeRow(currentWorksheet.get(), rowCount.getAndIncrement(), tripDto);

                    if (rowCount.get() > conf.getMaxRowsPerSheet()) {
                        currentWorksheet.get().finish();

                        flush(outputStream);

                        pageCount.incrementAndGet();
                        currentWorksheet.set(workbook.newWorksheet(conf.getSheetPrefix() + pageCount.get()));
                        writer.writeHeaders(currentWorksheet.get());
                        rowCount.set(1);
                    }

                    if (rowCount.get() % conf.getBatchSize() == 0) {
                        flush(outputStream);

                        log.info("Batch written, row {}, page {}.", rowCount.get(), pageCount.get());
                    }

                } catch (IOException e) {
                    log.error("Error while writing row {} at page {}: {}", rowCount.get(), pageCount.get(), e.getMessage(), e);
                    throw new ExportException("Error while exporting to excel", e);
                }
            });
        } finally {
            workbook.finish();
            flush(outputStream);
        }
    }

    private void flush(OutputStream outputStream) throws IOException {
        outputStream.flush();
        entityManager.clear();
    }
}