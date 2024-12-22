package com.banizelia.taxi.trip.service;

import com.banizelia.taxi.error.export.ExportException;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.util.export.excel.TripExcelExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DownloadTripService {
    private final TripExcelExporter tripExcelExporter;

    @Transactional(readOnly = true)
    public StreamingResponseBody execute(TripFilterParams filterParams) {
        return out -> {
            try (out) {
                tripExcelExporter.exportTrips(out, filterParams);
            } catch (IOException e) {
                throw new ExportException("Error exporting data to Excel", e);
            }
        };
    }
}
