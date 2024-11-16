package com.web.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class AsyncConfig implements WebMvcConfigurer {
    @Value("${async.timeout.millis}")
    private long asyncTimeoutMillis;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        if (asyncTimeoutMillis <= 0) {
            log.warn("Invalid async timeout value: {}. Using default", asyncTimeoutMillis);
            asyncTimeoutMillis = 30000;
        }
        configurer.setDefaultTimeout(asyncTimeoutMillis);
        log.info("Async timeout configured: {} ms", asyncTimeoutMillis);
    }
}
