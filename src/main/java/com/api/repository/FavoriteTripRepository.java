package com.api.repository;

import com.api.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteTripRepository extends JpaRepository<FavoriteTrip, Long> {

    @Query("SELECT t FROM Trip t JOIN t.favoriteTrip ft")
    List<Trip> getFavouriteTrips();

}
