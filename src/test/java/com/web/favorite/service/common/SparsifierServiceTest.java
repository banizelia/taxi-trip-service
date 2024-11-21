//package com.web.favorite.service.common;
//
//import com.web.common.exception.position.PositionOverflowException;
//import com.web.favorite.config.FavoriteTripListConf;
//import com.web.favorite.model.FavoriteTrip;
//import com.web.favorite.repository.FavoriteTripRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class SparsifierServiceTest {
//
//    @Mock
//    private FavoriteTripRepository repository;
//
//    @Mock
//    private FavoriteTripListConf config;
//
//    @InjectMocks
//    private SparsifierService sparsifier;
//
//    @Test
//    void getNextAvailablePosition_NoRebalancing() {
//        when(repository.findMaxPosition()).thenReturn(Optional.of(1000L));
//
//        long result = sparsifier.getNextAvailablePosition();
//
//        assertEquals(2000L, result);
//        verify(repository, never()).findAllByOrderByPositionAsc();
//    }
//
//    @Test
//    void getNextAvailablePosition_WithRebalancing() {
//        when(repository.findMaxPosition())
//                .thenReturn(Optional.of(Long.MAX_VALUE - 100L))
//                .thenReturn(Optional.of(1000L));
//        when(repository.findAllByOrderByPositionAsc()).thenReturn(Collections.emptyList());
//
//        long result = sparsifier.getNextAvailablePosition();
//
//        assertEquals(2000L, result);
//        verify(repository).findAllByOrderByPositionAsc();
//    }
//
//    @Test
//    void getNextAvailablePosition_PositionOverflow() {
//        when(repository.findMaxPosition())
//                .thenReturn(Optional.of(Long.MAX_VALUE - 100L))
//                .thenReturn(Optional.of(Long.MAX_VALUE - 100L));
//        when(repository.findAllByOrderByPositionAsc()).thenReturn(Collections.emptyList());
//
//        assertThrows(PositionOverflowException.class,
//                () -> sparsifier.getNextAvailablePosition());
//    }
//
//    @Test
//    void sparsify_EmptyList() {
//        when(repository.findAllByOrderByPositionAsc()).thenReturn(Collections.emptyList());
//
//        sparsifier.sparsify();
//
//        verify(repository, never()).saveAll(anyList());
//    }
//
//    @Test
//    void sparsify_WithTrips() {
//        FavoriteTrip trip1 = new FavoriteTrip();
//        FavoriteTrip trip2 = new FavoriteTrip();
//        when(repository.findAllByOrderByPositionAsc()).thenReturn(Arrays.asList(trip1, trip2));
//
//        sparsifier.sparsify();
//
//        verify(repository).saveAll(anyList());
//        assertEquals(1000L, trip1.getPosition());
//        assertEquals(2000L, trip2.getPosition());
//    }
//
//    @Test
//    void getNextAvailablePosition_NoExistingPositions() {
//        when(repository.findMaxPosition()).thenReturn(Optional.empty());
//
//        long result = sparsifier.getNextAvailablePosition();
//
//        assertEquals(1000L, result);
//    }
//}