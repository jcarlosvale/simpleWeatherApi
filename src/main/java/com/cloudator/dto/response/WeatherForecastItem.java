package com.cloudator.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherForecastItem {

    @JsonProperty("dt_txt")
    private String dateInText;

    @JsonProperty("main")
    private MainWeatherForecast mainWeatherForecast;
}
