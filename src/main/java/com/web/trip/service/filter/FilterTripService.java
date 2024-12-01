package com.web.trip.service.filter;

import com.web.trip.mapper.TripMapper;
import com.web.trip.model.Trip;
import com.web.trip.model.TripDto;
import com.web.trip.model.TripFilterParams;
import com.web.trip.repository.TripsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilterTripService {
    private final TripsRepository tripsRepository;

    public Page<TripDto> execute(TripFilterParams params) {
        Page<Trip> trips = tripsRepository.filter(
                params.startDateTime(),
                params.endDateTime(),
                params.minWindSpeed(),
                params.maxWindSpeed(),
                params.getPageable()
        );

        return trips.map(TripMapper.INSTANCE::tripToTripDto);
    }
}