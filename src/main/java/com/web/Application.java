package com.web;

import com.web.favorite.config.FavoriteTripListConf;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FavoriteTripListConf.class)
@OpenAPIDefinition(info = @Info(title = "API Documentation", version = "1.0", description = "Taxi Trip API"))
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}