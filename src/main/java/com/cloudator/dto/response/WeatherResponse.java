package com.cloudator.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeatherResponse {
    @JsonProperty("list")
    private List<WeatherForecastItem> weatherForecastResponseList;

    private Double latitude;

    private Double longitude;

    @JsonProperty("city")
    private void unpackCityField(Map<String,Object> city) {
        Map<String, Object> coord = (Map<String, Object>) city.get("coord");
        Object latitudeValue = coord.get("lat");
        Object longitudeValue = coord.get("lon");
        this.latitude  = (latitudeValue instanceof Integer) ? new Double((Integer) latitudeValue) : (Double) latitudeValue;
        this.longitude = (longitudeValue instanceof Integer) ? new Double((Integer) longitudeValue) : (Double) longitudeValue;
    }
}
