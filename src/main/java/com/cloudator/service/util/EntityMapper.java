package com.cloudator.service.util;

import com.cloudator.dto.ForecastAlertDTO;
import com.cloudator.dto.LocationToMonitorItemDTO;

import com.cloudator.dto.LocationToMonitorListDTO;
import com.cloudator.dto.response.WeatherForecastItem;
import com.cloudator.entity.ForecastAlert;
import com.cloudator.entity.Location;
import lombok.experimental.UtilityClass;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<ForecastAlert> convertToForecastAlertList(List<WeatherForecastItem> newForecastAlertList,
                                                                 Double latitude,
                                                                 Double longitude) {
        return
        newForecastAlertList
                .stream()
                .map(weatherForecastItem -> ForecastAlert
                        .builder()
                        .timestamp(weatherForecastItem.getTimestamp())
                        .dateTimeAsText(weatherForecastItem.getDateInText())
                        .maxTemp(weatherForecastItem.getMainWeatherForecast().getMaxTemp())
                        .minTemp(weatherForecastItem.getMainWeatherForecast().getMinTemp())
                        .location(
                                Location
                                        .builder()
                                        .latitude(latitude)
                                        .longitude(longitude)
                                        .isMonitored(true)
                                        .build())
                        .build())
                .collect(Collectors.toList());
    }

    public static ForecastAlertDTO convertToDTO(ForecastAlert forecastAlert) {
        return ForecastAlertDTO
                .builder()
                .latitude(forecastAlert.getLocation().getLatitude())
                .longitude(forecastAlert.getLocation().getLongitude())
                .maxTemp(forecastAlert.getMaxTemp())
                .minTemp(forecastAlert.getMinTemp())
                .timestamp(forecastAlert.getTimestamp())
                .dateTimeAsText(forecastAlert.getDateTimeAsText())
                .build();
    }
}
