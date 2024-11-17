package com.web.common.config;

import com.web.common.exception.initialization.ApplicationInitializationException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Slf4j
@Configuration
public class TimeZoneConfig {
    private static final String APPLICATION_TIMEZONE = "GMT";

    @PostConstruct
    void init() {
        try {
            TimeZone targetTimeZone = TimeZone.getTimeZone(APPLICATION_TIMEZONE);
            TimeZone.setDefault(targetTimeZone);
            log.info("Application timezone set to: {}", APPLICATION_TIMEZONE);
        } catch (Exception e) {
            log.error("Failed to set timezone to {}", APPLICATION_TIMEZONE, e);
            throw new ApplicationInitializationException("Failed to set timezone", e);
        }
    }
}