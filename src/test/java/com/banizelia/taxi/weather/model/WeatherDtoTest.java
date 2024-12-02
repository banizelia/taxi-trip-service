package com.banizelia.taxi.weather.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class WeatherDtoTest {

    private WeatherDto weatherDto;
    private final LocalDate localDate = LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        weatherDto = new WeatherDto();
        weatherDto.setId(1L);
        weatherDto.setStationId("STATION001");
        weatherDto.setStationName("Central Station");
        weatherDto.setDate(localDate);
        weatherDto.setAverageWindSpeed(15.5);
        weatherDto.setPrecipitation(25.7);
        weatherDto.setSnowDepth(10.0);
        weatherDto.setSnowfall(5.5);
        weatherDto.setMaxTemperature(30L);
        weatherDto.setMinTemperature(-5L);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L, weatherDto.getId());
        assertEquals("STATION001", weatherDto.getStationId());
        assertEquals("Central Station", weatherDto.getStationName());
        assertEquals(localDate, weatherDto.getDate());
        assertEquals(15.5, weatherDto.getAverageWindSpeed());
        assertEquals(25.7, weatherDto.getPrecipitation());
        assertEquals(10.0, weatherDto.getSnowDepth());
        assertEquals(5.5, weatherDto.getSnowfall());
        assertEquals(30L, weatherDto.getMaxTemperature());
        assertEquals(-5L, weatherDto.getMinTemperature());
    }

    @Test
    void testEquals() {
        // Create an identical DTO
        WeatherDto identicalDto = new WeatherDto();
        identicalDto.setId(1L);
        identicalDto.setStationId("STATION001");
        identicalDto.setStationName("Central Station");
        identicalDto.setDate(localDate);
        identicalDto.setAverageWindSpeed(15.5);
        identicalDto.setPrecipitation(25.7);
        identicalDto.setSnowDepth(10.0);
        identicalDto.setSnowfall(5.5);
        identicalDto.setMaxTemperature(30L);
        identicalDto.setMinTemperature(-5L);

        // Test equality
        assertEquals(weatherDto, identicalDto);
        assertEquals(weatherDto.hashCode(), identicalDto.hashCode());

        // Test inequality
        WeatherDto differentDto = new WeatherDto();
        differentDto.setId(2L);
        assertNotEquals(weatherDto, differentDto);
        assertNotEquals(weatherDto.hashCode(), differentDto.hashCode());
    }

    @Test
    void testToString() {
        String toString = weatherDto.toString();

        // Verify that toString contains all field values
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("stationId=STATION001"));
        assertTrue(toString.contains("stationName=Central Station"));
        assertTrue(toString.contains("date=" + localDate));
        assertTrue(toString.contains("averageWindSpeed=15.5"));
        assertTrue(toString.contains("precipitation=25.7"));
        assertTrue(toString.contains("snowDepth=10.0"));
        assertTrue(toString.contains("snowfall=5.5"));
        assertTrue(toString.contains("maxTemperature=30"));
        assertTrue(toString.contains("minTemperature=-5"));
    }

    @Test
    void testBuilder() {
        WeatherDto dto = new WeatherDto();
        dto.setId(1L);
        dto.setStationId("STATION001");

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("STATION001", dto.getStationId());
    }

    @Test
    void testNullValues() {
        WeatherDto dto = new WeatherDto();

        assertNull(dto.getId());
        assertNull(dto.getStationId());
        assertNull(dto.getStationName());
        assertNull(dto.getDate());
        assertNull(dto.getAverageWindSpeed());
        assertNull(dto.getPrecipitation());
        assertNull(dto.getSnowDepth());
        assertNull(dto.getSnowfall());
        assertNull(dto.getMaxTemperature());
        assertNull(dto.getMinTemperature());
    }

    @Test
    void testEqualsWithNullFields() {
        WeatherDto dto1 = new WeatherDto();
        WeatherDto dto2 = new WeatherDto();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testCopyConstructor() {
        WeatherDto copy = new WeatherDto();
        copy.setId(weatherDto.getId());
        copy.setStationId(weatherDto.getStationId());
        copy.setStationName(weatherDto.getStationName());
        copy.setDate(weatherDto.getDate());
        copy.setAverageWindSpeed(weatherDto.getAverageWindSpeed());
        copy.setPrecipitation(weatherDto.getPrecipitation());
        copy.setSnowDepth(weatherDto.getSnowDepth());
        copy.setSnowfall(weatherDto.getSnowfall());
        copy.setMaxTemperature(weatherDto.getMaxTemperature());
        copy.setMinTemperature(weatherDto.getMinTemperature());

        assertEquals(weatherDto, copy);
        assertNotSame(weatherDto, copy);
    }

    @Test
    void testEqualsWithDifferentTypes() {
        Object other = new Object();
        assertNotEquals(weatherDto, other);
        assertNotEquals(null, weatherDto);
    }
}