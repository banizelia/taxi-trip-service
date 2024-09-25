package com.api.excelHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import com.api.model.Trip;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"id",
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

    static String SHEET = "Trips";

    public static ByteArrayInputStream tripsToExcel(List<Trip> trips) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            for (Trip trip : trips) {
                Row row = sheet.createRow(rowIdx);
                rowIdx++;

                row.createCell(0).setCellValue(trip.getId());
                row.createCell(1).setCellValue(trip.getVendorId());
                row.createCell(2).setCellValue(trip.getPickupDate());
                row.createCell(3).setCellValue(trip.getDropoffDatetime());
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
                row.createCell(20).setCellValue(trip.getPickupDate());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}