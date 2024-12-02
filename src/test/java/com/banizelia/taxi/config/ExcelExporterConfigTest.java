package com.banizelia.taxi.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcelExporterConfigTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenValidInput_thenNoViolations() {
        ExcelExporterConfig config = new ExcelExporterConfig("validPrefix", 100, 10);
        Set<ConstraintViolation<ExcelExporterConfig>> violations = validator.validate(config);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenInvalidSheetPrefix_thenViolation() {
        ExcelExporterConfig config = new ExcelExporterConfig("invalid*prefix", 100, 10);
        Set<ConstraintViolation<ExcelExporterConfig>> violations = validator.validate(config);

        assertEquals(1, violations.size());
    }

    @Test
    void whenBatchSizeNegative_thenViolation() {
        ExcelExporterConfig config = new ExcelExporterConfig("validPrefix", 100, -1);
        Set<ConstraintViolation<ExcelExporterConfig>> violations = validator.validate(config);

        assertEquals(1, violations.size());
    }
}