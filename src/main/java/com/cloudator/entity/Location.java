package com.cloudator.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(LocationId.class)
public class Location {
    @Id
    private Double latitude;
    @Id
    private Double longitude;
    private Boolean isMonitored;
    private Double maxTemp;
    private Double minTemp;
}
