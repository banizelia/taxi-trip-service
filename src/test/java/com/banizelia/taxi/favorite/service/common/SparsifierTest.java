package com.banizelia.taxi.favorite.service.common;

import com.banizelia.taxi.config.FavoriteTripListConfig;
import com.banizelia.taxi.error.position.PositionOverflowException;
import com.banizelia.taxi.favorite.repository.FavoriteTripRepository;
import com.banizelia.taxi.favorite.service.position.Sparsifier;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SparsifierTest {

    @Mock
    private FavoriteTripRepository repository;

    @Mock
    private FavoriteTripListConfig config;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private Sparsifier sparsifier;

    @BeforeEach
    void setup() throws Exception {
        Field field = Sparsifier.class.getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(sparsifier, entityManager);
    }

    @Test
    void getNextAvailablePosition_PositionOverflow() {
        when(repository.findMaxPosition())
                .thenReturn(Optional.of(Long.MAX_VALUE - 100))
                .thenReturn(Optional.of(Long.MAX_VALUE - 100));
        when(repository.findAllByOrderByPositionAscStream()).thenReturn(Stream.empty());

        assertThrows(PositionOverflowException.class, () -> sparsifier.getNextAvailablePosition());
    }

    @Test
    void sparsify_EmptyList() {
        when(repository.findAllByOrderByPositionAscStream()).thenReturn(Stream.empty());
        sparsifier.sparsify();
        verify(entityManager, atLeastOnce()).flush();
        verify(entityManager, atLeastOnce()).clear();
    }
}
