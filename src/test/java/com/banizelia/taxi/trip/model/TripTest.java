package com.banizelia.taxi.trip.model;

import com.banizelia.taxi.favorite.model.FavoriteTrip;
import com.banizelia.taxi.weather.model.Weather;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TripTest {

    private Trip trip;
    private final LocalDateTime localDateTime = LocalDateTime.of(2024, 1, 1, 12, 0);
    private final LocalDate localDate = LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        trip = new Trip();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(trip);
    }

    @Nested
    class BasicFieldsTests {
        @Test
        void testIdField() {
            Long id = 1L;
            trip.setId(id);
            assertEquals(id, trip.getId());
        }

        @Test
        void testVendorIdField() {
            String vendorId = "V123";
            trip.setVendorId(vendorId);
            assertEquals(vendorId, trip.getVendorId());
        }

        @Test
        void testPickupDatetimeField() {
            trip.setPickupDatetime(localDateTime);
            assertEquals(localDateTime, trip.getPickupDatetime());
        }

        @Test
        void testDropoffDatetimeField() {
            trip.setDropoffDatetime(localDateTime);
            assertEquals(localDateTime, trip.getDropoffDatetime());
        }

        @Test
        void testPassengerCountField() {
            Integer count = 2;
            trip.setPassengerCount(count);
            assertEquals(count, trip.getPassengerCount());
        }

        @Test
        void testTripDistanceField() {
            Double distance = 10.5;
            trip.setTripDistance(distance);
            assertEquals(distance, trip.getTripDistance());
        }

        @Test
        void testRateCodeIdField() {
            String rateCodeId = "R1";
            trip.setRateCodeId(rateCodeId);
            assertEquals(rateCodeId, trip.getRateCodeId());
        }

        @Test
        void testStoreAndFwdFlagField() {
            String flag = "Y";
            trip.setStoreAndFwdFlag(flag);
            assertEquals(flag, trip.getStoreAndFwdFlag());
        }
    }

    @Nested
    class LocationFieldsTests {
        @Test
        void testPickupLocationIdField() {
            Integer locationId = 100;
            trip.setPickupLocationId(locationId);
            assertEquals(locationId, trip.getPickupLocationId());
        }

        @Test
        void testDropoffLocationIdField() {
            Integer locationId = 200;
            trip.setDropoffLocationId(locationId);
            assertEquals(locationId, trip.getDropoffLocationId());
        }
    }

    @Nested
    class PaymentFieldsTests {
        @Test
        void testPaymentTypeField() {
            String paymentType = "CREDIT";
            trip.setPaymentType(paymentType);
            assertEquals(paymentType, trip.getPaymentType());
        }

        @Test
        void testFareAmountField() {
            Double fareAmount = 25.50;
            trip.setFareAmount(fareAmount);
            assertEquals(fareAmount, trip.getFareAmount());
        }

        @Test
        void testExtraField() {
            Double extra = 2.0;
            trip.setExtra(extra);
            assertEquals(extra, trip.getExtra());
        }

        @Test
        void testMtaTaxField() {
            Double mtaTax = 0.5;
            trip.setMtaTax(mtaTax);
            assertEquals(mtaTax, trip.getMtaTax());
        }

        @Test
        void testTipAmountField() {
            Double tipAmount = 5.0;
            trip.setTipAmount(tipAmount);
            assertEquals(tipAmount, trip.getTipAmount());
        }

        @Test
        void testTollsAmountField() {
            Double tollsAmount = 6.0;
            trip.setTollsAmount(tollsAmount);
            assertEquals(tollsAmount, trip.getTollsAmount());
        }

        @Test
        void testImprovementSurchargeField() {
            Double improvementSurcharge = 0.3;
            trip.setImprovementSurcharge(improvementSurcharge);
            assertEquals(improvementSurcharge, trip.getImprovementSurcharge());
        }

        @Test
        void testTotalAmountField() {
            Double totalAmount = 39.30;
            trip.setTotalAmount(totalAmount);
            assertEquals(totalAmount, trip.getTotalAmount());
        }

        @Test
        void testCongestionSurchargeField() {
            Double congestionSurcharge = 2.5;
            trip.setCongestionSurcharge(congestionSurcharge);
            assertEquals(congestionSurcharge, trip.getCongestionSurcharge());
        }

        @Test
        void testAirportFeeField() {
            Double airportFee = 1.5;
            trip.setAirportFee(airportFee);
            assertEquals(airportFee, trip.getAirportFee());
        }
    }

    @Nested
    class RelationshipTests {
        @Test
        void testWeatherRelationship() {
            Weather weather = new Weather();
            weather.setId(1L);
            weather.setDate(localDate);

            trip.setWeather(weather);

            assertNotNull(trip.getWeather());
            assertEquals(weather.getId(), trip.getWeather().getId());
            assertEquals(weather.getDate(), trip.getWeather().getDate());
        }

        @Test
        void testFavoriteTripRelationship() {
            FavoriteTrip favoriteTrip = new FavoriteTrip();
            favoriteTrip.setId(1L);
            favoriteTrip.setTripId(1L);

            trip.setFavoriteTrip(favoriteTrip);

            assertNotNull(trip.getFavoriteTrip());
            assertEquals(favoriteTrip.getId(), trip.getFavoriteTrip().getId());
            assertEquals(favoriteTrip.getTripId(), trip.getFavoriteTrip().getTripId());
        }
    }

    @Test
    void testCompleteTrip() {
        trip.setId(1L);
        trip.setVendorId("V123");
        trip.setPickupDatetime(localDateTime);
        trip.setDropoffDatetime(localDateTime.plusHours(1));
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
        trip.setPickupDate(localDate);

        Weather weather = new Weather();
        weather.setId(1L);
        weather.setDate(localDate);
        trip.setWeather(weather);

        FavoriteTrip favoriteTrip = new FavoriteTrip();
        favoriteTrip.setId(1L);
        favoriteTrip.setTripId(1L);
        trip.setFavoriteTrip(favoriteTrip);

        assertAll(
                () -> assertEquals(1L, trip.getId()),
                () -> assertEquals("V123", trip.getVendorId()),
                () -> assertEquals(localDateTime, trip.getPickupDatetime()),
                () -> assertEquals(localDateTime.plusHours(1), trip.getDropoffDatetime()),
                () -> assertEquals(2, trip.getPassengerCount()),
                () -> assertEquals(10.5, trip.getTripDistance()),
                () -> assertEquals("R1", trip.getRateCodeId()),
                () -> assertEquals("Y", trip.getStoreAndFwdFlag()),
                () -> assertEquals(100, trip.getPickupLocationId()),
                () -> assertEquals(200, trip.getDropoffLocationId()),
                () -> assertEquals("CREDIT", trip.getPaymentType()),
                () -> assertEquals(25.50, trip.getFareAmount()),
                () -> assertEquals(2.0, trip.getExtra()),
                () -> assertEquals(0.5, trip.getMtaTax()),
                () -> assertEquals(5.0, trip.getTipAmount()),
                () -> assertEquals(6.0, trip.getTollsAmount()),
                () -> assertEquals(0.3, trip.getImprovementSurcharge()),
                () -> assertEquals(39.30, trip.getTotalAmount()),
                () -> assertEquals(2.5, trip.getCongestionSurcharge()),
                () -> assertEquals(1.5, trip.getAirportFee()),
                () -> assertEquals(localDate, trip.getPickupDate()),
                () -> assertNotNull(trip.getWeather()),
                () -> assertNotNull(trip.getFavoriteTrip())
        );
    }
}