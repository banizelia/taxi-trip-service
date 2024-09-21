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



    @Query(value = "SELECT trips.*, central_park_weather_observations.average_wind_speed\n" +
            "FROM central_park_weather_observations\n" +
            "INNER JOIN trips ON central_park_weather_observations.date = trips.pickup_datetime::DATE\n" +
            "WHERE \n" +
            "(:startDateTime IS NULL OR pickup_datetime >= :startDateTime) \n" +
            "AND (:endDateTime IS NULL OR pickup_datetime <= :endDateTime) \n" +
            "AND (:minWindSpeed IS NULL OR central_park_weather_observations.average_wind_speed >= :minWindSpeed)\n" +
            "AND (:maxWindSpeed IS NULL OR central_park_weather_observations.average_wind_speed <= :maxWindSpeed)\n" +
            "\n" +
            "limit 5"
            , nativeQuery = true)
    Optional<List<Trip>> filterTrips(
            @Param("startDateTime") String startDateTime,
            @Param("endDateTime") String endDateTime,
            @Param("minWindSpeed") Double minWindSpeed,
            @Param("maxWindSpeed") Double maxWindSpeed);


//    @Query(value = "SELECT \n" +
//            "\ttrips.*,\n" +
//            "\tcentral_park_weather_observations.average_wind_speed\n" +
//            "FROM \n" +
//            "    central_park_weather_observations\n" +
//            "    INNER JOIN trips ON central_park_weather_observations.date = trips.pickup_datetime::DATE\n" +
//            "WHERE\n" +
//            "\tpickup_datetime BETWEEN :startDateTime AND :endDateTime\n" +
//            "   AND (:minWindSpeed IS NULL OR average_wind_speed >= :minWindSpeed)\n" +
//            "   AND (:maxWindSpeed IS NULL OR average_wind_speed <= :maxWindSpeed);"
//            , nativeQuery = true)
//    Optional<List<Trip>> filterTrips(
//            @Param("startDateTime") String startDateTime,
//            @Param("endDateTime") String endDateTime,
//            @Param("minWindSpeed") Double minWindSpeed,
//            @Param("maxWindSpeed") Double maxWindSpeed);







////     фильтрация по pickup_datetime
//    @Query(value = "SELECT \n" +
//            "\ttrips.*,\n" +
//            "\tcentral_park_weather_observations.average_wind_speed\n" +
//            "FROM \n" +
//            "    central_park_weather_observations\n" +
//            "    INNER JOIN trips ON central_park_weather_observations.date = trips.pickup_datetime::DATE\n" +
//            "WHERE\n" +
//            "\tpickup_datetime BETWEEN :startDateTime AND :endDateTime;"
//            , nativeQuery = true)
//    Optional<List<Trip>> filterByPickupDatetime(
//            @Param("startDateTime") String startDateTime,
//            @Param("endDateTime") String endDateTime);










//    @Query(value = "", nativeQuery = true)
//    Optional<List<Trip>> filterByPickupDatetime(@Param("id") Long id,
//                                                @Param()
//                                                );


//    @Query(value = "SELECT * FROM trips WHERE trips.passenger_count = 9;")
//    List<Trip> findByPassengerCount(int passengerCount);

//    @Query(value = "SELECT * FROM student WHERE address->>'postCode' = :postCode", nativeQuery = true)
//    List<Trip> findByPickupDatetimeBetweenAndAverageWindSpeedTenthsOfMetersPerSecondBetween(
//            Time startDateTime,
//            Time endDateTime,
//            double minWindSpeed,
//            double maxWindSpeed
//    );


//    //@Query(value = "SELECT * FROM student WHERE address->>'postCode' = :postCode", nativeQuery = true)
//    @Query
//    List<Trip> findByPickupDatetimeBetweenAndAverageWindSpeedTenthsOfMetersPerSecondBetween(
//            Time startDateTime,
//            Time endDateTime,
//            double minWindSpeed,
//            double maxWindSpeed
//    );
//
//    @Query
//    List<Trip> findByPickupDatetimeBetweenOrderByPickupDatetime(
//            Time startDateTime,
//            Time endDateTime
//    );
//
//    @Query
//    List<Trip> findByAverageWindSpeedTenthsOfMetersPerSecondBetweenOrderByAverageWindSpeedTenthsOfMetersPerSecond(
//            double minWindSpeed,
//            double maxWindSpeed
//    );




}
