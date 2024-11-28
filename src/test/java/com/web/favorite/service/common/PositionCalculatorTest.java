//package com.web.favorite.service.common;
//
//import com.web.favorite.config.FavoriteTripListConf;
//import com.web.favorite.repository.FavoriteTripRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PositionCalculatorTest {
//
//    @Mock
//    private FavoriteTripListConf config;
//
//    @Mock
//    private SparsifierService sparsifier;
//
//    @Mock
//    private FavoriteTripRepository repository;
//
//    @InjectMocks
//    private PositionCalculator calculator;
//
//    @Test
//    void calculateFirstPosition_NoSparsification() {
//        when(repository.count()).thenReturn(5L);
//        when(repository.findMinPosition()).thenReturn(Optional.of(200L));
//
//        long result = calculator.calculateNewPosition(1L);
//
//        assertEquals(100L, result);
//        verify(sparsifier, never()).sparsify();
//    }
//
//    @Test
//    void calculateFirstPosition_WithSparsification() {
//        when(repository.count()).thenReturn(5L);
//        when(repository.findMinPosition())
//                .thenReturn(Optional.of(50L))
//                .thenReturn(Optional.of(200L));
//
//        long result = calculator.calculateNewPosition(1L);
//
//        assertEquals(25, result);
//    }
//
//    @Test
//    void calculateMiddlePosition_NoSparsification() {
//        when(repository.count()).thenReturn(5L);
//        when(repository.findPositionByIndex(1L)).thenReturn(Optional.of(1000L));
//        when(repository.findPositionByIndex(2L)).thenReturn(Optional.of(2000L));
//
//        long result = calculator.calculateNewPosition(3L);
//
//        assertEquals(1500L, result);
//        verify(sparsifier, never()).sparsify();
//    }
//
//    @Test
//    void calculateMiddlePosition_WithSparsification() {
//        when(repository.count()).thenReturn(5L);
//        when(repository.findPositionByIndex(1L))
//                .thenReturn(Optional.of(100L))
//                .thenReturn(Optional.of(1000L));
//        when(repository.findPositionByIndex(2L))
//                .thenReturn(Optional.of(150L))
//                .thenReturn(Optional.of(2000L));
//
//        long result = calculator.calculateNewPosition(3L);
//
//        assertEquals(125, result);
//    }
//
//    @Test
//    void calculateLastPosition_Empty() {
//        when(sparsifier.getNextAvailablePosition()).thenReturn(0L);
//
//        long result = calculator.calculateLastPosition();
//
//        assertEquals(0, result);
//    }
//
//    @Test
//    void calculateLastPosition_WithExistingPositions() {
//        when(sparsifier.getNextAvailablePosition()).thenReturn(2000L);
//
//        long result = calculator.calculateLastPosition();
//
//        assertEquals(2000L, result);
//    }
//}