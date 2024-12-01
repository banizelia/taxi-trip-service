package com.web.common.export;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExcelExporterConfTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenValidInput_thenNoViolations() {
        ExcelExporterConf config = new ExcelExporterConf("validPrefix", 100, 10);
        Set<ConstraintViolation<ExcelExporterConf>> violations = validator.validate(config);

        assertTrue(violations.isEmpty());
    }

    @Test
    void whenInvalidSheetPrefix_thenViolation() {
        ExcelExporterConf config = new ExcelExporterConf("invalid*prefix", 100, 10);
        Set<ConstraintViolation<ExcelExporterConf>> violations = validator.validate(config);

        assertEquals(1, violations.size());
    }

    @Test
    void whenBatchSizeNegative_thenViolation() {
        ExcelExporterConf config = new ExcelExporterConf("validPrefix", 100, -1);
        Set<ConstraintViolation<ExcelExporterConf>> violations = validator.validate(config);

        assertEquals(1, violations.size());
    }
}