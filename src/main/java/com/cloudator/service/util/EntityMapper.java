package com.cloudator.service.util;

import com.cloudator.dto.ForecastAlertDTO;
import com.cloudator.dto.MonitoredLocationItemDTO;
import com.cloudator.dto.response.WeatherForecastItem;
import com.cloudator.entity.ForecastAlert;
import com.cloudator.entity.Location;
import lombok.experimental.UtilityClass;

import javax.validation.Valid;

@UtilityClass
public class EntityMapper {

    public static ForecastAlertDTO convertToDTO(@Valid ForecastAlert forecastAlert) {
        return ForecastAlertDTO
                .builder()
                .latitude(forecastAlert.getLatitude())
                .longitude(forecastAlert.getLongitude())
                .maxTemp(forecastAlert.getMaxTemp())
                .minTemp(forecastAlert.getMinTemp())
                .timestamp(forecastAlert.getTimestamp())
                .dateTimeAsText(forecastAlert.getDateTimeAsText())
                .build();
    }

    public static MonitoredLocationItemDTO convertToDTO(@Valid Location location) {
        return MonitoredLocationItemDTO
                .builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .maxTemp(location.getMaxTemp())
                .minTemp(location.getMinTemp())
                .build();
    }

    public static Location convertToEntity(@Valid MonitoredLocationItemDTO monitoredLocationItemDTO) {
        return Location
                .builder()
                .latitude(monitoredLocationItemDTO.getLatitude())
                .longitude(monitoredLocationItemDTO.getLongitude())
                .isMonitored(true)
                .maxTemp(monitoredLocationItemDTO.getMaxTemp())
                .minTemp(monitoredLocationItemDTO.getMinTemp())
                .build();
    }

    public static ForecastAlert convertToEntity(WeatherForecastItem weatherForecastItem, Double latitude, Double longitude) {
        return ForecastAlert
                .builder()
                .longitude(longitude)
                .latitude(latitude)
                .timestamp(weatherForecastItem.getTimestamp())
                .dateTimeAsText(weatherForecastItem.getDateInText())
                .maxTemp(weatherForecastItem.getMaxTemp())
                .minTemp(weatherForecastItem.getMinTemp())
                .build();
    }
}
