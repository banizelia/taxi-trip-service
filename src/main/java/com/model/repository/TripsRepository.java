package com.model.repository;

import com.model.Trip;

import org.hibernate.annotations.SortComparator;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TripsRepository extends JpaRepository<Trip, Long> {

    @Query(value = "SELECT trips.*, weather_observations.average_wind_speed " +
            "FROM weather_observations " +
            "INNER JOIN trips ON weather_observations.date = trips.pickup_datetime::DATE " +
            "WHERE (:startDateTime IS NULL OR pickup_datetime >= :startDateTime) AND " +
            "(:endDateTime IS NULL OR pickup_datetime <= :endDateTime) AND " +
            "(:minWindSpeed IS NULL OR weather_observations.average_wind_speed >= :minWindSpeed) AND " +
            "(:maxWindSpeed IS NULL OR weather_observations.average_wind_speed <= :maxWindSpeed) ",
            nativeQuery = true)
    List<Trip> filterTrips(
            @Param("startDateTime") String startDateTime,
            @Param("endDateTime") String endDateTime,
            @Param("minWindSpeed") Double minWindSpeed,
            @Param("maxWindSpeed") Double maxWindSpeed /*, Sort sort*/);


//    @Query(value = "SELECT trips.*, weather_observations.average_wind_speed " +
//            "FROM weather_observations " +
//            "INNER JOIN trips ON weather_observations.date = trips.pickup_datetime::DATE " +
////            "WHERE " +
////            "(:startDateTime IS NULL OR pickup_datetime >= :startDateTime) AND " +
////            "(:endDateTime IS NULL OR pickup_datetime <= :endDateTime) AND " +
////            "(:minWindSpeed IS NULL OR weather_observations.average_wind_speed >= :minWindSpeed) AND " +
////            "(:maxWindSpeed IS NULL OR weather_observations.average_wind_speed <= :maxWindSpeed) " +
//            "ORDER BY pickup_datetime desc " +
//            "limit :limit"
//            , nativeQuery = true)
//    List<Trip> filterTrips1(
////            @Param("startDateTime") String startDateTime,
////            @Param("endDateTime") String endDateTime,
////            @Param("minWindSpeed") Double minWindSpeed,
////            @Param("maxWindSpeed") Double maxWindSpeed,
////            @Param("sortBy") String sortBy,
////            @Param("direction") String direction,
//            @Param("limit") Integer limit);


    @Query( value = "SELECT weather_observations.average_wind_speed " +
            "FROM weather_observations " +
            "INNER JOIN trips ON weather_observations.date = trips.pickup_datetime::DATE " +
            "WHERE trips.id = :id"
            , nativeQuery = true)
    Double getWindSpeedByTripId(@Param("id") Long id);


    @Query(value = "INSERT INTO favorite_trips (trip_id) " +
            "SELECT :id " +
            "WHERE NOT EXISTS ( SELECT 1 FROM favorite_trips WHERE trip_id = :id );",
            nativeQuery = true)
    void addToFavourite(@Param("id") Long id);


    @Query( value = "DELETE FROM favorite_trips WHERE trip_id = :id;",
            nativeQuery = true)
    void removeFromFavourite(@Param("id") Long id);


    @Query(value = "SELECT trips.* " +
            "FROM trips " +
            "JOIN favorite_trips ON trips.id = favorite_trips.trip_id ",
            nativeQuery = true)
    List<Trip> getFavouriteList();
}
