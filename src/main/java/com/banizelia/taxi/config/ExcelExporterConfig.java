package com.banizelia.taxi.config;

import jakarta.validation.constraints.*;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Value
@Validated
@ConfigurationProperties(prefix = "excel-export")
public class ExcelExporterConfig {
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    @Size(min = 1, max = 25)
    String sheetPrefix;

    @Min(1)
    @Max(1048576) // limitation by xlsx format
    int maxRowsPerSheet;

    @Positive
    int batchSize;

    @AssertTrue()
    private boolean isBatchSizeValid() {
        return batchSize <= maxRowsPerSheet;
    }
}