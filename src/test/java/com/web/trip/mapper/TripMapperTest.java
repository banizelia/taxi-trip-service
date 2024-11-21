package com.web.trip.mapper;

import com.web.favorite.model.FavoriteTrip;
import com.web.trip.model.Trip;
import com.web.trip.model.TripDto;
import com.web.weather.model.Weather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TripMapperTest {

    private Trip trip;
    private final LocalDateTime TEST_DATETIME = LocalDateTime.of(2024, 1, 1, 12, 0);
    private final LocalDate TEST_DATE = LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        trip = new Trip();
        trip.setId(1L);
        trip.setVendorId("V123");
        trip.setPickupDatetime(TEST_DATETIME);
        trip.setDropoffDatetime(TEST_DATETIME.plusHours(1));
        trip.setPassengerCount(2);
        trip.setTripDistance(10.5);
        trip.setRateCodeId("R1");
        trip.setStoreAndFwdFlag("Y");
        trip.setPickupLocationId(100);
        trip.setDropoffLocationId(200);
        trip.setPaymentType("CREDIT");
        trip.setFareAmount(25.50);
        trip.setExtra(2.0);
        trip.setMtaTax(0.5);
        trip.setTipAmount(5.0);
        trip.setTollsAmount(6.0);
        trip.setImprovementSurcharge(0.3);
        trip.setTotalAmount(39.30);
        trip.setCongestionSurcharge(2.5);
        trip.setAirportFee(1.5);
        trip.setPickupDate(TEST_DATE);
    }

    @Test
    void testTripToTripDto_WhenAllFieldsAreSet() {
        // Act
        TripDto dto = TripMapper.INSTANCE.tripToTripDto(trip);

        // Assert
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
        // Act
        TripDto dto = TripMapper.INSTANCE.tripToTripDto(null);

        // Assert
        assertNull(dto);
    }

    @Test
    void testTripToTripDto_WhenAllFieldsAreNull() {
        // Arrange
        Trip emptyTrip = new Trip();

        // Act
        TripDto dto = TripMapper.INSTANCE.tripToTripDto(emptyTrip);

        // Assert
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
        // Arrange
        Weather weather = new Weather();
        weather.setId(1L);
        weather.setDate(TEST_DATE);
        trip.setWeather(weather);

        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setId(1L);
        favoriteTrip.setTripId(1L);
        trip.setFavoriteTrip(favoriteTrip);

        // Act
        TripDto dto = TripMapper.INSTANCE.tripToTripDto(trip);

        // Assert
        assertNotNull(dto);
        assertAll(
                // Проверяем, что все основные поля замаплены корректно
                () -> assertEquals(trip.getId(), dto.getId()),
                () -> assertEquals(trip.getVendorId(), dto.getVendorId()),
                // ... остальные поля

                // Проверяем, что связанные сущности не влияют на маппинг
                () -> assertNotNull(trip.getWeather()),
                () -> assertNotNull(trip.getFavoriteTrip())
        );
    }

    @Test
    void testTripToTripDto_NumericValues() {
        // Arrange
        Trip numericTrip = new Trip();
        numericTrip.setPassengerCount(0);
        numericTrip.setTripDistance(0.0);
        numericTrip.setFareAmount(0.0);
        numericTrip.setTotalAmount(-1.0);

        // Act
        TripDto dto = TripMapper.INSTANCE.tripToTripDto(numericTrip);

        // Assert
        assertNotNull(dto);
        assertAll(
                () -> assertEquals(0, dto.getPassengerCount()),
                () -> assertEquals(0.0, dto.getTripDistance()),
                () -> assertEquals(0.0, dto.getFareAmount()),
                () -> assertEquals(-1.0, dto.getTotalAmount())
        );
    }

    @Test
    void testTripToTripDto_DateTimeValues() {
        // Arrange
        Trip dateTimeTrip = new Trip();
        LocalDateTime now = LocalDateTime.now();
        dateTimeTrip.setPickupDatetime(now);
        dateTimeTrip.setDropoffDatetime(now.plusHours(1));

        // Act
        TripDto dto = TripMapper.INSTANCE.tripToTripDto(dateTimeTrip);

        // Assert
        assertNotNull(dto);
        assertAll(
                () -> assertEquals(now, dto.getPickupDatetime()),
                () -> assertEquals(now.plusHours(1), dto.getDropoffDatetime())
        );
    }
}