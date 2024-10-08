package com.web.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Модель для представления данных наблюдений за погодой, включая данные о ветре, осадках, температуре и снегопаде.
 */
@Entity
@Table(name = "weather_observations")
public class Weather {

    /**
     * Список поездок, связанных с текущими погодными условиями.
     */
    @OneToMany(mappedBy = "weather")
    private List<Trip> trips;

    /**
     * Идентификатор записи о погоде (основной ключ).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id")
    private Long id;

    /**
     * Идентификатор метеостанции.
     */
    @Column(name = "station_id")
    private String stationId;

    /**
     * Название метеостанции.
     */
    @Column(name = "station_name")
    private String stationName;

    /**
     * Дата наблюдения за погодой.
     */
    @Column(name = "date")
    private LocalDate date;

    /**
     * Средняя скорость ветра.
     */
    @Column(name = "average_wind_speed")
    private Double averageWindSpeed;

    /**
     * Количество осадков.
     */
    @Column(name = "precipitation")
    private Double precipitation;

    /**
     * Глубина снега на момент наблюдения.
     */
    @Column(name = "snow_depth")
    private Double snowDepth;

    /**
     * Количество выпавшего снега.
     */
    @Column(name = "snowfall")
    private Double snowfall;

    /**
     * Максимальная температура.
     */
    @Column(name = "max_temperature")
    private Long maxTemperature;

    /**
     * Минимальная температура.
     */
    @Column(name = "min_temperature")
    private Long minTemperature;

    public Weather() {
    }

    // Getters and setters

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
