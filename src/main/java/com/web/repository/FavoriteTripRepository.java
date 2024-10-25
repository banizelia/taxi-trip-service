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
import java.util.Optional;

@Repository
public interface FavoriteTripRepository extends JpaRepository<FavoriteTrip, Long> {
    @Query("SELECT t FROM Trip t JOIN FavoriteTrip ft ON t.id = ft.tripId ORDER BY ft.position")
    List<Trip> getFavouriteTrips();

    @Query("SELECT COALESCE(MAX(ft.position), 0) FROM FavoriteTrip ft")
    Long findMaxPosition();

    @Query("SELECT f FROM FavoriteTrip f WHERE f.position > :startPosition AND f.position < :endPosition ORDER BY f.position ASC")
    List<FavoriteTrip> findTripsInPositionRange(Long startPosition, Long endPosition);

    Optional<FavoriteTrip> findByTripId(Long id);

    @Query(value = " SELECT position FROM favorite_trip WHERE trip_id = :trip_id")
    Optional<Long> findPositionByIndex(@Param("trip_id") Long tripId);

    List<FavoriteTrip> findAllByOrderByPositionAsc();

//    @Query("SELECT COUNT(f) FROM FavoriteTrip f")
//    long count();

//    @Query("SELECT COALESCE(MIN(ft.position), 0) FROM FavoriteTrip ft")
//    Long findMinPosition();
//
//    @Query("SELECT MIN(ft.position) FROM FavoriteTrip ft WHERE ft.position > :position")
//    Optional<Long> findNextPosition(Long position);

//    @Deprecated
//    @Modifying
//    @Transactional
//    @Query("UPDATE FavoriteTrip ft SET ft.position = ft.position + 1 WHERE ft.position >= :newPosition AND ft.position < :oldPosition")
//    void incrementPositions(@Param("newPosition") Long newPosition,
//                            @Param("oldPosition") Long oldPosition);
//
//    @Deprecated
//    @Modifying
//    @Transactional
//    @Query("UPDATE FavoriteTrip ft SET ft.position = ft.position - 1 WHERE ft.position > :oldPosition AND ft.position <= :newPosition")
//    void decrementPositions(@Param("oldPosition") Long oldPosition,
//                            @Param("newPosition") Long newPosition);
//
//    @Deprecated
//    @Modifying
//    @Transactional
//    @Query("UPDATE FavoriteTrip ft SET ft.position = ft.position - 1 WHERE ft.position > :oldPosition")
//    void decrementPositionsAfter(@Param("oldPosition") Long oldPosition);
}
