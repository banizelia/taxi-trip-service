package com.web.trip.service.filter;

import com.web.trip.mapper.TripMapper;
import com.web.trip.model.Trip;
import com.web.trip.model.TripDto;
import com.web.trip.model.TripFilterParams;
import com.web.trip.repository.TripsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilterTripService {
    private final TripsRepository tripsRepository;

    public Page<TripDto> execute(TripFilterParams params) {
        Sort sort = Sort.by(Sort.Direction.fromString(params.direction()), params.sort());
        Pageable pageable = PageRequest.of(params.page(), params.size(), sort);

        Page<Trip> trips = tripsRepository.filter(
                params.startDateTime(),
                params.endDateTime(),
                params.minWindSpeed(),
                params.maxWindSpeed(),
                pageable
        );

        return trips.map(TripMapper.INSTANCE::tripToTripDto);
    }
}