package com.example.springapi.api.model;

import java.sql.Time;

public class Trip {
    private int trip_id;
    private Time tpep_pickup_datetime;
    private double average_wind_speed;

//    String vendor_id;
//    LocalDateTime tpep_dropoff_datetime;
//    int passenger_count;
//    double trip_distance;
//    String rate_code_id;
//    String store_and_fwd_flag;
//    int pickup_location_id;
//    int dropoff_location_id;
//    int payment_type;
//    double fare_amount;
//    double extra;
//    double mta_tax
//    double tip_amount;
//    double tolls_amount;
//    double improvement_surcharge;
//    double total_amount;
//    double congestion_surcharge;
//    double airport_fee;

    public double getAverage_wind_speed() {
        return average_wind_speed;
    }

    public void setAverage_wind_speed(double average_wind_speed) {
        this.average_wind_speed = average_wind_speed;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public Time getTpep_pickup_datetime() {
        return tpep_pickup_datetime;
    }

    public void setTpep_pickup_datetime(Time tpep_pickup_datetime) {
        this.tpep_pickup_datetime = tpep_pickup_datetime;
    }
}
