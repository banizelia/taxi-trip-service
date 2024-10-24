package com.web.service;

import com.web.export.TripExcelExporterFastExcel;
import com.web.model.Trip;
import com.web.model.dto.TripDto;
import com.web.model.mapper.TripMapper;
import com.web.model.reflection.ColumnAnnotatedFields;
import com.web.repository.TripsRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Service for managing trip data.
 */
@AllArgsConstructor
@Service
public class TripService {
    private TripsRepository tripsRepository;

    /**
     * Filters and retrieves trips based on various criteria.
     *
     * @param startDateTime Start date and time for filtering trips.
     * @param endDateTime   End date and time for filtering trips.
     * @param minWindSpeed  Minimum wind speed for filtering trips.
     * @param maxWindSpeed  Maximum wind speed for filtering trips.
     * @param direction     Sorting direction (asc or desc).
     * @param sortBy        Field to sort by.
     * @param page          Page number for pagination.
     * @param pageSize      Page size for pagination.
     * @return ResponseEntity containing a list of filtered trips.
     * @throws IllegalArgumentException if input parameters are invalid.
     */
    public Page<TripDto> filter(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                                Double minWindSpeed, Double maxWindSpeed,
                                                                Integer page, Integer size,
                                                                String sortBy, String direction) {
        validateFilterParams(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, direction, sortBy);

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Trip> trips = tripsRepository.filter(startDateTime, endDateTime, minWindSpeed, maxWindSpeed, pageable);

        return trips.map(TripMapper.INSTANCE::tripToTripDto);
    }

    private void validateFilterParams(LocalDateTime startDateTime, LocalDateTime endDateTime,
                                      Double minWindSpeed, Double maxWindSpeed,
                                      String direction, String sortBy) {
        if (endDateTime.isBefore(startDateTime)){
            throw new IllegalArgumentException("endDateTime is before startDateTime, startDateTime = " + startDateTime + " endDateTime = " + endDateTime);
        }

        if (maxWindSpeed <= minWindSpeed){
            throw new IllegalArgumentException("maxWindSpeed is smaller or equal to minWindSpeed, maxWindSpeed = " + maxWindSpeed + " minWindSpeed = " + minWindSpeed);
        }

        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")){
            throw new IllegalArgumentException("Invalid direction : " + direction);
        }

        Set<String> weatherAndTripAllowedField = new HashSet<>();
        weatherAndTripAllowedField.addAll(ColumnAnnotatedFields.getTripFields());
        weatherAndTripAllowedField.addAll(ColumnAnnotatedFields.getWeatherFields());

        if (!weatherAndTripAllowedField.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
    }

    /**
     * Downloads trip data as an Excel file.
     *
     * @param sheetLimit The maximum number of sheets in the Excel file.
     * @return ResponseEntity containing the Excel file as a Resource.
     * @throws IllegalArgumentException if sheetLimit is less than one.
     */
    public ResponseEntity<Resource> download() {
        String filename = "trips.xlsx";
        InputStreamResource file = new InputStreamResource(TripExcelExporterFastExcel.tripsToExcel(tripsRepository));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}