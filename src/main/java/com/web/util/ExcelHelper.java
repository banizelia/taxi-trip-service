package com.web.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import com.web.model.Trip;
import com.web.repository.TripsRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ExcelHelper {
    private static final String[] HEADERS = {"id",
            "vendor_id",
            "pickup_datetime",
            "dropoff_datetime",
            "passenger_count",
            "trip_distance",
            "rate_code_id",
            "store_and_fwd_flag",
            "pickup_location_id",
            "dropoff_location_id",
            "payment_type",
            "fare_amount",
            "extra",
            "mta_tax",
            "tip_amount",
            "tolls_amount",
            "improvement_surcharge",
            "total_amount",
            "congestion_surcharge",
            "airport_fee",
            "pickup_date" };
    private static final Logger logger = LoggerFactory.getLogger(ExcelHelper.class);
    private static final String SHEET = "trips_";
    private static final int MAX_ROWS_PER_SHEET = 1_000_000; // 1_048_576 is the maximum number of lines for xlsx sheet
    private static final int BATCH_SIZE = 100_000;


    public static ByteArrayInputStream tripsToExcel(TripsRepository tripsRepository, Integer listsLimit) {
        Long start = System.nanoTime()/1_000_000_000;

        try (SXSSFWorkbook workbook = createWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            int sheetCount = 1;
            int rowIdx = 1;

            Sheet sheet = workbook.createSheet(SHEET + sheetCount);
            createHeader(sheet);

            Pageable pageable = PageRequest.of(0, BATCH_SIZE, Sort.by(Sort.Direction.ASC, "id"));

            List<Trip> currentBatch;
            do {
                currentBatch = tripsRepository.findAll(pageable).getContent();

                for (Trip trip : currentBatch) {

                    if (rowIdx >= MAX_ROWS_PER_SHEET) {
                        sheetCount++;
                        sheet = workbook.createSheet(SHEET + sheetCount);
                        createHeader(sheet);
                        rowIdx = 1;
                    }

                    createRow(sheet, rowIdx, trip);
                    rowIdx++;
                }

                Long now = System.nanoTime()/1_000_000_000;
                logger.info("Сompleted page № " + pageable.getPageNumber() + " in " + (now - start) + " seconds"); // for test only
                start = now;

                pageable = pageable.next();

            } while (!currentBatch.isEmpty() && sheetCount < listsLimit);

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel file: " + e.getMessage(), e);
        }
    }

    static SXSSFWorkbook createWorkbook() {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100) {
            public void close() throws IOException {
                try {
                    dispose();
                } catch (Exception e) {
                    throw e;
                }
                super.close();
            }
        };
        workbook.setCompressTempFiles(true);
        return workbook;
    }

    private static void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < HEADERS.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(HEADERS[col]);
        }
    }

    private static void createRow(Sheet sheet, int rowIdx, Trip trip) {
        Row row = sheet.createRow(rowIdx);

        row.createCell(0).setCellValue(trip.getId());
        row.createCell(1).setCellValue(trip.getVendorId());
        row.createCell(2).setCellValue(trip.getPickupDatetime().toString());
        row.createCell(3).setCellValue(trip.getDropoffDatetime().toString());
        row.createCell(4).setCellValue(trip.getPassengerCount());
        row.createCell(5).setCellValue(trip.getTripDistance());
        row.createCell(6).setCellValue(trip.getRateCodeId());
        row.createCell(7).setCellValue(trip.getStoreAndFwdFlag());
        row.createCell(8).setCellValue(trip.getPickupLocationId());
        row.createCell(9).setCellValue(trip.getDropoffLocationId());
        row.createCell(10).setCellValue(trip.getPaymentType());
        row.createCell(11).setCellValue(trip.getFareAmount());
        row.createCell(12).setCellValue(trip.getExtra());
        row.createCell(13).setCellValue(trip.getMtaTax());
        row.createCell(14).setCellValue(trip.getTipAmount());
        row.createCell(15).setCellValue(trip.getTollsAmount());
        row.createCell(16).setCellValue(trip.getImprovementSurcharge());
        row.createCell(17).setCellValue(trip.getTotalAmount());
        row.createCell(18).setCellValue(trip.getCongestionSurcharge());
        row.createCell(19).setCellValue(trip.getAirportFee());
        row.createCell(20).setCellValue(trip.getPickupDate().toString());
    }
}