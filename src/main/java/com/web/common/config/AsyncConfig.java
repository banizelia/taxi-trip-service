package com.web.common.config;

import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class AsyncConfig implements WebMvcConfigurer {
    @Positive
    @Value("${async-timeout-millis:600000}")
    private long asyncTimeoutMillis;

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer conf) {
        conf.setDefaultTimeout(asyncTimeoutMillis);
        log.info("Async timeout configured: {} ms", asyncTimeoutMillis);
    }
}