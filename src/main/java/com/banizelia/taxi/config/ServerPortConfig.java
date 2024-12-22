package com.banizelia.taxi.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerPortConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Min(1)
    @Max(65535)
    @Value("${server-port:8080}")
    private int serverPort;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.setPort(serverPort);
    }
}
