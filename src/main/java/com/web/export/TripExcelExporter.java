package com.web.export;

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

/**
 * Utility class for exporting trip data to Excel file.
 */
public class TripExcelExporter {
    // Column headers for the Excel file
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
    private static final Logger logger = LoggerFactory.getLogger(TripExcelExporter.class);

    // Name of the Excel sheet
    private static final String SHEET = "trips_";

    // Maximum number of rows per Excel sheet
    private static final int MAX_ROWS_PER_SHEET = 1_000_000;

    // Batch size for database queries
    private static final int BATCH_SIZE = 100_000;

    /**
     * Exports trip data from the database to an Excel file.
     *
     * @param tripsRepository repository for retrieving trip data
     * @param sheetLimit maximum number of sheets in the Excel file
     * @return InputStream with the contents of the Excel file
     */
    public static ByteArrayInputStream tripsToExcel(TripsRepository tripsRepository, Integer sheetLimit) {
        Long start = System.nanoTime()/1_000_000_000;

        try (SXSSFWorkbook workbook = createWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            int sheetCount = 1;
            int rowIdx = 1;

            // Create the first sheet and add the header
            Sheet sheet = workbook.createSheet(SHEET + sheetCount);
            createHeader(sheet);

            // Paginated query of data from the database with sorting by id
            Pageable pageable = PageRequest.of(0, BATCH_SIZE, Sort.by(Sort.Direction.ASC, "id"));
            List<Trip> currentBatch;

            // Loop through data pages
            do {
                currentBatch = tripsRepository.findAll(pageable).getContent();

                // Fill rows with trip data
                for (Trip trip : currentBatch) {
                    // If we've reached the maximum rows per sheet, create a new sheet
                    if (rowIdx >= MAX_ROWS_PER_SHEET) {
                        sheetCount++;
                        sheet = workbook.createSheet(SHEET + sheetCount);
                        createHeader(sheet);
                        rowIdx = 1;
                    }

                    createRow(sheet, rowIdx, trip);
                    rowIdx++;
                }

                // Log information about the current page execution
                Long now = System.nanoTime()/1_000_000_000;
                logger.info("Completed page # " + pageable.getPageNumber() + " in " + (now - start) + " seconds");
                start = now;

                pageable = pageable.next();

            } while (!currentBatch.isEmpty() && sheetCount < sheetLimit);

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error exporting data to Excel file: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a new Excel workbook with support for temporary files for large data sets.
     *
     * @return SXSSFWorkbook object for working with Excel
     */
    static SXSSFWorkbook createWorkbook() {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100) {
            public void close() throws IOException {
                try {
                    dispose(); // Release temporary resources
                } catch (Exception e) {
                    throw e;
                }
                super.close();
            }
        };
        workbook.setCompressTempFiles(true); // Enable compression of temporary files
        return workbook;
    }

    /**
     * Creates a header for the Excel sheet.
     *
     * @param sheet the sheet where the header is created
     */
    private static void createHeader(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        for (int col = 0; col < HEADERS.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(HEADERS[col]);
        }
    }

    /**
     * Creates a row with trip data in the Excel sheet.
     *
     * @param sheet the sheet where the row is created
     * @param rowIdx the index of the row
     * @param trip the Trip object with data
     */
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