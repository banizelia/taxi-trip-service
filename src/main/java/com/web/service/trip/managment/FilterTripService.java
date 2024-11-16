package com.web.service.trip.managment;

import com.web.mapper.TripMapper;
import com.web.model.Trip;
import com.web.model.dto.TripDto;
import com.web.model.reflection.ColumnAnnotatedFields;
import com.web.repository.TripsRepository;
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
}
