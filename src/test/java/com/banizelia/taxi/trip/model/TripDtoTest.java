package com.banizelia.taxi.trip.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TripDtoTest {

    private TripDto tripDto;
    private final LocalDateTime testDateTime = LocalDateTime.of(2024, 1, 1, 12, 0);

    @BeforeEach
    void setUp() {
        tripDto = new TripDto();
    }

    @Test
    void testNoArgsConstructor() {
        assertNotNull(tripDto);
        assertNull(tripDto.getId());
        assertNull(tripDto.getVendorId());
    }

    @Nested
    class GetterSetterTests {
        @Test
        void testIdGetterSetter() {
            Long id = 1L;
            tripDto.setId(id);
            assertEquals(id, tripDto.getId());
        }

        @Test
        void testVendorIdGetterSetter() {
            String vendorId = "V123";
            tripDto.setVendorId(vendorId);
            assertEquals(vendorId, tripDto.getVendorId());
        }

        @Test
        void testPickupDatetimeGetterSetter() {
            tripDto.setPickupDatetime(testDateTime);
            assertEquals(testDateTime, tripDto.getPickupDatetime());
        }

        @Test
        void testDropoffDatetimeGetterSetter() {
            tripDto.setDropoffDatetime(testDateTime);
            assertEquals(testDateTime, tripDto.getDropoffDatetime());
        }

        @Test
        void testPassengerCountGetterSetter() {
            Integer count = 2;
            tripDto.setPassengerCount(count);
            assertEquals(count, tripDto.getPassengerCount());
        }

        @Test
        void testTripDistanceGetterSetter() {
            Double distance = 10.5;
            tripDto.setTripDistance(distance);
            assertEquals(distance, tripDto.getTripDistance());
        }

        @Test
        void testLocationIdsGetterSetter() {
            Integer pickupId = 100;
            Integer dropoffId = 200;
            tripDto.setPickupLocationId(pickupId);
            tripDto.setDropoffLocationId(dropoffId);
            assertEquals(pickupId, tripDto.getPickupLocationId());
            assertEquals(dropoffId, tripDto.getDropoffLocationId());
        }

        @Test
        void testPaymentFieldsGetterSetter() {
            Double fareAmount = 25.50;
            Double extra = 2.0;
            Double mtaTax = 0.5;
            Double tipAmount = 5.0;
            Double tollsAmount = 6.0;
            Double improvementSurcharge = 0.3;
            Double totalAmount = 39.30;
            Double congestionSurcharge = 2.5;
            Double airportFee = 1.5;

            tripDto.setFareAmount(fareAmount);
            tripDto.setExtra(extra);
            tripDto.setMtaTax(mtaTax);
            tripDto.setTipAmount(tipAmount);
            tripDto.setTollsAmount(tollsAmount);
            tripDto.setImprovementSurcharge(improvementSurcharge);
            tripDto.setTotalAmount(totalAmount);
            tripDto.setCongestionSurcharge(congestionSurcharge);
            tripDto.setAirportFee(airportFee);

            assertEquals(fareAmount, tripDto.getFareAmount());
            assertEquals(extra, tripDto.getExtra());
            assertEquals(mtaTax, tripDto.getMtaTax());
            assertEquals(tipAmount, tripDto.getTipAmount());
            assertEquals(tollsAmount, tripDto.getTollsAmount());
            assertEquals(improvementSurcharge, tripDto.getImprovementSurcharge());
            assertEquals(totalAmount, tripDto.getTotalAmount());
            assertEquals(congestionSurcharge, tripDto.getCongestionSurcharge());
            assertEquals(airportFee, tripDto.getAirportFee());
        }
    }

    @Test
    void testEquals() {
        TripDto tripDto1 = new TripDto();
        TripDto tripDto2 = new TripDto();

        tripDto1.setId(1L);
        tripDto2.setId(1L);
        tripDto1.setVendorId("V123");
        tripDto2.setVendorId("V123");

        assertEquals(tripDto1, tripDto2);
        assertEquals(tripDto1.hashCode(), tripDto2.hashCode());

        tripDto2.setId(2L);
        assertNotEquals(tripDto1, tripDto2);
        assertNotEquals(tripDto1.hashCode(), tripDto2.hashCode());
    }

    @Test
    void testToString() {
        tripDto.setId(1L);
        tripDto.setVendorId("V123");
        tripDto.setPickupDatetime(testDateTime);
        tripDto.setPassengerCount(2);

        String toString = tripDto.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("vendorId=V123"));
        assertTrue(toString.contains("pickupDatetime=" + testDateTime));
        assertTrue(toString.contains("passengerCount=2"));
    }

    @Test
    void testCompleteDto() {
        tripDto.setId(1L);
        tripDto.setVendorId("V123");
        tripDto.setPickupDatetime(testDateTime);
        tripDto.setDropoffDatetime(testDateTime.plusHours(1));
        tripDto.setPassengerCount(2);
        tripDto.setTripDistance(10.5);
        tripDto.setRateCodeId("R1");
        tripDto.setStoreAndFwdFlag("Y");
        tripDto.setPickupLocationId(100);
        tripDto.setDropoffLocationId(200);
        tripDto.setPaymentType("CREDIT");
        tripDto.setFareAmount(25.50);
        tripDto.setExtra(2.0);
        tripDto.setMtaTax(0.5);
        tripDto.setTipAmount(5.0);
        tripDto.setTollsAmount(6.0);
        tripDto.setImprovementSurcharge(0.3);
        tripDto.setTotalAmount(39.30);
        tripDto.setCongestionSurcharge(2.5);
        tripDto.setAirportFee(1.5);

        assertAll(
                () -> assertEquals(1L, tripDto.getId()),
                () -> assertEquals("V123", tripDto.getVendorId()),
                () -> assertEquals(testDateTime, tripDto.getPickupDatetime()),
                () -> assertEquals(testDateTime.plusHours(1), tripDto.getDropoffDatetime()),
                () -> assertEquals(2, tripDto.getPassengerCount()),
                () -> assertEquals(10.5, tripDto.getTripDistance()),
                () -> assertEquals("R1", tripDto.getRateCodeId()),
                () -> assertEquals("Y", tripDto.getStoreAndFwdFlag()),
                () -> assertEquals(100, tripDto.getPickupLocationId()),
                () -> assertEquals(200, tripDto.getDropoffLocationId()),
                () -> assertEquals("CREDIT", tripDto.getPaymentType()),
                () -> assertEquals(25.50, tripDto.getFareAmount()),
                () -> assertEquals(2.0, tripDto.getExtra()),
                () -> assertEquals(0.5, tripDto.getMtaTax()),
                () -> assertEquals(5.0, tripDto.getTipAmount()),
                () -> assertEquals(6.0, tripDto.getTollsAmount()),
                () -> assertEquals(0.3, tripDto.getImprovementSurcharge()),
                () -> assertEquals(39.30, tripDto.getTotalAmount()),
                () -> assertEquals(2.5, tripDto.getCongestionSurcharge()),
                () -> assertEquals(1.5, tripDto.getAirportFee())
        );
    }

    @Test
    void testBuilder() {
        // Test the builder pattern generated by Lombok @Data
        TripDto dto = new TripDto();
        dto.setId(1L);
        dto.setVendorId("V123");

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("V123", dto.getVendorId());
    }

    @Test
    void testEqualsWithNullFields() {
        TripDto dto1 = new TripDto();
        TripDto dto2 = new TripDto();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testEqualsWithDifferentTypes() {
        TripDto dto = new TripDto();
        Object other = new Object();

        assertNotEquals(dto, other);
        assertNotEquals(null, dto);
    }
}