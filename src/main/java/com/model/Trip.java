package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "trips")
public class Trip {

    @ManyToOne
    @JoinColumn(name = "pickup_date")
    private Weather weather;

//    @Transient
//    @JsonIgnore
//    private Double averageWindSpeed;
//
//    public Double getAverageWindSpeed() {
//        return averageWindSpeed;
//    }
//
//    public void setAverageWindSpeed(Double averageWindSpeed) {
//        this.averageWindSpeed = averageWindSpeed;
//    }


    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "vendor_id")
    private String vendorId;

    @Column(name = "pickup_datetime")
    private String pickupDatetime;

    @Column(name = "pickup_date")
    private String pickupDate;

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @Column(name = "dropoff_datetime")
    private String dropoffDatetime;

    @Column(name = "passenger_count")
    private Integer passengerCount;

    @Column(name = "trip_distance")
    private Double tripDistance;

    @Column(name = "rate_code_id")
    private String rateCodeId;

    @Column(name = "store_and_fwd_flag")
    private String storeAndFwdFlag;

    @Column(name = "pickup_location_id")
    private Integer pickupLocationId;

    @Column(name = "dropoff_location_id")
    private Integer dropoffLocationId;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "fare_amount")
    private Double fareAmount;

    @Column(name = "extra")
    private Double extra;

    @Column(name = "mta_tax")
    private Double mtaTax;

    @Column(name = "tip_amount")
    private Double tipAmount;

    @Column(name = "tolls_amount")
    private Double tollsAmount;

    @Column(name = "improvement_surcharge")
    private Double improvementSurcharge;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "congestion_surcharge")
    private Double congestionSurcharge;

    @Column(name = "airport_fee")
    private Double airportFee;

    public Double getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(Double tipAmount) {
        this.tipAmount = tipAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getPickupDatetime() {
        return pickupDatetime;
    }

    public void setPickupDatetime(String pickupDatetime) {
        this.pickupDatetime = pickupDatetime;
    }

    public String getDropoffDatetime() {
        return dropoffDatetime;
    }

    public void setDropoffDatetime(String dropoffDatetime) {
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
        return congestionSurcharge;
    }

    public void setCongestionSurcharge(Double congestionSurcharge) {
        this.congestionSurcharge = congestionSurcharge;
    }

    public Double getAirportFee() {
        return airportFee;
    }

    public void setAirportFee(Double airportFee) {
        this.airportFee = airportFee;
    }
}