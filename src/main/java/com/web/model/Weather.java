package com.web.model;


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
    private Long id;

    @Column(name = "station_id")
    private String stationId;

    @Column(name = "station_name")
    private String stationName;

    @Column(name = "date")
    private Date date;

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


//        public Weather(Object[] weatherParams) {
//
//        this.stationId = (String) weatherParams[0];
//        this.stationName = (String) weatherParams[1];
//        this.date = (Date) weatherParams[2];
//        this.averageWindSpeed = (Double) weatherParams[3];
//        this.precipitation = (Double) weatherParams[4];
//        this.snowDepth = (Double) weatherParams[5];
//        this.snowfall = (Double) weatherParams[6];
//        this.maxTemperature = (Long) weatherParams[7];
//        this.minTemperature = (Long) weatherParams[8];
//        this.id = (Long) weatherParams[9];
//    }

}
