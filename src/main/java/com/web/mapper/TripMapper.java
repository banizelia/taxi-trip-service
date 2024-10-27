package com.web.mapper;

import com.web.model.Trip;
import com.web.model.dto.TripDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TripMapper {
    TripMapper INSTANCE = Mappers.getMapper(TripMapper.class);
    TripDto tripToTripDto(Trip trip);
}