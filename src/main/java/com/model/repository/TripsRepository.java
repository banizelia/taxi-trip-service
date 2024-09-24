package com.model.repository;

import com.model.Trip;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.*;

@Repository
public interface TripsRepository extends JpaRepository<Trip, Long> {

    @Query("SELECT t FROM Trip t WHERE " +
            " (pickupDatetime BETWEEN :startDateTime AND :endDateTime) AND " +
            " (weather.averageWindSpeed BETWEEN :minWindSpeed AND :maxWindSpeed)")
    List<Trip> filter(@Param("startDateTime") Timestamp startDateTime,
                      @Param("endDateTime") Timestamp endDateTime,
                      @Param("minWindSpeed") Double minWindSpeed,
                      @Param("maxWindSpeed") Double maxWindSpeed,
                      Sort sort, Limit limit);

    // [Request processing failed: org.springframework.orm.jpa.JpaSystemException: JDBC exception executing SQL [INSERT INTO favorite_trips (trip_id) VALUES (?)] [Запрос не вернул результатов.]
    // но при этом значение добавляется
    @Query(value = "INSERT INTO favorite_trips (trip_id) VALUES (:id)"
            , nativeQuery = true)
    void addToFavourite(@Param("id") Long id);

    @Query(value = "DELETE FROM favorite_trips WHERE favorite_trips:trip_id = :id "
            , nativeQuery = true)
    void removeFromFavourite(@Param("id") Long id);

    @Query(value = "SELECT EXISTS ( SELECT 1 FROM public.favorite_trips WHERE trip_id = :id ) "
            , nativeQuery = true)
    Boolean isTripInFavourite(@Param("id") Long id);

    @Query("SELECT t FROM Trip t WHERE t.id = favoriteTrip.tripId")
    List<Trip> getFavouriteList();

}
