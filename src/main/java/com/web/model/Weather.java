package com.web.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Model representing weather observation data, including information about wind, precipitation, temperature, and snowfall.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "weather_observations")
public class Weather {
    @OneToMany(mappedBy = "weather")
    private List<Trip> trips;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id")
    private Long id;

    @Column(name = "station_id")
    private String stationId;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "average_wind_speed")
    private Double averageWindSpeed;

    @Column(name = "precipitation")
    private Double precipitation;

    @Column(name = "snow_depth")
    private Double snowDepth;

    @Column(name = "snowfall")
    private Double snowfall;

    @Column(name = "max_temperature")
    private Long maxTemperature;

    @Column(name = "min_temperature")
    private Long minTemperature;
}
