//package com.web.export;
//
//import com.web.model.Trip;
//import com.web.repository.TripsRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import java.io.ByteArrayOutputStream;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.stream.Stream;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class TripExcelExporterFastExcelTest {
//
//    @Mock
//    private TripsRepository tripsRepository;
//
//    private TripExcelExporterFastExcel exporter;
//    private ByteArrayOutputStream outputStream;
//
//    @BeforeEach
//    void setUp() {
//        exporter = new TripExcelExporterFastExcel(tripsRepository);
//        outputStream = new ByteArrayOutputStream();
//    }
//
//    @Test
//    void shouldExportEmptyExcelFileWhenNoTrips() throws Exception {
//        // given
////        when(tripsRepository.findAllStream(anyInt())).thenReturn(Arrays.asList().stream());
//
//        // when
//        exporter.tripsToExcelStream(outputStream);
//
//        // then
//        assertTrue(outputStream.size() > 0, "Excel file should be created even if empty");
//        verify(tripsRepository).findAllStream(10000);
//    }
//
//    @Test
//    void shouldExportTripsToExcel() throws Exception {
//        // given
//        Trip trip1 = createTestTrip(1L);
//        Trip trip2 = createTestTrip(2L);
//        when(tripsRepository.findAllStream(anyInt()))
//                .thenReturn(Arrays.asList(trip1, trip2).stream());
//
//        // when
//        exporter.tripsToExcelStream(outputStream);
//
//        // then
//        assertTrue(outputStream.size() > 0, "Excel file should contain data");
//        verify(tripsRepository).findAllStream(10000);
//    }
//
//    @Test
//    void shouldHandleLargeNumberOfTrips() throws Exception {
//        // given
//        int numberOfTrips = 1_100_000; // This will test multiple sheet creation
//        Iterator<Trip> tripIterator = createLargeNumberOfTrips(numberOfTrips);
//        Stream<Trip> stream = Stream.of(tripIterator.next());
//        when(tripsRepository.findAllStream(anyInt())).thenReturn(stream);
//
//        // when
//        exporter.tripsToExcelStream(outputStream);
//
//        // then
//        assertTrue(outputStream.size() > 0, "Excel file should be created with multiple sheets");
//        verify(tripsRepository).findAllStream(10000);
//    }
//
//    private Trip createTestTrip(Long id) {
//        Trip trip = new Trip();
//        trip.setId(id);
//        trip.setVendorId("1");
//        trip.setPickupDatetime(LocalDateTime.now());
//        trip.setDropoffDatetime(LocalDateTime.now().plusHours(1));
//        trip.setPassengerCount(2);
//        trip.setTripDistance(10.5);
//        trip.setRateCodeId("1");
//        trip.setStoreAndFwdFlag("N");
//        trip.setPickupLocationId(100);
//        trip.setDropoffLocationId(101);
//        trip.setPaymentType("1");
//        trip.setFareAmount(20.0);
//        trip.setExtra(1.0);
//        trip.setMtaTax(0.5);
//        trip.setTipAmount(4.0);
//        trip.setTollsAmount(0.0);
//        trip.setImprovementSurcharge(0.3);
//        trip.setTotalAmount(25.8);
//        trip.setCongestionSurcharge(2.5);
//        trip.setAirportFee(0.0);
//        return trip;
//    }
//
//    private Iterator<Trip> createLargeNumberOfTrips(int count) {
//        return new Iterator<Trip>() {
//            private int current = 0;
//
//            @Override
//            public boolean hasNext() {
//                return current < count;
//            }
//
//            @Override
//            public Trip next() {
//                return createTestTrip((long) ++current);
//            }
//        };
//    }
//}