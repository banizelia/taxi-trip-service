package com.banizelia.taxi.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class AsyncConfigTest {

    @Test
    void testConfigureAsyncSupport() {
        AsyncConfig asyncConfig = new AsyncConfig();
        ReflectionTestUtils.setField(asyncConfig, "asyncTimeoutMillis", 10000L);

        AsyncSupportConfigurer mockConfigurer = mock(AsyncSupportConfigurer.class);
        asyncConfig.configureAsyncSupport(mockConfigurer);

        verify(mockConfigurer).setDefaultTimeout(10000L);
    }
}
