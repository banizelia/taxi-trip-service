package com.web.service.favorite;

import com.web.model.dto.TripDto;
import com.web.service.favorite.managment.DeleteFavoriteTripService;
import com.web.service.favorite.managment.GetFavoriteTripService;
import com.web.service.favorite.managment.SaveFavoriteTripService;
import com.web.service.favorite.managment.DragAndDropFavoriteTripService;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service class for managing favorite trips.
 * This class provides methods for adding, removing, retrieving, and reordering favorite trips.
 */
@AllArgsConstructor
@Service
public class FavoriteTripService {
    private GetFavoriteTripService getFavoriteTripService;
    private SaveFavoriteTripService saveFavoriteTripService;
    private DeleteFavoriteTripService deleteFavoriteTripService;
    private DragAndDropFavoriteTripService dragAndDropFavoriteTripService;

    public List<TripDto> getFavouriteTrips() {
        return getFavoriteTripService.execute();
    }

    public void saveToFavourite(Long tripId) throws BadRequestException {
        saveFavoriteTripService.execute(tripId);
    }

    public void deleteFromFavourite(Long tripId) {
        deleteFavoriteTripService.execute(tripId);
    }

    public void dragAndDrop(Long tripId, Long newPosition) throws BadRequestException {
        dragAndDropFavoriteTripService.execute(tripId, newPosition);
    }
}