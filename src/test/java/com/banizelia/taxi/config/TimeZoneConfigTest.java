package com.banizelia.taxi.config;

import com.banizelia.taxi.error.initialization.InvalidTimeZoneException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class TimeZoneConfigTest {

    private TimeZoneConfig timeZoneConfig;

    @BeforeEach
    void setUp() {
        timeZoneConfig = new TimeZoneConfig();
    }

    @Test
    void testValidTimeZoneInitialization() {
        ReflectionTestUtils.setField(timeZoneConfig, "applicationTimezone", "GMT");

        assertDoesNotThrow(() -> {
            timeZoneConfig.init();
            assertEquals("GMT", TimeZone.getDefault().getID());
        });
    }

    @Test
    void testInvalidTimeZoneInitialization() {
        ReflectionTestUtils.setField(timeZoneConfig, "applicationTimezone", "InvalidZone");


        assertThrows(InvalidTimeZoneException.class, timeZoneConfig::init);

    }

    @Test
    void testApplicationInitializationException() {
        ReflectionTestUtils.setField(timeZoneConfig, "applicationTimezone", "");

        assertThrows(InvalidTimeZoneException.class, timeZoneConfig::init);
    }
}
