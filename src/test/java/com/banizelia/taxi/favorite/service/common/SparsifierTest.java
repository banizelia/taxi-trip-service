package com.banizelia.taxi.favorite.service.common;

import com.banizelia.taxi.error.position.PositionOverflowException;
import com.banizelia.taxi.config.FavoriteTripListConfig;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import com.banizelia.taxi.favorite.service.position.Sparsifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SparsifierTest {

    @Mock
    private FavoriteTripRepository repository;

    @Mock
    private FavoriteTripListConfig config;

    @InjectMocks
    private Sparsifier sparsifier;

    @Test
    void getNextAvailablePosition_PositionOverflow() {
        when(repository.findMaxPosition())
                .thenReturn(Optional.of(Long.MAX_VALUE - 100L))
                .thenReturn(Optional.of(Long.MAX_VALUE - 100L));
        when(repository.findAllByOrderByPositionAsc()).thenReturn(Collections.emptyList());

        assertThrows(PositionOverflowException.class,
                () -> sparsifier.getNextAvailablePosition());
    }

    @Test
    void sparsify_EmptyList() {
        when(repository.findAllByOrderByPositionAsc()).thenReturn(Collections.emptyList());

        sparsifier.sparsify();

        verify(repository, never()).saveAll(anyList());
    }

}