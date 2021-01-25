package com.cloudator.service.util;

import com.cloudator.dto.LocationToMonitorItemDTO;

import com.cloudator.dto.LocationToMonitorListDTO;
import com.cloudator.entity.Location;
import lombok.experimental.UtilityClass;


import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class EntityMapper {
    public static List<Location> convertToListOfLocation(List<LocationToMonitorItemDTO> locationToMonitorItemDTOList) {
        List<Location> locationList = new ArrayList<>();
        locationToMonitorItemDTOList.forEach(locationDTO -> locationList.add(
                Location
                        .builder()
                        .latitude(locationDTO.getLatitude())
                        .longitude(locationDTO.getLongitude())
                        .maxTemp(locationDTO.getMaxTemp())
                        .minTemp(locationDTO.getMinTemp())
                        .isMonitored(true)
                        .build()
        ));
        return locationList;
    }

    public static LocationToMonitorListDTO convertToLocationToMonitorListDTO(List<Location> locationList) {
        List<LocationToMonitorItemDTO> locationToMonitorItemDTOList = new ArrayList<>();
        locationList.forEach(location -> locationToMonitorItemDTOList.add(LocationToMonitorItemDTO.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .maxTemp(location.getMaxTemp())
                .minTemp(location.getMinTemp())
                .build()
        ));

        return LocationToMonitorListDTO.builder().locationToMonitorItemDTOList(locationToMonitorItemDTOList).build();
    }
}
