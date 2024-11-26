package com.web.common.config;

import com.web.common.exception.initialization.ApplicationInitializationException;
import com.web.common.exception.initialization.InvalidTimeZoneException;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.util.TimeZone;

@Slf4j
@Configuration
public class TimeZoneConfig {

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    @Value("${application-timezone:GMT}")
    private String applicationTimezone;

    @PostConstruct
    void init() {

        TimeZone targetTimeZone = TimeZone.getTimeZone(applicationTimezone);

        if (!targetTimeZone.getID().equals(applicationTimezone)) {
            throw new InvalidTimeZoneException(applicationTimezone);
        }

        try {
            TimeZone.setDefault(targetTimeZone);
            log.info("Application timezone set to: {}", applicationTimezone);
        } catch (Exception e) {
            log.error("Failed to set timezone to {}", applicationTimezone, e);
            throw new ApplicationInitializationException("Failed to set timezone", e);
        }
    }
}