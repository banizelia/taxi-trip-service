package com.web.export;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import com.web.mapper.TripMapper;
import com.web.model.Trip;
import com.web.model.dto.TripDto;
import com.web.repository.TripsRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
@Transactional(readOnly = true)
public class TripExcelExporterFastExcel {
    private TripsRepository tripsRepository;

    private static final Logger logger = LoggerFactory.getLogger(TripExcelExporterFastExcel.class);
    private static final String[] HEADERS = {"id", "vendor_id", "pickup_datetime",
            "dropoff_datetime", "passenger_count", "trip_distance",
            "rate_code_id", "store_and_fwd_flag", "pickup_location_id",
            "dropoff_location_id", "payment_type", "fare_amount",
            "extra", "mta_tax", "tip_amount", "tolls_amount",
            "improvement_surcharge", "total_amount", "congestion_surcharge",
            "airport_fee"};
    private static final String SHEET_PREFIX = "trips_";
    private static final int MAX_ROWS_PER_SHEET = 1_000_000;
    private static final int BATCH_SIZE = 100_000;

    public void tripsToExcelStream(OutputStream outputStream) throws IOException {
        // Для логгирования
        int batchCounter = 1;
        StopWatch watch = new StopWatch();
        watch.start();
        long lastSplitTime = watch.getTime();

        Workbook workbook = new Workbook(outputStream, "Trips Export", "1.0");
//        workbook.setCompressionLevel(9);
        int totalPages = 0;
        int currentRow = 1;
        Worksheet currentSheet = createNewSheet(workbook, 1);

        Iterator<Trip> tripIterator = tripsRepository.findAllStream(BATCH_SIZE).iterator();

        while (tripIterator.hasNext()) {
            TripDto trip = TripMapper.INSTANCE.tripToTripDto(tripIterator.next());

            if (currentRow > MAX_ROWS_PER_SHEET) {
                currentSheet.flush();
                currentSheet.finish();
                outputStream.flush();

                currentSheet = createNewSheet(workbook, ++totalPages);
                currentRow = 1;
            }

            writeRow(currentSheet, currentRow++, trip);

            // Логгирование прогресса каждый батч
            if (currentRow % BATCH_SIZE == 0) {
                Runtime rt = Runtime.getRuntime();
                long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
                long currentTime = watch.getTime();
                logger.info("batch # {} comleted in {} seconds, memory usage: {}", batchCounter++,(currentTime - lastSplitTime) / 1000.0 , usedMB);
                lastSplitTime = currentTime;
            }
        }

        watch.stop();
        logger.info("Total export time: {} seconds", watch.getTime(TimeUnit.SECONDS));

        workbook.finish();
        outputStream.flush();
    }

    private static Worksheet createNewSheet(Workbook workbook, int sheetNumber) throws IOException {
        Worksheet sheet = workbook.newWorksheet(SHEET_PREFIX + sheetNumber);
        writeHeaders(sheet);
        return sheet;
    }

    private static void writeHeaders(Worksheet sheet){
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.value(0, i, HEADERS[i]);
        }
    }

    private static void writeRow(Worksheet sheet, int rowIdx, TripDto trip) {
        sheet.value(rowIdx, 0, trip.getId());
        sheet.value(rowIdx, 1, trip.getVendorId());
        sheet.value(rowIdx, 2, trip.getPickupDatetime().toString());
        sheet.value(rowIdx, 3, trip.getDropoffDatetime().toString());
        sheet.value(rowIdx, 4, trip.getPassengerCount());
        sheet.value(rowIdx, 5, trip.getTripDistance());
        sheet.value(rowIdx, 6, trip.getRateCodeId());
        sheet.value(rowIdx, 7, trip.getStoreAndFwdFlag());
        sheet.value(rowIdx, 8, trip.getPickupLocationId());
        sheet.value(rowIdx, 9, trip.getDropoffLocationId());
        sheet.value(rowIdx, 10, trip.getPaymentType());
        sheet.value(rowIdx, 11, trip.getFareAmount());
        sheet.value(rowIdx, 12, trip.getExtra());
        sheet.value(rowIdx, 13, trip.getMtaTax());
        sheet.value(rowIdx, 14, trip.getTipAmount());
        sheet.value(rowIdx, 15, trip.getTollsAmount());
        sheet.value(rowIdx, 16, trip.getImprovementSurcharge());
        sheet.value(rowIdx, 17, trip.getTotalAmount());
        sheet.value(rowIdx, 18, trip.getCongestionSurcharge());
        sheet.value(rowIdx, 19, trip.getAirportFee());
    }
}