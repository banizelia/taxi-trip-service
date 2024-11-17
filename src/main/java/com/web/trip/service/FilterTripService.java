package com.web.trip.service;

import com.web.common.exception.filter.InvalidDateRangeException;
import com.web.common.exception.filter.InvalidSortDirectionException;
import com.web.common.exception.filter.InvalidSortFieldException;
import com.web.common.exception.filter.InvalidWindSpeedRangeException;
import com.web.trip.mapper.TripMapper;
import com.web.trip.model.Trip;
import com.web.trip.model.TripDto;
import com.web.common.export.ColumnAnnotatedFields;
import com.web.trip.repository.TripsRepository;
import com.web.weather.model.WeatherDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class FilterTripService {
    private TripsRepository tripsRepository;

    public Page<TripDto> execute(LocalDateTime startDateTime, LocalDateTime endDateTime,
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
            throw new InvalidDateRangeException(startDateTime, endDateTime);
        }

        if (maxWindSpeed <= minWindSpeed){
            throw new InvalidWindSpeedRangeException(maxWindSpeed, minWindSpeed);
        }

        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")){
            throw new InvalidSortDirectionException(direction);
        }

        Set<String> allowedField = new HashSet<>();
        allowedField.addAll(ColumnAnnotatedFields.getAnnotatedFields(TripDto.class));
        allowedField.addAll(ColumnAnnotatedFields.getAnnotatedFields(WeatherDto.class));

        if (!allowedField.contains(sortBy)) {
            throw new InvalidSortFieldException(sortBy);
        }
    }
}
