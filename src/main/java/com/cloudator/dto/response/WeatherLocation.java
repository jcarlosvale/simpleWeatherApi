package com.cloudator.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherLocation {
    private final Double latitude;
    private final Double longitude;
}
