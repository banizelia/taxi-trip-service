package com.web.service.favorite;

import com.web.model.dto.TripDto;
import com.web.service.favorite.managment.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for managing favorite trips.
 * This class provides methods for adding, removing, retrieving, and reordering favorite trips.
 */
@Service
@AllArgsConstructor
public class FavoriteTripService {
    private GetFavoriteTripService getFavoriteTripService;
    private SaveFavoriteTripService saveFavoriteTripService;
    private DeleteFavoriteTripService deleteFavoriteTripService;
    private DragAndDropFavoriteTripService dragAndDropFavoriteTripService;
    private GetFavoriteTripPageService getFavoriteTripPageService;

    public Page<TripDto> getFavouriteTripsPage(Integer page, Integer size, String sort, String direction) {
        return getFavoriteTripPageService.execute(page, size, sort, direction);
    }

    @Deprecated(forRemoval = true)
    public List<TripDto> getFavouriteTrips() {
        return getFavoriteTripService.execute();
    }

    public void saveToFavourite(Long tripId) {
        saveFavoriteTripService.execute(tripId);
    }

    public void deleteFromFavourite(Long tripId) {
        deleteFavoriteTripService.execute(tripId);
    }

    public void dragAndDrop(Long tripId, Long newPosition) {
        dragAndDropFavoriteTripService.execute(tripId, newPosition);
    }
}