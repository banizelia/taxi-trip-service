package com.web.mapper;

import com.web.model.Trip;
import com.web.model.dto.TripDto;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TripMapperTest {

    private final TripMapper mapper = TripMapper.INSTANCE;

    @Test
    void shouldMapAllFieldsCorrectly() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setVendorId("2");
        trip.setPickupDatetime(now);
        trip.setDropoffDatetime(now.plusHours(1));
        trip.setPassengerCount(3);
        trip.setTripDistance(15.5);
        trip.setRateCodeId("1");
        trip.setStoreAndFwdFlag("N");
        trip.setPickupLocationId(100);
        trip.setDropoffLocationId(200);
        trip.setPaymentType("1");
        trip.setFareAmount(25.0);
        trip.setExtra(2.0);
        trip.setMtaTax(0.5);
        trip.setTipAmount(5.0);
        trip.setTollsAmount(0.0);
        trip.setImprovementSurcharge(0.3);
        trip.setTotalAmount(32.8);
        trip.setCongestionSurcharge(2.5);
        trip.setAirportFee(1.5);

        // when
        TripDto tripDto = mapper.tripToTripDto(trip);

        // then
        assertNotNull(tripDto);
        assertEquals(trip.getId(), tripDto.getId());
        assertEquals(trip.getVendorId(), tripDto.getVendorId());
        assertEquals(trip.getPickupDatetime(), tripDto.getPickupDatetime());
        assertEquals(trip.getDropoffDatetime(), tripDto.getDropoffDatetime());
        assertEquals(trip.getPassengerCount(), tripDto.getPassengerCount());
        assertEquals(trip.getTripDistance(), tripDto.getTripDistance());
        assertEquals(trip.getRateCodeId(), tripDto.getRateCodeId());
        assertEquals(trip.getStoreAndFwdFlag(), tripDto.getStoreAndFwdFlag());
        assertEquals(trip.getPickupLocationId(), tripDto.getPickupLocationId());
        assertEquals(trip.getDropoffLocationId(), tripDto.getDropoffLocationId());
        assertEquals(trip.getPaymentType(), tripDto.getPaymentType());
        assertEquals(trip.getFareAmount(), tripDto.getFareAmount());
        assertEquals(trip.getExtra(), tripDto.getExtra());
        assertEquals(trip.getMtaTax(), tripDto.getMtaTax());
        assertEquals(trip.getTipAmount(), tripDto.getTipAmount());
        assertEquals(trip.getTollsAmount(), tripDto.getTollsAmount());
        assertEquals(trip.getImprovementSurcharge(), tripDto.getImprovementSurcharge());
        assertEquals(trip.getTotalAmount(), tripDto.getTotalAmount());
        assertEquals(trip.getCongestionSurcharge(), tripDto.getCongestionSurcharge());
        assertEquals(trip.getAirportFee(), tripDto.getAirportFee());
    }

    @Test
    void shouldHandleNullTrip() {
        // when
        TripDto tripDto = mapper.tripToTripDto(null);

        // then
        assertNull(tripDto);
    }

    @Test
    void shouldHandleNullFields() {
        // given
        Trip trip = new Trip();
        // все поля null по умолчанию

        // when
        TripDto tripDto = mapper.tripToTripDto(trip);

        // then
        assertNotNull(tripDto);
        assertNull(tripDto.getId());
        assertNull(tripDto.getVendorId());
        assertNull(tripDto.getPickupDatetime());
        assertNull(tripDto.getDropoffDatetime());
        assertNull(tripDto.getPassengerCount());
        assertNull(tripDto.getTripDistance());
        assertNull(tripDto.getRateCodeId());
        assertNull(tripDto.getStoreAndFwdFlag());
        assertNull(tripDto.getPickupLocationId());
        assertNull(tripDto.getDropoffLocationId());
        assertNull(tripDto.getPaymentType());
        assertNull(tripDto.getFareAmount());
        assertNull(tripDto.getExtra());
        assertNull(tripDto.getMtaTax());
        assertNull(tripDto.getTipAmount());
        assertNull(tripDto.getTollsAmount());
        assertNull(tripDto.getImprovementSurcharge());
        assertNull(tripDto.getTotalAmount());
        assertNull(tripDto.getCongestionSurcharge());
        assertNull(tripDto.getAirportFee());
    }

    @Test
    void shouldPreserveNumericPrecision() {
        // given
        Trip trip = new Trip();
        trip.setTripDistance(15.55555);
        trip.setFareAmount(25.99999);
        trip.setTotalAmount(41.54554);

        // when
        TripDto tripDto = mapper.tripToTripDto(trip);

        // then
        assertNotNull(tripDto);
        assertEquals(trip.getTripDistance(), tripDto.getTripDistance());
        assertEquals(trip.getFareAmount(), tripDto.getFareAmount());
        assertEquals(trip.getTotalAmount(), tripDto.getTotalAmount());
    }
}