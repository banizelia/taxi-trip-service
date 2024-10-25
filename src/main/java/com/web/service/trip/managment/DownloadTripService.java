package com.web.service.trip.managment;

import com.web.export.TripExcelExporterFastExcel;
import com.web.repository.TripsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.io.IOException;

@Service
@AllArgsConstructor
public class DownloadTripService {
    private TripExcelExporterFastExcel tripExcelExporterFastExcel;
    private TripsRepository tripsRepository;

    @Transactional(readOnly = true)
    public StreamingResponseBody execute() {
        return out -> {
            try (out) {
                tripExcelExporterFastExcel.tripsToExcelStream(tripsRepository, out);
            } catch (IOException e) {
                throw new RuntimeException("Error exporting data to Excel: " + e.getMessage(), e);
            }
        };
    }
}
