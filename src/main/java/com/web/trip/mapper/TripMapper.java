package com.web.trip.mapper;

import com.web.trip.model.Trip;
import com.web.trip.model.TripDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TripMapper {
    TripMapper INSTANCE = Mappers.getMapper(TripMapper.class);
    TripDto tripToTripDto(Trip trip);
}