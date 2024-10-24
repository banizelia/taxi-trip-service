package com.web.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.web.model.Trip;
import com.web.repository.TripsRepository;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

public class TripExcelExporterFastExcel {
    private static final String[] HEADERS = {"id", "vendor_id", "pickup_datetime",
            "dropoff_datetime", "passenger_count", "trip_distance",
            "rate_code_id", "store_and_fwd_flag", "pickup_location_id",
            "dropoff_location_id", "payment_type", "fare_amount",
            "extra", "mta_tax", "tip_amount", "tolls_amount",
            "improvement_surcharge", "total_amount", "congestion_surcharge",
            "airport_fee", "pickup_date"};
    private static final Logger logger = LoggerFactory.getLogger(TripExcelExporterFastExcel.class);
    private static final String SHEET = "trips_";
    private static final int MAX_ROWS_PER_SHEET = 1_000_000;
    private static final int BATCH_SIZE = 100_000;
    private static final StopWatch watch = new StopWatch();

    public static ByteArrayInputStream tripsToExcel(TripsRepository tripsRepository) {
        watch.reset();
        watch.start();
        long lastSplitTime = watch.getTime();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Workbook workbook = new Workbook(out, "Trips Export", "1.0");

            int sheetCount = 1;
            int rowIdx = 1;

            Worksheet sheet = workbook.newWorksheet(SHEET + sheetCount);
            createHeader(sheet);

            Pageable pageable = PageRequest.of(0, BATCH_SIZE, Sort.by(Sort.Direction.ASC, "id"));

            List<Trip> currentBatch = tripsRepository.findAll(pageable).getContent();
            while (!currentBatch.isEmpty() && sheetCount <= 1) {
                for (Trip trip : currentBatch) {
                    if (rowIdx > MAX_ROWS_PER_SHEET) {
                        sheetCount++;
                        sheet = workbook.newWorksheet(SHEET + sheetCount);
                        createHeader(sheet);
                        rowIdx = 1;
                    }
                    createRow(sheet, rowIdx++, trip);
                }

                long currentTime = watch.getTime();
                long splitTime = currentTime - lastSplitTime;
                lastSplitTime = currentTime;
                logger.info("Completed page # {} in {} seconds", pageable.getPageNumber(), splitTime / 1000.0);

                pageable = pageable.next();
                currentBatch = tripsRepository.findAll(pageable).getContent();
            }

            watch.stop();
            logger.info("Total time taken: {} milliseconds", watch.getTime(TimeUnit.MILLISECONDS));

            workbook.finish();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error exporting data to Excel file: " + e.getMessage(), e);
        }
    }

    private static void createHeader(Worksheet sheet) {
        for (int col = 0; col < HEADERS.length; col++) {
            sheet.value(0, col, HEADERS[col]);
        }
    }

    private static void createRow(Worksheet sheet, int rowIdx, Trip trip) {
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
        sheet.value(rowIdx, 20, trip.getPickupDate().toString());
    }
}