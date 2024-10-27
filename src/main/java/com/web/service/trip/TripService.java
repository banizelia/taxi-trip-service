package com.web.service.trip;

import com.web.export.TripExcelExporterFastExcel;
import com.web.model.dto.TripDto;
import com.web.repository.TripsRepository;
import com.web.service.trip.managment.DownloadTripService;
import com.web.service.trip.managment.FilterTripService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * Service for managing trip data.
 */
@AllArgsConstructor
@Service
public class TripService {
    private FilterTripService filterTripService;
    private DownloadTripService downloadTripService;
    private TripExcelExporterFastExcel tripExcelExporterFastExcel;
    private TripsRepository tripsRepository;


    public Page<TripDto> filter(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                Double minWindSpeed, Double maxWindSpeed,
                                Integer page, Integer size,
                                String sortBy, String direction) {
        return filterTripService.execute(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, page, size, sortBy, direction);
    }


    @Transactional(readOnly = true)
    public Resource download() {
        return downloadTripService.execute();
    }
}