package com.cloudator.service;

import com.cloudator.configuration.WeatherApiProperties;
import com.cloudator.dto.ForecastAlertDTO;
import com.cloudator.dto.LocationToMonitorItemDTO;
import com.cloudator.dto.response.WeatherForecastItem;
import com.cloudator.dto.response.WeatherResponse;
import com.cloudator.entity.ForecastAlert;
import com.cloudator.repository.ForecastAlertRepository;
import com.cloudator.service.util.EntityMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ForecastAlertService {

    private final WeatherApiServiceProxy weatherApiServiceProxy;
    private final WeatherApiProperties weatherApiProperties;
    private final ForecastAlertRepository forecastAlertRepository;
    private final LocationService locationService;

    @Scheduled(fixedDelayString = "${weather.fixedDelayInMilli}")
    public List<ForecastAlert> verifyWeather() {
        //TODO: insert logs
        //TODO: refactor the code
        List<ForecastAlert> forecastAlertList = new ArrayList<>();
        locationService
                .retrieveMonitoredLocations()
                .getLocationToMonitorItemDTOList()
                .forEach(monitoredLocationDTO -> {
                    WeatherResponse weatherResponse = consumeRestProvider(monitoredLocationDTO);
                    if (Objects.nonNull(weatherResponse) && !weatherResponse.getWeatherForecastResponseList().isEmpty()) {
                        List<WeatherForecastItem> newForecastAlertList =
                                weatherResponse
                                        .getWeatherForecastResponseList()
                                        .stream()
                                        .filter(weatherForecastItem ->
                                                isTemperatureExceeded(
                                                        weatherForecastItem,
                                                        monitoredLocationDTO.getMinTemp(),
                                                        monitoredLocationDTO.getMaxTemp()))
                                        .collect(Collectors.toList());
                        forecastAlertRepository
                                .saveAll(EntityMapper.convertToForecastAlertList(
                                        newForecastAlertList,
                                        monitoredLocationDTO.getLatitude(),
                                        monitoredLocationDTO.getLongitude()))
                                .forEach(forecastAlertList::add);
                    }
                });
        return forecastAlertList;
    }

    private boolean isTemperatureExceeded(WeatherForecastItem weatherForecastItem, Double minTemp, Double maxTemp) {
        return  weatherForecastItem.getMainWeatherForecast().getMaxTemp() >= maxTemp ||
                weatherForecastItem.getMainWeatherForecast().getMinTemp() <= minTemp;
    }

    @HystrixCommand(fallbackMethod = "fallbackConsumeRestProvider")
    private WeatherResponse consumeRestProvider(LocationToMonitorItemDTO monitoredLocationDTO) {
        return
                weatherApiServiceProxy.forecastWeather(
                        monitoredLocationDTO.getLatitude(),
                        monitoredLocationDTO.getLongitude(),
                        weatherApiProperties.getId());
    }

    private WeatherResponse fallbackConsumeRestProvider(Throwable e) {
        log.error(e.getMessage());
        log.error(e.getStackTrace());
        return null;
    }

    public List<ForecastAlertDTO> retrieveForecastAlertFrom(LocalDateTime fromLocalDateTime) {
        //TODO: look as example convert
        long timestamp =  fromLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        return
        forecastAlertRepository
                .findAllByTimestampGreaterThanEqualOrderByTimestamp(timestamp)
                .stream()
                .map(EntityMapper::convertToDTO)
                .collect(Collectors.toList());
    }
}
