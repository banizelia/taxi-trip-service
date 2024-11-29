package com.web.trip.service;

import com.web.common.exception.export.ExportException;
import com.web.common.export.TripExcelExporter;
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
    public StreamingResponseBody execute() {
        return out -> {
            try (out) {
                tripExcelExporter.tripsToExcelStream(out);
            } catch (IOException e) {
                throw new ExportException("Error exporting data to Excel", e);
            }
        };
    }
}
