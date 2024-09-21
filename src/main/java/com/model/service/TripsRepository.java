package com.model.service;

import com.model.Trip;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TripsRepository extends JpaRepository<Trip, Long> {

    @Query(value = "SELECT * FROM trips WHERE trips.pickup_datetime = :startDateTime", nativeQuery = true)
    Optional<Trip> findByPickupDate(@Param("startDateTime") String date);



    @Query(value = "SELECT trips.*, central_park_weather_observations.average_wind_speed " +
            "FROM central_park_weather_observations " +
            "INNER JOIN trips ON central_park_weather_observations.date = trips.pickup_datetime::DATE " +
            "WHERE " +
            "(:startDateTime IS NULL OR pickup_datetime >= :startDateTime) AND " +
            "(:endDateTime IS NULL OR pickup_datetime <= :endDateTime) AND " +
            "(:minWindSpeed IS NULL OR central_park_weather_observations.average_wind_speed >= :minWindSpeed) AND " +
            "(:maxWindSpeed IS NULL OR central_park_weather_observations.average_wind_speed <= :maxWindSpeed) " +
            "limit 5"
            , nativeQuery = true)
    Optional<List<Trip>> filterTrips(
            @Param("startDateTime") String startDateTime,
            @Param("endDateTime") String endDateTime,
            @Param("minWindSpeed") Double minWindSpeed,
            @Param("maxWindSpeed") Double maxWindSpeed);

}
