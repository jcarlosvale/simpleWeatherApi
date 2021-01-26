package com.cloudator.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherForecastItem {

    @JsonProperty("dt")
    private Long timestamp;

    @JsonProperty("dt_txt")
    private String dateInText;

    private Double minTemp;

    private Double maxTemp;

    @JsonProperty("main")
    private void unpackMainField(Map<String,Object> main) {
        this.minTemp = (Double)main.get("temp_min");
        this.maxTemp = (Double)main.get("temp_max");
    }
}
