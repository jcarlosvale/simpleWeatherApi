package com.cloudator.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForecastAlertId implements Serializable {
    @Id
    private Double latitude;
    @Id
    private Double longitude;
    @Id
    private Long timestamp;
}
