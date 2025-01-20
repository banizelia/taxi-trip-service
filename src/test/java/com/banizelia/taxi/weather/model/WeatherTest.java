package com.banizelia.taxi.weather.model;

import com.banizelia.taxi.trip.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WeatherTest {

    private final LocalDate localDate = LocalDate.of(2024, 1, 1);
    private Weather weather;

    @BeforeEach
    void setUp() {
        weather = new Weather();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(weather);
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        weather.setId(id);
        assertEquals(id, weather.getId());
    }

    @Test
    void testSetAndGetStationId() {
        String stationId = "STATION001";
        weather.setStationId(stationId);
        assertEquals(stationId, weather.getStationId());
    }

    @Test
    void testSetAndGetStationName() {
        String stationName = "Central Station";
        weather.setStationName(stationName);
        assertEquals(stationName, weather.getStationName());
    }

    @Test
    void testSetAndGetDate() {
        weather.setDate(localDate);
        assertEquals(localDate, weather.getDate());
    }

    @Test
    void testSetAndGetAverageWindSpeed() {
        Double windSpeed = 15.5;
        weather.setAverageWindSpeed(windSpeed);
        assertEquals(windSpeed, weather.getAverageWindSpeed());
    }

    @Test
    void testSetAndGetPrecipitation() {
        Double precipitation = 25.7;
        weather.setPrecipitation(precipitation);
        assertEquals(precipitation, weather.getPrecipitation());
    }

    @Test
    void testSetAndGetSnowDepth() {
        Double snowDepth = 10.0;
        weather.setSnowDepth(snowDepth);
        assertEquals(snowDepth, weather.getSnowDepth());
    }

    @Test
    void testSetAndGetSnowfall() {
        Double snowfall = 5.5;
        weather.setSnowfall(snowfall);
        assertEquals(snowfall, weather.getSnowfall());
    }

    @Test
    void testSetAndGetMaxTemperature() {
        Long maxTemp = 30L;
        weather.setMaxTemperature(maxTemp);
        assertEquals(maxTemp, weather.getMaxTemperature());
    }

    @Test
    void testSetAndGetMinTemperature() {
        Long minTemp = -5L;
        weather.setMinTemperature(minTemp);
        assertEquals(minTemp, weather.getMinTemperature());
    }

    @Test
    void testSetAndGetTrips() {
        List<Trip> trips = new ArrayList<>();
        Trip trip1 = new Trip();
        Trip trip2 = new Trip();
        trips.add(trip1);
        trips.add(trip2);

        weather.setTrips(trips);
        assertEquals(trips, weather.getTrips());
        assertEquals(2, weather.getTrips().size());
    }

    @Test
    void testFullWeatherObject() {
        // Arrange
        Long id = 1L;
        String stationId = "STATION001";
        String stationName = "Central Station";
        Double windSpeed = 15.5;
        Double precipitation = 25.7;
        Double snowDepth = 10.0;
        Double snowfall = 5.5;
        Long maxTemp = 30L;
        Long minTemp = -5L;
        List<Trip> trips = new ArrayList<>();

        // Act
        weather.setId(id);
        weather.setStationId(stationId);
        weather.setStationName(stationName);
        weather.setDate(localDate);
        weather.setAverageWindSpeed(windSpeed);
        weather.setPrecipitation(precipitation);
        weather.setSnowDepth(snowDepth);
        weather.setSnowfall(snowfall);
        weather.setMaxTemperature(maxTemp);
        weather.setMinTemperature(minTemp);
        weather.setTrips(trips);

        // Assert
        assertEquals(id, weather.getId());
        assertEquals(stationId, weather.getStationId());
        assertEquals(stationName, weather.getStationName());
        assertEquals(localDate, weather.getDate());
        assertEquals(windSpeed, weather.getAverageWindSpeed());
        assertEquals(precipitation, weather.getPrecipitation());
        assertEquals(snowDepth, weather.getSnowDepth());
        assertEquals(snowfall, weather.getSnowfall());
        assertEquals(maxTemp, weather.getMaxTemperature());
        assertEquals(minTemp, weather.getMinTemperature());
        assertEquals(trips, weather.getTrips());
    }
}