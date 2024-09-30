package com.web.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherTest {

    private Weather weather;

    @BeforeEach
    public void setUp() {
        weather = new Weather();
    }

    @Test
    public void testSetAndGetId() {
        Long id = 1L;
        weather.setId(id);
        assertEquals(id, weather.getId());
    }

    @Test
    public void testSetAndGetStationId() {
        String stationId = "Station123";
        weather.setStationId(stationId);
        assertEquals(stationId, weather.getStationId());
    }

    @Test
    public void testSetAndGetStationName() {
        String stationName = "Main Station";
        weather.setStationName(stationName);
        assertEquals(stationName, weather.getStationName());
    }

    @Test
    public void testSetAndGetDate() {
        Date date = new Date(System.currentTimeMillis());
        weather.setDate(date);
        assertEquals(date, weather.getDate());
    }

    @Test
    public void testSetAndGetAverageWindSpeed() {
        Double averageWindSpeed = 10.5;
        weather.setAverageWindSpeed(averageWindSpeed);
        assertEquals(averageWindSpeed, weather.getAverageWindSpeed());
    }

    @Test
    public void testSetAndGetPrecipitation() {
        Double precipitation = 2.5;
        weather.setPrecipitation(precipitation);
        assertEquals(precipitation, weather.getPrecipitation());
    }

    @Test
    public void testSetAndGetSnowDepth() {
        Double snowDepth = 15.0;
        weather.setSnowDepth(snowDepth);
        assertEquals(snowDepth, weather.getSnowDepth());
    }

    @Test
    public void testSetAndGetSnowfall() {
        Double snowfall = 10.0;
        weather.setSnowfall(snowfall);
        assertEquals(snowfall, weather.getSnowfall());
    }

    @Test
    public void testSetAndGetMaxTemperature() {
        Long maxTemperature = 30L;
        weather.setMaxTemperature(maxTemperature);
        assertEquals(maxTemperature, weather.getMaxTemperature());
    }

    @Test
    public void testSetAndGetMinTemperature() {
        Long minTemperature = 15L;
        weather.setMinTemperature(minTemperature);
        assertEquals(minTemperature, weather.getMinTemperature());
    }

    @Test
    public void testSetAndGetTrips() {
        Trip trip = new Trip();
        weather.setTrips(Collections.singletonList(trip));
        assertEquals(1, weather.getTrips().size());
        assertEquals(trip, weather.getTrips().get(0));
    }
}
