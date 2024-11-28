package com.web.common.export;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.web.common.exception.export.ExportException;
import com.web.trip.mapper.TripMapper;
import com.web.trip.model.Trip;
import com.web.trip.model.TripDto;
import com.web.trip.repository.TripsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripExcelExporter {
    private final TripsRepository tripsRepository;
    private final ExcelExporterConf conf;

    private final List<FieldExtractor> fieldExtractors = List.of(
            new FieldExtractor("ID", TripDto::getId),
            new FieldExtractor("Vendor ID", TripDto::getVendorId),
            new FieldExtractor("Pickup Datetime", TripDto::getPickupDatetime),
            new FieldExtractor("Dropoff Datetime", TripDto::getDropoffDatetime),
            new FieldExtractor("Passenger Count", TripDto::getPassengerCount),
            new FieldExtractor("Trip Distance", TripDto::getTripDistance),
            new FieldExtractor("Rate Code ID", TripDto::getRateCodeId),
            new FieldExtractor("Store and Forward Flag", TripDto::getStoreAndFwdFlag),
            new FieldExtractor("Pickup Location ID", TripDto::getPickupLocationId),
            new FieldExtractor("Dropoff Location ID", TripDto::getDropoffLocationId),
            new FieldExtractor("Payment Type", TripDto::getPaymentType),
            new FieldExtractor("Fare Amount", TripDto::getFareAmount),
            new FieldExtractor("Extra", TripDto::getExtra),
            new FieldExtractor("MTA Tax", TripDto::getMtaTax),
            new FieldExtractor("Tip Amount", TripDto::getTipAmount),
            new FieldExtractor("Tolls Amount", TripDto::getTollsAmount),
            new FieldExtractor("Improvement Surcharge", TripDto::getImprovementSurcharge),
            new FieldExtractor("Total Amount", TripDto::getTotalAmount),
            new FieldExtractor("Congestion Surcharge", TripDto::getCongestionSurcharge),
            new FieldExtractor("Airport Fee", TripDto::getAirportFee)
    );

    public void tripsToExcelStream(OutputStream outputStream) throws IOException {
        int batchCounter = 1;
        StopWatch watch = new StopWatch();
        watch.start();
        long lastSplitTime = watch.getTime();

        Workbook workbook = null;
        try {
            workbook = new Workbook(outputStream, "Trips Export", "1.0");
            int totalPages = 0;

            Worksheet currentSheet = createNewSheet(workbook, 1);
            int currentRow = 1;

            Iterator<Trip> tripIterator = tripsRepository.findAllStream().iterator();

            while (tripIterator.hasNext()) {
                TripDto trip = TripMapper.INSTANCE.tripToTripDto(tripIterator.next());

                if (currentRow > conf.getMaxRowsPerSheet()) {
                    currentSheet.finish();
                    outputStream.flush();

                    currentSheet = createNewSheet(workbook, ++totalPages);
                    currentRow = 1;
                }

                writeRow(currentSheet, currentRow++, trip);

                if (currentRow % conf.getBatchSize() == 0) {
                    Runtime rt = Runtime.getRuntime();
                    long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
                    long currentTime = watch.getTime();
                    log.info("batch # {} completed in {} seconds, memory usage: {}", batchCounter++,
                            (currentTime - lastSplitTime) / 1000.0, usedMB);
                    lastSplitTime = currentTime;
                }
            }
        } catch (IOException e) {
            log.error("Ошибка во время экспорта в xlsx: {}", e.getMessage());
            throw new ExportException("Ошибка во время экспорта в xlsx", e);
        } finally {
            watch.stop();
            log.info("Total export time: {} seconds", watch.getTime(TimeUnit.SECONDS));
            workbook.finish();
            outputStream.flush();
        }
    }

    private Worksheet createNewSheet(Workbook workbook, int sheetNumber) {
        Worksheet sheet = workbook.newWorksheet(conf.getSheetPrefix() + sheetNumber);
        writeHeaders(sheet);
        return sheet;
    }

    private void writeHeaders(Worksheet sheet) {
        for (int i = 0; i < fieldExtractors.size(); i++) {
            sheet.value(0, i, fieldExtractors.get(i).name());
        }
    }

    private void writeRow(Worksheet sheet, int rowIdx, TripDto trip) {
        for (int i = 0; i < fieldExtractors.size(); i++) {
            Object value = fieldExtractors.get(i).extractor().apply(trip);
            if (value != null) {
                switch (value) {
                    case LocalDateTime localDateTime -> sheet.value(rowIdx, i, localDateTime.toString());

                    case Number number -> sheet.value(rowIdx, i, number.doubleValue());

                    default -> sheet.value(rowIdx, i, value.toString());
                }
            }
        }
    }
}