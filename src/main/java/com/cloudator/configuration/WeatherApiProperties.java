package com.cloudator.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Property file representing the Application.yaml
 */
@ConfigurationProperties("weather")
@Data
public class WeatherApiProperties {
    private String url = " ";
    private String id = " ";
    private String absoluteFilePath= " ";
    private String fixedDelayInMilli = "60000";
}
