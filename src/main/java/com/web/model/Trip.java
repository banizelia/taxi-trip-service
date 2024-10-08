package com.web.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.*;

/**
 * Модель поездки, включающая информацию о времени, месте и оплате поездки, а также связанную информацию о погоде и избранных поездках.
 */
@Entity
@Table(name = "trips")
public class Trip {

    /**
     * Связь с моделью погоды. Отображает информацию о погоде в момент поездки.
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_date", referencedColumnName = "date", insertable = false, updatable = false)
    private Weather weather;

    /**
     * Связь с моделью избранной поездки. Отображает, если поездка находится в избранном.
     */
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "trip_id", insertable = false, updatable = false)
    private FavoriteTrip favoriteTrip;

    /**
     * Идентификатор поездки (основной ключ).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Идентификатор поставщика услуги такси.
     */
    @Column(name = "vendor_id")
    private String vendorId;

    /**
     * Дата и время начала поездки.
     */
    @Column(name = "pickup_datetime")
    private LocalDateTime pickupDatetime;

    /**
     * Дата и время окончания поездки.
     */
    @Column(name = "dropoff_datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dropoffDatetime;

    /**
     * Количество пассажиров.
     */
    @Column(name = "passenger_count")
    private Integer passengerCount;

    /**
     * Расстояние поездки в милях.
     */
    @Column(name = "trip_distance")
    private Double tripDistance;

    /**
     * Код тарифа для поездки.
     */
    @Column(name = "rate_code_id")
    private String rateCodeId;

    /**
     * Флаг "store_and_fwd", указывающий, был ли заказ отправлен через внешнюю систему.
     */
    @Column(name = "store_and_fwd_flag")
    private String storeAndFwdFlag;

    /**
     * Идентификатор места посадки.
     */
    @Column(name = "pickup_location_id")
    private Integer pickupLocationId;

    /**
     * Идентификатор места высадки.
     */
    @Column(name = "dropoff_location_id")
    private Integer dropoffLocationId;

    /**
     * Тип оплаты за поездку.
     */
    @Column(name = "payment_type")
    private String paymentType;

    /**
     * Основная сумма за поездку.
     */
    @Column(name = "fare_amount")
    private Double fareAmount;

    /**
     * Дополнительная плата.
     */
    @Column(name = "extra")
    private Double extra;

    /**
     * Налог MTA.
     */
    @Column(name = "mta_tax")
    private Double mtaTax;

    /**
     * Сумма чаевых.
     */
    @Column(name = "tip_amount")
    private Double tipAmount;

    /**
     * Плата за проезд через платные дороги.
     */
    @Column(name = "tolls_amount")
    private Double tollsAmount;

    /**
     * Дополнительная плата за улучшение.
     */
    @Column(name = "improvement_surcharge")
    private Double improvementSurcharge;

    /**
     * Общая сумма за поездку.
     */
    @Column(name = "total_amount")
    private Double totalAmount;

    /**
     * Плата за пробки.
     */
    @Column(name = "congestion_surcharge")
    private Double congestionSurcharge;

    /**
     * Плата за услуги аэропорта.
     */
    @Column(name = "airport_fee")
    private Double airportFee;

    /**
     * Дата поездки, вычисляемая из времени начала поездки.
     */
    @Column(name = "pickup_date", insertable = false, updatable = false)
    private LocalDate pickupDate;

    public Trip() {
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public LocalDateTime getPickupDatetime() {
        return pickupDatetime;
    }

    public void setPickupDatetime(LocalDateTime pickupDatetime) {
        this.pickupDatetime = pickupDatetime;
    }

    public LocalDateTime getDropoffDatetime() {
        return dropoffDatetime;
    }

    public void setDropoffDatetime(LocalDateTime dropoffDatetime) {
        this.dropoffDatetime = dropoffDatetime;
    }

    public Integer getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }

    public Double getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(Double tripDistance) {
        this.tripDistance = tripDistance;
    }

    public String getRateCodeId() {
        return rateCodeId;
    }

    public void setRateCodeId(String rateCodeId) {
        this.rateCodeId = rateCodeId;
    }

    public String getStoreAndFwdFlag() {
        return storeAndFwdFlag;
    }

    public void setStoreAndFwdFlag(String storeAndFwdFlag) {
        this.storeAndFwdFlag = storeAndFwdFlag;
    }

    public Integer getPickupLocationId() {
        return pickupLocationId;
    }

    public void setPickupLocationId(Integer pickupLocationId) {
        this.pickupLocationId = pickupLocationId;
    }

    public Integer getDropoffLocationId() {
        return dropoffLocationId;
    }

    public void setDropoffLocationId(Integer dropoffLocationId) {
        this.dropoffLocationId = dropoffLocationId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(Double fareAmount) {
        this.fareAmount = fareAmount;
    }

    public Double getExtra() {
        return extra;
    }

    public void setExtra(Double extra) {
        this.extra = extra;
    }

    public Double getMtaTax() {
        return mtaTax;
    }

    public void setMtaTax(Double mtaTax) {
        this.mtaTax = mtaTax;
    }

    public Double getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(Double tipAmount) {
        this.tipAmount = tipAmount;
    }

    public Double getTollsAmount() {
        return tollsAmount;
    }

    public void setTollsAmount(Double tollsAmount) {
        this.tollsAmount = tollsAmount;
    }

    public Double getImprovementSurcharge() {
        return improvementSurcharge;
    }

    public void setImprovementSurcharge(Double improvementSurcharge) {
        this.improvementSurcharge = improvementSurcharge;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getCongestionSurcharge() {
        return congestionSurcharge != null ? congestionSurcharge : 0.0;
    }

    public void setCongestionSurcharge(Double congestionSurcharge) {
        this.congestionSurcharge = congestionSurcharge;
    }

    public Double getAirportFee() {
        return airportFee != null ? airportFee : 0.0;
    }

    public void setAirportFee(Double airportFee) {
        this.airportFee = airportFee;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    @PrePersist
    @PreUpdate
    public void setPickupDate() {
        if (pickupDatetime != null) {
            this.pickupDate = pickupDatetime.toLocalDate();
        }
    }

    public FavoriteTrip getFavoriteTrip() {
        return favoriteTrip;
    }

    public void setFavoriteTrip(FavoriteTrip favoriteTrip) {
        this.favoriteTrip = favoriteTrip;
    }
}
