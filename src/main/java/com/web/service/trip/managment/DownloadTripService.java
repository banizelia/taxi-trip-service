package com.web.service.trip.managment;

import com.web.export.TripExcelExporterFastExcel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.io.IOException;

@Service
@AllArgsConstructor
public class DownloadTripService {
    private TripExcelExporterFastExcel tripExcelExporterFastExcel;

    @Transactional(readOnly = true)
    public StreamingResponseBody execute() {
        return out -> {
            try (out) {
                tripExcelExporterFastExcel.tripsToExcelStream(out);
            } catch (IOException e) {
                throw new IllegalStateException("Error exporting data to Excel: " + e.getMessage(), e);
            }
        };
    }
}
