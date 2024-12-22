package com.banizelia.taxi.favorite.service.common;

import com.banizelia.taxi.config.FavoriteTripListConfig;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import com.banizelia.taxi.favorite.service.position.PositionCalculator;
import com.banizelia.taxi.favorite.service.position.Sparsifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PositionCalculatorTest {

    @Mock
    private FavoriteTripListConfig config;

    @Mock
    private Sparsifier sparsifier;

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @InjectMocks
    private PositionCalculator positionCalculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class CalculateNewPositionTests {

        @Test
        void calculateNewPosition_TargetPositionOne_FirstPositionValid() {
            Long targetPosition = 1L;
            long totalCount = 5L;
            long firstPosition = 20000000L;
            long minGap = 100L;

            when(favoriteTripRepository.count()).thenReturn(totalCount);
            when(favoriteTripRepository.findMinPosition()).thenReturn(Optional.of(firstPosition));
            when(config.getMinGap()).thenReturn(minGap);

            long newPosition = positionCalculator.calculateNewPosition(targetPosition);

            assertEquals(firstPosition / 2, newPosition);
            verify(favoriteTripRepository, times(1)).count();
            verify(favoriteTripRepository, times(1)).findMinPosition();
            verify(config, times(1)).getMinGap();
            verify(sparsifier, never()).sparsify();
        }

        @Test
        void calculateNewPosition_TargetPositionOne_FirstPositionRequiresSparsify() {
            Long targetPosition = 1L;
            long totalCount = 5L;
            long initialPosition = 10000000L;
            long firstPosition = 50L; // Less than minGap
            long minGap = 100L;

            when(favoriteTripRepository.count()).thenReturn(totalCount);
            when(favoriteTripRepository.findMinPosition()).thenReturn(Optional.of(firstPosition))
                    .thenReturn(Optional.of(firstPosition));
            when(config.getMinGap()).thenReturn(minGap);
            when(config.getInitialPosition()).thenReturn(initialPosition);

            long newPosition = positionCalculator.calculateNewPosition(targetPosition);

            assertEquals(firstPosition / 2, newPosition);
            verify(favoriteTripRepository, times(2)).findMinPosition();
            verify(sparsifier, times(1)).sparsify();
            verify(config, times(1)).getMinGap();
        }

        @Test
        void calculateNewPosition_TargetPositionMiddle_GapSufficient() {
            Long targetPosition = 3L;
            long totalCount = 5L;
            long prevIndex = 1L;
            long nextIndex = 2L;
            long prevPosition = 10000000L;
            long nextPosition = 20000000L;
            long minGap = 100L;

            when(favoriteTripRepository.count()).thenReturn(totalCount);
            when(favoriteTripRepository.findPositionByIndex(prevIndex)).thenReturn(Optional.of(prevPosition));
            when(favoriteTripRepository.findPositionByIndex(nextIndex)).thenReturn(Optional.of(nextPosition));
            when(config.getMinGap()).thenReturn(minGap);

            long newPosition = positionCalculator.calculateNewPosition(targetPosition);

            assertEquals(prevPosition + (nextPosition - prevPosition) / 2, newPosition);
            verify(favoriteTripRepository, times(1)).count();
            verify(favoriteTripRepository, times(1)).findPositionByIndex(prevIndex);
            verify(favoriteTripRepository, times(1)).findPositionByIndex(nextIndex);
            verify(config, times(1)).getMinGap();
            verify(sparsifier, never()).sparsify();
        }
    }

}
