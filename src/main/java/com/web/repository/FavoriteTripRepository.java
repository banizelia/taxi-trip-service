package com.web.repository;

import com.web.model.FavoriteTrip;
import com.web.model.Trip;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteTripRepository extends JpaRepository<FavoriteTrip, Long> {

    @Query("SELECT t FROM Trip t JOIN FavoriteTrip ft ON t.id = ft.tripId ORDER BY ft.position")
    List<Trip> getFavouriteTrips();

    @Query("SELECT COALESCE(MAX(ft.position), 0) FROM FavoriteTrip ft")
    Long findMaxPosition();

    @Modifying
    @Transactional
    @Query("UPDATE FavoriteTrip ft SET ft.position = ft.position + 1 WHERE ft.position >= :newPosition AND ft.position < :oldPosition")
    void incrementPositions(@Param("newPosition") Long newPosition,
                            @Param("oldPosition") Long oldPosition);

    @Modifying
    @Transactional
    @Query("UPDATE FavoriteTrip ft SET ft.position = ft.position - 1 WHERE ft.position > :oldPosition AND ft.position <= :newPosition")
    void decrementPositions(@Param("oldPosition") Long oldPosition,
                            @Param("newPosition") Long newPosition);

}
