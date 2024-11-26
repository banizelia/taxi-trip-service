package com.web.common.export;

import jakarta.validation.constraints.*;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Value
@Validated
@ConfigurationProperties(prefix = "excel-export")
public class ExcelExporterConf {
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Sheet prefix can only contain letters, numbers, underscore, and hyphen")
    @Size(min = 1, max = 30, message = "Sheet prefix length must be between 1 and 30 characters")
    String sheetPrefix;

    @Min(1)
    @Max(1048576) // limitation by xlsx format
    int maxRowsPerSheet;

    @Positive
    int batchSize;
}