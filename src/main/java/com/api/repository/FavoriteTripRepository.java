package com.api.repository;

import com.api.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteTripRepository extends JpaRepository<FavoriteTrip, Long> {

    @Query("SELECT t FROM Trip t JOIN t.favoriteTrip ft")
    List<Trip> getFavouriteTrips();

    @Query("SELECT COALESCE(MAX(ft.position), 0) FROM FavoriteTrip ft")
    Optional<Long> findMaxPosition();

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
