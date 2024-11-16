package com.web.service.favorite.managment;

import com.web.mapper.TripMapper;
import com.web.model.Trip;
import com.web.model.dto.TripDto;
import com.web.repository.FavoriteTripRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class GetFavoriteTripPageService {
    private FavoriteTripRepository favoriteTripRepository;

    public Page<TripDto> execute(Integer page, Integer size, String sortBy, String direction) {
        validateSortParams(direction, sortBy);

        Sort sort = Sort.by(Sort.Direction.fromString(direction), "favoriteTrip." +sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Trip> trips = favoriteTripRepository.findAllWithPagination(pageable);
        return trips.map(TripMapper.INSTANCE::tripToTripDto);
    }

    private void validateSortParams(String direction, String sortBy) {
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        Set<String> allowedFields = new HashSet<>(Arrays.asList("position", "id", "tripId"));
        if (!allowedFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }
    }
}
