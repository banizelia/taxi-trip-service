package com.banizelia.taxi.trip.mapper;

import com.banizelia.taxi.favorite.model.FavoriteTrip;
import com.banizelia.taxi.trip.model.Trip;
import com.banizelia.taxi.trip.model.TripDto;
import com.banizelia.taxi.weather.model.Weather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TripMapperTest {

    private Trip trip;
    private final LocalDateTime localDateTime = LocalDateTime.of(2024, 1, 1, 12, 0);
    private final LocalDate localDate = LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setId(1L);
        trip.setVendorId(1);
        trip.setPickupDatetime(localDateTime);
        trip.setDropoffDatetime(localDateTime.plusHours(1));
        trip.setPassengerCount(2);
        trip.setTripDistance(10.5);
        trip.setRateCodeId(1);
        trip.setStoreAndFwdFlag("Y");
        trip.setPickupLocationId(100);
        trip.setDropoffLocationId(200);
        trip.setPaymentType(1);
        trip.setFareAmount(25.50);
        trip.setExtra(2.0);
        trip.setMtaTax(0.5);
        trip.setTipAmount(5.0);
        trip.setTollsAmount(6.0);
        trip.setImprovementSurcharge(0.3);
        trip.setTotalAmount(39.30);
        trip.setCongestionSurcharge(2.5);
        trip.setAirportFee(1.5);
        trip.setPickupDate(localDate);
    }

    @Test
    void testTripToTripDto_WhenAllFieldsAreSet() {
        TripDto dto = TripMapper.INSTANCE.tripToTripDto(trip);

        assertNotNull(dto);
        assertAll(
                () -> assertEquals(trip.getId(), dto.getId()),
                () -> assertEquals(trip.getVendorId(), dto.getVendorId()),
                () -> assertEquals(trip.getPickupDatetime(), dto.getPickupDatetime()),
                () -> assertEquals(trip.getDropoffDatetime(), dto.getDropoffDatetime()),
                () -> assertEquals(trip.getPassengerCount(), dto.getPassengerCount()),
                () -> assertEquals(trip.getTripDistance(), dto.getTripDistance()),
                () -> assertEquals(trip.getRateCodeId(), dto.getRateCodeId()),
                () -> assertEquals(trip.getStoreAndFwdFlag(), dto.getStoreAndFwdFlag()),
                () -> assertEquals(trip.getPickupLocationId(), dto.getPickupLocationId()),
                () -> assertEquals(trip.getDropoffLocationId(), dto.getDropoffLocationId()),
                () -> assertEquals(trip.getPaymentType(), dto.getPaymentType()),
                () -> assertEquals(trip.getFareAmount(), dto.getFareAmount()),
                () -> assertEquals(trip.getExtra(), dto.getExtra()),
                () -> assertEquals(trip.getMtaTax(), dto.getMtaTax()),
                () -> assertEquals(trip.getTipAmount(), dto.getTipAmount()),
                () -> assertEquals(trip.getTollsAmount(), dto.getTollsAmount()),
                () -> assertEquals(trip.getImprovementSurcharge(), dto.getImprovementSurcharge()),
                () -> assertEquals(trip.getTotalAmount(), dto.getTotalAmount()),
                () -> assertEquals(trip.getCongestionSurcharge(), dto.getCongestionSurcharge()),
                () -> assertEquals(trip.getAirportFee(), dto.getAirportFee())
        );
    }

    @Test
    void testTripToTripDto_WhenTripIsNull() {
        TripDto dto = TripMapper.INSTANCE.tripToTripDto(null);

        assertNull(dto);
    }

    @Test
    void testTripToTripDto_WhenAllFieldsAreNull() {
        Trip emptyTrip = new Trip();

        TripDto dto = TripMapper.INSTANCE.tripToTripDto(emptyTrip);

        assertNotNull(dto);
        assertAll(
                () -> assertNull(dto.getId()),
                () -> assertNull(dto.getVendorId()),
                () -> assertNull(dto.getPickupDatetime()),
                () -> assertNull(dto.getDropoffDatetime()),
                () -> assertNull(dto.getPassengerCount()),
                () -> assertNull(dto.getTripDistance()),
                () -> assertNull(dto.getRateCodeId()),
                () -> assertNull(dto.getStoreAndFwdFlag()),
                () -> assertNull(dto.getPickupLocationId()),
                () -> assertNull(dto.getDropoffLocationId()),
                () -> assertNull(dto.getPaymentType()),
                () -> assertNull(dto.getFareAmount()),
                () -> assertNull(dto.getExtra()),
                () -> assertNull(dto.getMtaTax()),
                () -> assertNull(dto.getTipAmount()),
                () -> assertNull(dto.getTollsAmount()),
                () -> assertNull(dto.getImprovementSurcharge()),
                () -> assertNull(dto.getTotalAmount()),
                () -> assertNull(dto.getCongestionSurcharge()),
                () -> assertNull(dto.getAirportFee())
        );
    }

    @Test
    void testTripToTripDto_WithRelatedEntities() {
        Weather weather = new Weather();
        weather.setId(1L);
        weather.setDate(localDate);
        trip.setWeather(weather);

        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setId(1L);
        favoriteTrip.setTripId(1L);
        trip.setFavoriteTrip(favoriteTrip);

        TripDto dto = TripMapper.INSTANCE.tripToTripDto(trip);

        assertNotNull(dto);
        assertEquals(trip.getId(), dto.getId());
        assertEquals(trip.getVendorId(), dto.getVendorId());
        assertNotNull(trip.getWeather());
        assertNotNull(trip.getFavoriteTrip());
    }

    @Test
    void testTripToTripDto_NumericValues() {
        Trip numericTrip = new Trip();
        numericTrip.setPassengerCount(0);
        numericTrip.setTripDistance(0.0);
        numericTrip.setFareAmount(0.0);
        numericTrip.setTotalAmount(-1.0);

        TripDto dto = TripMapper.INSTANCE.tripToTripDto(numericTrip);


        assertNotNull(dto);
        assertEquals(0, dto.getPassengerCount());
        assertEquals(0.0, dto.getTripDistance());
        assertEquals(0.0, dto.getFareAmount());
        assertEquals(-1.0, dto.getTotalAmount());
    }

    @Test
    void testTripToTripDto_DateTimeValues() {
        Trip dateTimeTrip = new Trip();
        LocalDateTime now = LocalDateTime.now();
        dateTimeTrip.setPickupDatetime(now);
        dateTimeTrip.setDropoffDatetime(now.plusHours(1));

        TripDto dto = TripMapper.INSTANCE.tripToTripDto(dateTimeTrip);

        assertNotNull(dto);
        assertEquals(now, dto.getPickupDatetime());
        assertEquals(now.plusHours(1), dto.getDropoffDatetime());
    }
}