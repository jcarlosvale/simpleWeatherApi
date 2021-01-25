package com.cloudator.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForecastAlert {
    @Id
    @GeneratedValue
    private Long id;
    private Long timestamp;
    private String dateTimeAsText;
    private Double maxTemp;
    private Double minTemp;

    @ManyToOne(fetch = FetchType.LAZY, cascade={CascadeType.MERGE, CascadeType.PERSIST})
    private Location location;
}
