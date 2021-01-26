package com.cloudator.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ForecastAlertId.class)
public class ForecastAlert {

    @Id
    @Min(value = -90, message = "Latitude invalid value [-90,90]")
    @Max(value = 90, message = "Latitude invalid value [-90,90]")
    private Double latitude;

    @Id
    @Min(value = -180, message = "Longitude invalid value [-180,180]")
    @Max(value = 180, message = "Latitude invalid value [-180,180]")
    private Double longitude;

    @Id
    private Long timestamp;

    private String dateTimeAsText;
    private Double maxTemp;
    private Double minTemp;
}
