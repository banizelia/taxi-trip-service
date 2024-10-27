package com.web.service.trip;

import com.web.model.dto.TripDto;
import com.web.service.trip.managment.DownloadTripService;
import com.web.service.trip.managment.FilterTripService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import java.time.LocalDateTime;

/**
 * Service for managing trip data.
 */
@AllArgsConstructor
@Service
public class TripService {
    private FilterTripService filterTripService;
    private DownloadTripService downloadTripService;

    public Page<TripDto> filter(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                Double minWindSpeed, Double maxWindSpeed,
                                Integer page, Integer size,
                                String sortBy, String direction) {
        return filterTripService.execute(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, page, size, sortBy, direction);
    }

    public StreamingResponseBody download() {
        return downloadTripService.execute();
    }
}