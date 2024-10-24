package com.web.service;

import com.web.exceptions.TripNotFound;
import com.web.model.FavoriteTrip;
import com.web.model.Trip;
import com.web.model.dto.TripDto;
import com.web.model.mapper.TripMapper;
import com.web.repository.FavoriteTripRepository;
import com.web.repository.TripsRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service class for managing favorite trips.
 * This class provides methods for adding, removing, retrieving, and reordering favorite trips.
 */
@AllArgsConstructor
@Service
public class FavoriteTripService {
    private FavoriteTripRepository favoriteTripRepository;
    private TripsRepository tripsRepository;

    /**
     * Retrieves all favorite trips.
     *
     * @return ResponseEntity containing a list of favorite trips.
     */
    public List<TripDto> getFavouriteTrips() {
        List<Trip> favoriteTrips = favoriteTripRepository.getFavouriteTrips();
        return favoriteTrips.stream().map(TripMapper.INSTANCE::tripToTripDto).toList();
    }

    /**
     * Adds a trip to favorites.
     *
     * @param tripId The ID of the trip to be added to favorites.
     * @return ResponseEntity with a status message.
     */
    public void saveToFavourite(Long tripId) throws BadRequestException {
        if (favoriteTripRepository.findByTripId(tripId).isPresent()) {
            throw new BadRequestException("Trip already in the table");
        }

        if (!tripsRepository.existsById(tripId)) {
            throw new BadRequestException("Such trip doesn't exist");
        }

        Long maxPosition = favoriteTripRepository.findMaxPosition();

        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setTripId(tripId);
        favoriteTrip.setPosition(maxPosition + 1);

        favoriteTripRepository.save(favoriteTrip);
    }

    /**
     * Removes a trip from favorites.
     *
     * @param tripId The ID of the trip to be removed from favorites.
     * @return ResponseEntity with a status message.
     */
    public void deleteFromFavourite(Long tripId) {
        Optional<FavoriteTrip> favoriteTripOptional = favoriteTripRepository.findByTripId(tripId);

        if (favoriteTripOptional.isEmpty()) {
            throw new TripNotFound("Trip not found");
        }

        FavoriteTrip favoriteTrip = favoriteTripOptional.get();
        Long position = favoriteTrip.getPosition();

        favoriteTripRepository.delete(favoriteTrip);
        favoriteTripRepository.decrementPositionsAfter(position);
    }

    /**
     * Changes the position of a favorite trip in the list.
     *
     * @param tripId The ID of the trip to be moved.
     * @param newPosition The new position for the trip in the favorites list.
     * @return ResponseEntity with a status message.
     */
    @Transactional
    public void dragAndDrop(Long tripId, Long newPosition) throws BadRequestException {
        Optional<FavoriteTrip> favoriteTripOptional = favoriteTripRepository.findByTripId(tripId);

        if (favoriteTripOptional.isEmpty()) {
            throw new TripNotFound("Trip not found");
        }

        FavoriteTrip favoriteTrip = favoriteTripOptional.get();
        Long oldPosition = favoriteTrip.getPosition();

        if (newPosition.equals(oldPosition)) {
            return; // Позиция не меняется, не выбрасываем исключение.
        }

        Long maxPosition = favoriteTripRepository.findMaxPosition();

        if (newPosition > maxPosition) {
            throw new BadRequestException("New position is out of bounds.");
        }

        if (newPosition < oldPosition) {
            favoriteTripRepository.incrementPositions(newPosition, oldPosition);
        } else {
            favoriteTripRepository.decrementPositions(oldPosition, newPosition);
        }

        favoriteTrip.setPosition(newPosition);
        favoriteTripRepository.save(favoriteTrip);
    }

    /**
     * Validates and repairs the positions of favorite trips in the list.
     *
     * @return ResponseEntity with a status message.
     */
    @Transactional
    public void validatePositions() {
        List<FavoriteTrip> allFavorites = favoriteTripRepository.findAllByOrderByPositionAsc();
        AtomicLong expectedPosition = new AtomicLong(1);

        allFavorites.stream()
                .forEach(trip -> {
                    if (trip.getPosition() != expectedPosition.get()) {
                        trip.setPosition(expectedPosition.get());
                        favoriteTripRepository.save(trip);
                    }
                    expectedPosition.incrementAndGet();
                });
    }
}