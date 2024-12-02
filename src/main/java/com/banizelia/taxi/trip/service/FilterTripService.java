package com.banizelia.taxi.trip.service;

import com.banizelia.taxi.trip.mapper.TripMapper;
import com.banizelia.taxi.trip.model.Trip;
import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.trip.model.TripFilterParams;
import com.banizelia.taxi.trip.repository.TripsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilterTripService {
    private final TripsRepository tripsRepository;

    public Page<TripDto> execute(TripFilterParams params, Pageable pageable) {
        Page<Trip> trips = tripsRepository.filter(
                params.getIsFavorite(),
                params.getStartDateTime(),
                params.getEndDateTime(),
                params.getMinWindSpeed(),
                params.getMaxWindSpeed(),
                pageable);

        return trips.map(TripMapper.INSTANCE::tripToTripDto);
    }
}