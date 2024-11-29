package com.web.favorite.service.common;

import com.web.favorite.config.FavoriteTripListConf;
import com.web.favorite.repository.FavoriteTripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

class PositionCalculatorTest {

    @Mock
    private FavoriteTripListConf config;

    @Mock
    private SparsifierService sparsifier;

    @Mock
    private FavoriteTripRepository favoriteTripRepository;

    @InjectMocks
    private PositionCalculator positionCalculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Tests for calculateNewPosition method")
    class CalculateNewPositionTests {

        @Test
        @DisplayName("Should calculate last position when targetPosition > totalCount and maxPosition == 0")
        void calculateNewPosition_TargetPositionGreaterThanTotalCount_MaxPositionZero() {
            // Arrange
            Long targetPosition = 10L;
            long totalCount = 5L;
            long initialPosition = 10000000L;

            when(favoriteTripRepository.count()).thenReturn(totalCount);
            when(sparsifier.getNextAvailablePosition()).thenReturn(0L);
            when(config.getInitialPosition()).thenReturn(initialPosition);

            // Act
            long newPosition = positionCalculator.calculateNewPosition(targetPosition);

            // Assert
            assertEquals(initialPosition, newPosition);
            verify(favoriteTripRepository, times(1)).count();
            verify(sparsifier, times(1)).getNextAvailablePosition();
            verify(config, times(1)).getInitialPosition();
        }

        @Test
        @DisplayName("Should calculate last position when targetPosition > totalCount and maxPosition > 0")
        void calculateNewPosition_TargetPositionGreaterThanTotalCount_MaxPositionNonZero() {
            // Arrange
            Long targetPosition = 10L;
            long totalCount = 5L;
            long maxPosition = 20000000L;
            long initialPosition = 10000000L;

            when(favoriteTripRepository.count()).thenReturn(totalCount);
            when(sparsifier.getNextAvailablePosition()).thenReturn(maxPosition);
            when(config.getInitialPosition()).thenReturn(initialPosition);

            // Act
            long newPosition = positionCalculator.calculateNewPosition(targetPosition);

            // Assert
            assertEquals(maxPosition + initialPosition, newPosition);
            verify(favoriteTripRepository, times(1)).count();
            verify(sparsifier, times(1)).getNextAvailablePosition();
            verify(config, times(1)).getInitialPosition();
        }

        @Test
        @DisplayName("Should calculate first position when targetPosition == 1 and firstPosition >= minGap")
        void calculateNewPosition_TargetPositionOne_FirstPositionValid() {
            // Arrange
            Long targetPosition = 1L;
            long totalCount = 5L;
            long firstPosition = 20000000L;
            long minGap = 100L;

            when(favoriteTripRepository.count()).thenReturn(totalCount);
            when(favoriteTripRepository.findMinPosition()).thenReturn(Optional.of(firstPosition));
            when(config.getMinGap()).thenReturn(minGap);

            // Act
            long newPosition = positionCalculator.calculateNewPosition(targetPosition);

            // Assert
            assertEquals(firstPosition / 2, newPosition);
            verify(favoriteTripRepository, times(1)).count();
            verify(favoriteTripRepository, times(1)).findMinPosition();
            verify(config, times(1)).getMinGap();
            verify(sparsifier, never()).sparsify();
        }

        @Test
        @DisplayName("Should calculate first position when targetPosition == 1 and firstPosition < minGap")
        void calculateNewPosition_TargetPositionOne_FirstPositionRequiresSparsify() {
            // Arrange
            Long targetPosition = 1L;
            long totalCount = 5L;
            long initialPosition = 10000000L;
            long firstPosition = 50L; // Less than minGap
            long minGap = 100L;

            when(favoriteTripRepository.count()).thenReturn(totalCount);
            when(favoriteTripRepository.findMinPosition()).thenReturn(Optional.of(firstPosition))
                    .thenReturn(Optional.of(firstPosition)); // After sparsify
            when(config.getMinGap()).thenReturn(minGap);
            when(config.getInitialPosition()).thenReturn(initialPosition);

            // Act
            long newPosition = positionCalculator.calculateNewPosition(targetPosition);

            // Assert
            assertEquals(firstPosition / 2, newPosition);
            verify(favoriteTripRepository, times(2)).findMinPosition();
            verify(sparsifier, times(1)).sparsify();
            verify(config, times(1)).getMinGap();
        }

        @Test
        @DisplayName("Should calculate middle position when targetPosition is between 2 and totalCount and gap is sufficient")
        void calculateNewPosition_TargetPositionMiddle_GapSufficient() {
            // Arrange
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

            // Act
            long newPosition = positionCalculator.calculateNewPosition(targetPosition);

            // Assert
            assertEquals(prevPosition + (nextPosition - prevPosition) / 2, newPosition);
            verify(favoriteTripRepository, times(1)).count();
            verify(favoriteTripRepository, times(1)).findPositionByIndex(prevIndex);
            verify(favoriteTripRepository, times(1)).findPositionByIndex(nextIndex);
            verify(config, times(1)).getMinGap();
            verify(sparsifier, never()).sparsify();
        }
    }

    @Nested
    @DisplayName("Tests for calculateLastPosition method")
    class CalculateLastPositionTests {

        @Test
        @DisplayName("Should calculate last position when maxPosition == 0")
        void calculateLastPosition_MaxPositionZero() {
            // Arrange
            long initialPosition = 10000000L;

            when(sparsifier.getNextAvailablePosition()).thenReturn(0L);
            when(config.getInitialPosition()).thenReturn(initialPosition);

            // Act
            long lastPosition = positionCalculator.calculateLastPosition();

            // Assert
            assertEquals(initialPosition, lastPosition);
            verify(sparsifier, times(1)).getNextAvailablePosition();
            verify(config, times(1)).getInitialPosition();
        }

        @Test
        @DisplayName("Should calculate last position when maxPosition > 0")
        void calculateLastPosition_MaxPositionNonZero() {
            // Arrange
            long initialPosition = 10000000L;
            long maxPosition = 20000000L;

            when(sparsifier.getNextAvailablePosition()).thenReturn(maxPosition);
            when(config.getInitialPosition()).thenReturn(initialPosition);

            // Act
            long lastPosition = positionCalculator.calculateLastPosition();

            // Assert
            assertEquals(maxPosition + initialPosition, lastPosition);
            verify(sparsifier, times(1)).getNextAvailablePosition();
            verify(config, times(1)).getInitialPosition();
        }
    }
}
