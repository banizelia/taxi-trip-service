package com.banizelia.taxi.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({FavoriteTripListConfig.class, ExcelExporterConfig.class})
public class ConfigurationPropertiesConfig {
}
