package com.web.service.trip.managment;

import com.web.export.TripExcelExporterFastExcel;
import com.web.repository.TripsRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@AllArgsConstructor
public class DownloadTripService {
    private TripExcelExporterFastExcel tripExcelExporterFastExcel;
    private TripsRepository tripsRepository;

    @Transactional(readOnly = true)
    public Resource execute() {

        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = tripExcelExporterFastExcel.tripsToExcel(new ByteArrayOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        InputStreamResource file = new InputStreamResource(byteArrayInputStream);

        return file;
    }
}
