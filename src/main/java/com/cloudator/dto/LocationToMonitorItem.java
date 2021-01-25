package com.cloudator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationToMonitorItem {
    @Min(value = -90, message = "Latitude invalid value [-90,90]")
    @Max(value = 90, message = "Latitude invalid value [-90,90]")
    private Double latitude;

    @Min(value = -180, message = "Longitude invalid value [-180,180]")
    @Max(value = 180, message = "Latitude invalid value [-180,180]")
    private Double longitude;

    @Min(value = 0, message = "Kelvin invalid value (> 0)")
    private Double minTemp;

    @Min(value = 0, message = "Kelvin invalid value (> 0)")
    private Double maxTemp;
}
