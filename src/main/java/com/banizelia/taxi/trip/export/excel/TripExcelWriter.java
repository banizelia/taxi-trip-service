package com.banizelia.taxi.trip.export.excel;

import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.util.extractors.FieldAndFunctionExtractor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class TripExcelWriter {
    private final List<FieldAndFunctionExtractor> extractors = List.of(
            new FieldAndFunctionExtractor("ID", TripDto::getId),
            new FieldAndFunctionExtractor("Favorite", TripDto::getIsFavorite),
            new FieldAndFunctionExtractor("Average Wind Speed", TripDto::getAverageWindSpeed),
            new FieldAndFunctionExtractor("Vendor ID", TripDto::getVendorId),
            new FieldAndFunctionExtractor("Pickup Datetime", TripDto::getPickupDatetime),
            new FieldAndFunctionExtractor("Dropoff Datetime", TripDto::getDropoffDatetime),
            new FieldAndFunctionExtractor("Passenger Count", TripDto::getPassengerCount),
            new FieldAndFunctionExtractor("Trip Distance", TripDto::getTripDistance),
            new FieldAndFunctionExtractor("Rate Code ID", TripDto::getRateCodeId),
            new FieldAndFunctionExtractor("Store and Forward Flag", TripDto::getStoreAndFwdFlag),
            new FieldAndFunctionExtractor("Pickup Location ID", TripDto::getPickupLocationId),
            new FieldAndFunctionExtractor("Dropoff Location ID", TripDto::getDropoffLocationId),
            new FieldAndFunctionExtractor("Payment Type", TripDto::getPaymentType),
            new FieldAndFunctionExtractor("Fare Amount", TripDto::getFareAmount),
            new FieldAndFunctionExtractor("Extra", TripDto::getExtra),
            new FieldAndFunctionExtractor("MTA Tax", TripDto::getMtaTax),
            new FieldAndFunctionExtractor("Tip Amount", TripDto::getTipAmount),
            new FieldAndFunctionExtractor("Tolls Amount", TripDto::getTollsAmount),
            new FieldAndFunctionExtractor("Improvement Surcharge", TripDto::getImprovementSurcharge),
            new FieldAndFunctionExtractor("Total Amount", TripDto::getTotalAmount),
            new FieldAndFunctionExtractor("Congestion Surcharge", TripDto::getCongestionSurcharge),
            new FieldAndFunctionExtractor("Airport Fee", TripDto::getAirportFee)
    );


    public void writeHeaders(Worksheet sheet) {
        for (int i = 0; i < extractors.size(); i++) {
            sheet.value(0, i, extractors.get(i).name());
        }
    }

    public void writeRow(Worksheet sheet, int row, TripDto dto) {
        for (int column = 0; column < extractors.size(); column++) {
            Object val = extractors.get(column).extractor().apply(dto);
            if (val instanceof LocalDateTime dateTime) {
                sheet.value(row, column, dateTime);
                sheet.style(row, column).format("yyyy-MM-dd H:mm:ss").set();
            } else if (val instanceof Number number) {
                sheet.value(row, column, number.doubleValue());
            } else if (val != null) {
                sheet.value(row, column, val.toString());
            }
        }
    }
}