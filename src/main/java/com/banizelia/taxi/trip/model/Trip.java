package com.banizelia.taxi.trip.model;

import com.banizelia.taxi.favorite.model.FavoriteTrip;
import com.banizelia.taxi.weather.model.Weather;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trips")
public class Trip {
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_date", referencedColumnName = "date", insertable = false, updatable = false)
    private Weather weather;

    @JsonIgnore
    @OneToOne(mappedBy = "trip", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FavoriteTrip favoriteTrip;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;

    @Column(name = "vendor_id")
    private Integer vendorId;

    @Column(name = "pickup_datetime")
    private LocalDateTime pickupDatetime;

    @Column(name = "dropoff_datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dropoffDatetime;

    @Column(name = "passenger_count")
    private Integer passengerCount;

    @Column(name = "trip_distance")
    private Double tripDistance;

    @Column(name = "rate_code_id")
    private Integer rateCodeId;

    @Column(name = "store_and_fwd_flag")
    private String storeAndFwdFlag;

    @Column(name = "pickup_location_id")
    private Integer pickupLocationId;

    @Column(name = "dropoff_location_id")
    private Integer dropoffLocationId;

    @Column(name = "payment_type")
    private Integer paymentType;

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

    @Column(name = "pickup_date")
    private LocalDate pickupDate;
}
