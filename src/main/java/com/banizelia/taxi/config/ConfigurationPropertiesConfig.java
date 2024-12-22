package com.banizelia.taxi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableConfigurationProperties({FavoriteTripListConfig.class, ExcelExporterConfig.class})
public class ConfigurationPropertiesConfig {
}
