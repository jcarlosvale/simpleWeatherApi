package com.cloudator.service;

import com.cloudator.configuration.WeatherApiProperties;
import com.cloudator.dto.ForecastAlertDTO;
import com.cloudator.dto.MonitoredLocationItemDTO;
import com.cloudator.dto.response.WeatherForecastItem;
import com.cloudator.dto.response.WeatherResponse;
import com.cloudator.entity.ForecastAlert;
import com.cloudator.repository.ForecastAlertRepository;
import com.cloudator.service.util.EntityMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

        log.info("Verifying the Weather Provider API at [{}]",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        List<ForecastAlert> forecastAlertList = new ArrayList<>();

        @Valid List<MonitoredLocationItemDTO> monitoredLocationItemDTOList =
                locationService.retrieveMonitoredLocations().getMonitoredLocationItemDTOList();

        monitoredLocationItemDTOList
                .forEach(monitoredLocationItemDTO -> forecastAlertList.addAll(findAlerts(monitoredLocationItemDTO)));

        forecastAlertRepository.saveAll(forecastAlertList);

        return forecastAlertList;
    }

    private List<ForecastAlert> findAlerts(@NonNull MonitoredLocationItemDTO monitoredLocationItemDTO) {
        List<ForecastAlert> forecastAlertList = new ArrayList<>();
        WeatherResponse weatherResponse = consumeRestProvider(monitoredLocationItemDTO);
        if (Objects.nonNull(weatherResponse) && !weatherResponse.getWeatherForecastResponseList().isEmpty()) {
            forecastAlertList.addAll(
                weatherResponse
                        .getWeatherForecastResponseList()
                        .stream()
                        .filter(weatherForecastItem ->
                                isTemperatureExceeded(
                                        weatherForecastItem,
                                        monitoredLocationItemDTO.getMinTemp(),
                                        monitoredLocationItemDTO.getMaxTemp()))
                        .map(weatherForecastItem ->
                                EntityMapper
                                        .convertToEntity(
                                                weatherForecastItem,
                                                weatherResponse.getLatitude(),
                                                weatherResponse.getLongitude()))
                        .collect(Collectors.toList())
            );
        }
        return forecastAlertList;
    }

    private boolean isTemperatureExceeded(WeatherForecastItem weatherForecastItem, Double minTemp, Double maxTemp) {
        return  weatherForecastItem.getMaxTemp() >= maxTemp || weatherForecastItem.getMinTemp() <= minTemp;
    }

    @HystrixCommand(fallbackMethod = "fallbackConsumeRestProvider")
    private WeatherResponse consumeRestProvider(MonitoredLocationItemDTO monitoredLocationDTO) {
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
        long timestamp =  fromLocalDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000;
        return
        forecastAlertRepository
                .findAllByTimestampGreaterThanEqualOrderByTimestamp(timestamp)
                .stream()
                .map(EntityMapper::convertToDTO)
                .collect(Collectors.toList());
    }
}
