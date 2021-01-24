package com.cloudator.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("weather")
@Data
public class WeatherApiProperties {
    private String url = " ";
    private String id = " ";
}
