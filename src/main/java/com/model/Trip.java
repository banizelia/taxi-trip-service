package com.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trips")
public class Trip {

//    @Transient
//    private double average_wind_speed;
//
//    public double getAverage_wind_speed() {
//        return average_wind_speed;
//    }
//
//    public void setAverage_wind_speed(double average_wind_speed) {
//        this.average_wind_speed = average_wind_speed;
//    }

    @Id
    @Column(name = "id")
    private Long id;


    @Column(name = "vendor_id")
    private String vendor_id;

    @Column(name = "pickup_datetime")
    private String pickup_datetime;

    @Column(name = "dropoff_datetime")
    private String dropoff_datetime;

    @Column(name = "passenger_count")
    private Integer passenger_count;

    @Column(name = "trip_distance")
    private Double trip_distance;

    @Column(name = "rate_code_id")
    private String rate_code_id;

    @Column(name = "store_and_fwd_flag")
    private String store_and_fwd_flag;

    @Column(name = "pickup_location_id")
    private Integer pickup_location_id;

    @Column(name = "dropoff_location_id")
    private Integer dropoff_location_id;

    @Column(name = "payment_type")
    private String payment_type;

    @Column(name = "fare_amount")
    private Double fare_amount;

    @Column(name = "extra")
    private Double extra;

    @Column(name = "mta_tax")
    private Double mta_tax;

    @Column(name = "tip_amount")
    private Double tip_amount;

    @Column(name = "tolls_amount")
    private Double tolls_amount;

    @Column(name = "improvement_surcharge")
    private Double improvement_surcharge;

    @Column(name = "total_amount")
    private Double total_amount;

    @Column(name = "congestion_surcharge")
    private Double congestion_surcharge;

    @Column(name = "airport_fee")
    private Double airport_fee;

    public Double getTip_amount() {
        return tip_amount;
    }

    public void setTip_amount(Double tip_amount) {
        this.tip_amount = tip_amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getPickup_datetime() {
        return pickup_datetime;
    }

    public void setPickup_datetime(String pickup_datetime) {
        this.pickup_datetime = pickup_datetime;
    }

    public String getDropoff_datetime() {
        return dropoff_datetime;
    }

    public void setDropoff_datetime(String dropoff_datetime) {
        this.dropoff_datetime = dropoff_datetime;
    }

    public Integer getPassenger_count() {
        return passenger_count;
    }

    public void setPassenger_count(Integer passenger_count) {
        this.passenger_count = passenger_count;
    }

    public Double getTrip_distance() {
        return trip_distance;
    }

    public void setTrip_distance(Double trip_distance) {
        this.trip_distance = trip_distance;
    }

    public String getRate_code_id() {
        return rate_code_id;
    }

    public void setRate_code_id(String rate_code_id) {
        this.rate_code_id = rate_code_id;
    }

    public String getStore_and_fwd_flag() {
        return store_and_fwd_flag;
    }

    public void setStore_and_fwd_flag(String store_and_fwd_flag) {
        this.store_and_fwd_flag = store_and_fwd_flag;
    }

    public Integer getPickup_location_id() {
        return pickup_location_id;
    }

    public void setPickup_location_id(Integer pickup_location_id) {
        this.pickup_location_id = pickup_location_id;
    }

    public Integer getDropoff_location_id() {
        return dropoff_location_id;
    }

    public void setDropoff_location_id(Integer dropoff_location_id) {
        this.dropoff_location_id = dropoff_location_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public Double getFare_amount() {
        return fare_amount;
    }

    public void setFare_amount(Double fare_amount) {
        this.fare_amount = fare_amount;
    }

    public Double getExtra() {
        return extra;
    }

    public void setExtra(Double extra) {
        this.extra = extra;
    }

    public Double getMta_tax() {
        return mta_tax;
    }

    public void setMta_tax(Double mta_tax) {
        this.mta_tax = mta_tax;
    }

    public Double getTolls_amount() {
        return tolls_amount;
    }

    public void setTolls_amount(Double tolls_amount) {
        this.tolls_amount = tolls_amount;
    }

    public Double getImprovement_surcharge() {
        return improvement_surcharge;
    }

    public void setImprovement_surcharge(Double improvement_surcharge) {
        this.improvement_surcharge = improvement_surcharge;
    }

    public Double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Double total_amount) {
        this.total_amount = total_amount;
    }

    public Double getCongestion_surcharge() {
        return congestion_surcharge;
    }

    public void setCongestion_surcharge(Double congestion_surcharge) {
        this.congestion_surcharge = congestion_surcharge;
    }

    public Double getAirport_fee() {
        return airport_fee;
    }

    public void setAirport_fee(Double airport_fee) {
        this.airport_fee = airport_fee;
    }
}