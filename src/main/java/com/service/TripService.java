package com.service;

import org.springframework.stereotype.Service;
import com.model.DatabaseConnector;
import com.model.Trip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class TripService {

    private final DatabaseConnector databaseConnector;

    public TripService(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public Trip getTripById(Integer tripId) {
        Trip trip = null;

        String query = "SELECT trip_id, tpep_pickup_datetime, " +
                "central_park_weather_observations.average_wind_speed " +
                "FROM trips WHERE trip_id = ?;";

        try (Connection connection = databaseConnector.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, tripId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                trip = new Trip();
                trip.setTrip_id(resultSet.getInt("trip_id"));
                trip.setPickup_datetime(resultSet.getTime("tpep_pickup_datetime"));
                trip.setAverage_wind_speed(resultSet.getDouble("average_wind_speed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trip;
    }
}
