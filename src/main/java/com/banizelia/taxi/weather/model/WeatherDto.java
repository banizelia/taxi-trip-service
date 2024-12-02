package com.banizelia.taxi.weather.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WeatherDto {
    private Long id;
    private String stationId;
    private String stationName;
    private LocalDate date;
    private Double averageWindSpeed;
    private Double precipitation;
    private Double snowDepth;
    private Double snowfall;
    private Long maxTemperature;
    private Long minTemperature;
}
