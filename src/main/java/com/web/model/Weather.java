package com.web.model;

import com.web.util.AllowedField;
import jakarta.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "weather_observations")
public class Weather {

    @OneToMany(mappedBy = "weather")
    private List<Trip> trips;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id")
    @AllowedField
    private Long id;

    @Column(name = "station_id")
    @AllowedField
    private String stationId;

    @Column(name = "station_name")
    @AllowedField
    private String stationName;

    @Column(name = "date")
    @AllowedField
    private Date date;

    @Column(name = "average_wind_speed")
    @AllowedField
    private Double averageWindSpeed;

    @Column(name = "precipitation")
    @AllowedField
    private Double precipitation;

    @Column(name = "snow_depth")
    @AllowedField
    private Double snowDepth;

    @Column(name = "snowfall")
    @AllowedField
    private Double snowfall;

    @Column(name = "max_temperature")
    @AllowedField
    private Long maxTemperature;

    @Column(name = "min_temperature")
    @AllowedField
    private Long minTemperature;

    public Weather() {
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAverageWindSpeed() {
        return averageWindSpeed;
    }

    public void setAverageWindSpeed(Double averageWindSpeed) {
        this.averageWindSpeed = averageWindSpeed;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public Double getSnowDepth() {
        return snowDepth;
    }

    public void setSnowDepth(Double snowDepth) {
        this.snowDepth = snowDepth;
    }

    public Double getSnowfall() {
        return snowfall;
    }

    public void setSnowfall(Double snowfall) {
        this.snowfall = snowfall;
    }

    public Long getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Long maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Long getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Long minTemperature) {
        this.minTemperature = minTemperature;
    }
}
