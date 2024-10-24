package com.web.model.mapper;

import com.web.model.Trip;
import com.web.model.dto.TripDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TripMapper {
    TripMapper INSTANCE = Mappers.getMapper(TripMapper.class);
    TripDto tripToTripDto(Trip trip);
}