package com.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
@OpenAPIDefinition(info = @Info(title = "API Documentation", version = "1.0", description = "Taxi Trip API"))
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}