package com.web.favorite.service.common;

import com.web.common.exception.position.PositionOverflowException;
import com.web.favorite.config.FavoriteTripListConf;
import com.web.favorite.repository.FavoriteTripRepository;
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
class SparsifierServiceTest {

    @Mock
    private FavoriteTripRepository repository;

    @Mock
    private FavoriteTripListConf config;

    @InjectMocks
    private SparsifierService sparsifier;

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