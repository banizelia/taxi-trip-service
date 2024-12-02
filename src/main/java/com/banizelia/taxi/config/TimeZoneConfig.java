package com.banizelia.taxi.config;

import com.banizelia.taxi.error.initialization.ApplicationInitializationException;
import com.banizelia.taxi.error.initialization.InvalidTimeZoneException;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.time.ZoneId;
import java.util.Set;
import java.util.TimeZone;

@Slf4j
@Configuration
public class TimeZoneConfig {

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    @Value("${application-timezone:GMT}")
    private String applicationTimezone;

    @PostConstruct
    void init() {
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();

        if (!availableZoneIds.contains(applicationTimezone)) {
            log.error("Invalid timezone specified: {}", applicationTimezone);
            throw new InvalidTimeZoneException(applicationTimezone);
        }

        try {
            ZoneId zoneId = ZoneId.of(applicationTimezone);
            TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
            log.info("Application timezone set to: {}", applicationTimezone);
        } catch (Exception e) {
            log.error("Failed to set timezone to {}", applicationTimezone, e);
            throw new ApplicationInitializationException("Failed to set timezone", e);
        }
    }
}