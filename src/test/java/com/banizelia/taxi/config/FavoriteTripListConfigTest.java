package com.banizelia.taxi.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteTripListConfigTest {

    private Validator validator;
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testValidConfiguration() {
        FavoriteTripListConfig config = new FavoriteTripListConfig(1000L, 1000L, 100L, 0.8);

        Set<ConstraintViolation<FavoriteTripListConfig>> violations = validator.validate(config);

        assertTrue(violations.isEmpty());
        assertEquals(1000L, config.getPositionGap());
        assertEquals(1000L, config.getInitialPosition());
        assertEquals(100L, config.getMinGap());
        assertEquals(0.8, config.getRebalanceThreshold());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -100L})
    void testInvalidPositionGap(long invalidGap) {
        FavoriteTripListConfig config = new FavoriteTripListConfig(invalidGap, 1000L, 100L, 0.8);

        Set<ConstraintViolation<FavoriteTripListConfig>> violations = validator.validate(config);

        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -100L})
    void testInvalidInitialPosition(long invalidPosition) {
        FavoriteTripListConfig config = new FavoriteTripListConfig(1000L, invalidPosition, 100L, 0.8);

        Set<ConstraintViolation<FavoriteTripListConfig>> violations = validator.validate(config);

        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -100L})
    void testInvalidMinGap(long invalidGap) {
        FavoriteTripListConfig config = new FavoriteTripListConfig(1000L, 1000L, invalidGap, 0.8);

        Set<ConstraintViolation<FavoriteTripListConfig>> violations = validator.validate(config);

        assertFalse(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 1.0, -0.1, 1.1})
    void testInvalidRebalanceThreshold(double invalidThreshold) {
        FavoriteTripListConfig config = new FavoriteTripListConfig(1000L, 1000L, 100L, invalidThreshold);

        Set<ConstraintViolation<FavoriteTripListConfig>> violations = validator.validate(config);

        assertFalse(violations.isEmpty());
    }

    @Test
    void testConfigurationBinding() {
        contextRunner
                .withPropertyValues(
                        "favorite-trip-list.position-gap=2000",
                        "favorite-trip-list.initial-position=1500",
                        "favorite-trip-list.min-gap=200",
                        "favorite-trip-list.rebalance-threshold=0.75"
                )
                .withUserConfiguration(TestConfig.class)
                .run(context -> {
                    FavoriteTripListConfig config = context.getBean(FavoriteTripListConfig.class);
                    assertEquals(2000L, config.getPositionGap());
                    assertEquals(1500L, config.getInitialPosition());
                    assertEquals(200L, config.getMinGap());
                    assertEquals(0.75, config.getRebalanceThreshold());
                });
    }

    @EnableConfigurationProperties(FavoriteTripListConfig.class)
    static class TestConfig {
    }

    @Test
    void testValueAnnotationBehavior() {
        // Arrange
        FavoriteTripListConfig config1 = new FavoriteTripListConfig(1000L, 1000L, 100L, 0.8);
        FavoriteTripListConfig config2 = new FavoriteTripListConfig(1000L, 1000L, 100L, 0.8);
        FavoriteTripListConfig config3 = new FavoriteTripListConfig(2000L, 1000L, 100L, 0.8);

        // Assert
        assertEquals(config1, config2);
        assertNotEquals(config1, config3);
        assertEquals(config1.hashCode(), config2.hashCode());
        assertNotEquals(config1.hashCode(), config3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        FavoriteTripListConfig config = new FavoriteTripListConfig(1000L, 1000L, 100L, 0.8);

        // Act
        String toString = config.toString();

        // Assert
        assertTrue(toString.contains("positionGap=1000"));
        assertTrue(toString.contains("initialPosition=1000"));
        assertTrue(toString.contains("minGap=100"));
        assertTrue(toString.contains("rebalanceThreshold=0.8"));
    }
}