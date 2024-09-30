package com.web.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripTest {

    private Trip trip;

    @BeforeEach
    public void setUp() {
        trip = new Trip();
    }

    @Test
    public void testSetAndGetId() {
        Long id = 1L;
        trip.setId(id);
        assertEquals(id, trip.getId());
    }

    @Test
    public void testSetAndGetVendorId() {
        String vendorId = "Vendor123";
        trip.setVendorId(vendorId);
        assertEquals(vendorId, trip.getVendorId());
    }

    @Test
    public void testSetAndGetPickupDatetime() {
        Timestamp pickupDatetime = new Timestamp(System.currentTimeMillis());
        trip.setPickupDatetime(pickupDatetime);
        assertEquals(pickupDatetime, trip.getPickupDatetime());
    }

    @Test
    public void testSetAndGetDropoffDatetime() {
        Timestamp dropoffDatetime = new Timestamp(System.currentTimeMillis());
        trip.setDropoffDatetime(dropoffDatetime);
        assertEquals(dropoffDatetime, trip.getDropoffDatetime());
    }

    @Test
    public void testSetAndGetPassengerCount() {
        Integer passengerCount = 2;
        trip.setPassengerCount(passengerCount);
        assertEquals(passengerCount, trip.getPassengerCount());
    }

    @Test
    public void testSetAndGetTripDistance() {
        Double tripDistance = 10.5;
        trip.setTripDistance(tripDistance);
        assertEquals(tripDistance, trip.getTripDistance());
    }

    @Test
    public void testSetAndGetRateCodeId() {
        String rateCodeId = "Rate123";
        trip.setRateCodeId(rateCodeId);
        assertEquals(rateCodeId, trip.getRateCodeId());
    }

    @Test
    public void testSetAndGetStoreAndFwdFlag() {
        String storeAndFwdFlag = "Y";
        trip.setStoreAndFwdFlag(storeAndFwdFlag);
        assertEquals(storeAndFwdFlag, trip.getStoreAndFwdFlag());
    }

    @Test
    public void testSetAndGetPickupLocationId() {
        Integer pickupLocationId = 1;
        trip.setPickupLocationId(pickupLocationId);
        assertEquals(pickupLocationId, trip.getPickupLocationId());
    }

    @Test
    public void testSetAndGetDropoffLocationId() {
        Integer dropoffLocationId = 2;
        trip.setDropoffLocationId(dropoffLocationId);
        assertEquals(dropoffLocationId, trip.getDropoffLocationId());
    }

    @Test
    public void testSetAndGetPaymentType() {
        String paymentType = "Credit Card";
        trip.setPaymentType(paymentType);
        assertEquals(paymentType, trip.getPaymentType());
    }

    @Test
    public void testSetAndGetFareAmount() {
        Double fareAmount = 20.0;
        trip.setFareAmount(fareAmount);
        assertEquals(fareAmount, trip.getFareAmount());
    }

    @Test
    public void testSetAndGetExtra() {
        Double extra = 5.0;
        trip.setExtra(extra);
        assertEquals(extra, trip.getExtra());
    }

    @Test
    public void testSetAndGetMtaTax() {
        Double mtaTax = 1.0;
        trip.setMtaTax(mtaTax);
        assertEquals(mtaTax, trip.getMtaTax());
    }

    @Test
    public void testSetAndGetTipAmount() {
        Double tipAmount = 3.0;
        trip.setTipAmount(tipAmount);
        assertEquals(tipAmount, trip.getTipAmount());
    }

    @Test
    public void testSetAndGetTollsAmount() {
        Double tollsAmount = 2.0;
        trip.setTollsAmount(tollsAmount);
        assertEquals(tollsAmount, trip.getTollsAmount());
    }

    @Test
    public void testSetAndGetImprovementSurcharge() {
        Double improvementSurcharge = 0.5;
        trip.setImprovementSurcharge(improvementSurcharge);
        assertEquals(improvementSurcharge, trip.getImprovementSurcharge());
    }

    @Test
    public void testSetAndGetTotalAmount() {
        Double totalAmount = 30.0;
        trip.setTotalAmount(totalAmount);
        assertEquals(totalAmount, trip.getTotalAmount());
    }

    @Test
    public void testGetCongestionSurchargeWithNull() {
        assertEquals(0.0, trip.getCongestionSurcharge());
    }

    @Test
    public void testSetAndGetCongestionSurcharge() {
        Double congestionSurcharge = 2.0;
        trip.setCongestionSurcharge(congestionSurcharge);
        assertEquals(congestionSurcharge, trip.getCongestionSurcharge());
    }

    @Test
    public void testGetAirportFeeWithNull() {
        assertEquals(0.0, trip.getAirportFee());
    }

    @Test
    public void testSetAndGetAirportFee() {
        Double airportFee = 1.5;
        trip.setAirportFee(airportFee);
        assertEquals(airportFee, trip.getAirportFee());
    }

    @Test
    public void testSetAndGetPickupDate() {
        Date pickupDate = new Date(System.currentTimeMillis());
        trip.setPickupDate(pickupDate);
        assertEquals(pickupDate, trip.getPickupDate());
    }

    @Test
    public void testSetAndGetFavoriteTrip() {
        FavoriteTrip favoriteTrip = new FavoriteTrip();
        trip.setFavoriteTrip(favoriteTrip);
        assertEquals(favoriteTrip, trip.getFavoriteTrip());
    }
}
