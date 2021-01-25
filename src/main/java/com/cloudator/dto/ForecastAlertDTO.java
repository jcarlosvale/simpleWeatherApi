package com.cloudator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForecastAlertDTO {
    private Double latitude;
    private Double longitude;
    private Double maxTemp;
    private Double minTemp;
    private Long timestamp;
    private String dateTimeAsText;
}
