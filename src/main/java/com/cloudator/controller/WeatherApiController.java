package com.cloudator.controller;

import com.cloudator.configuration.WeatherApiProperties;
import com.cloudator.dto.ForecastAlertDTO;
import com.cloudator.dto.LocationToMonitorListDTO;
import com.cloudator.dto.response.WeatherResponse;
import com.cloudator.entity.ForecastAlert;
import com.cloudator.service.ForecastAlertService;
import com.cloudator.service.LocationService;
import com.cloudator.service.WeatherApiServiceProxy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class WeatherApiController {

    private final LocationService locationService;
    private final ForecastAlertService forecastAlertService;

    @GetMapping(path = "forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ForecastAlertDTO>> getAllForecastAlert() {
        List<ForecastAlertDTO> forecastAlertDTOList = forecastAlertService.retrieveForecastAlertFrom(LocalDateTime.now());
        return ResponseEntity.ok(forecastAlertDTOList);
    }

    @GetMapping(path = "getMonitoredLocations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationToMonitorListDTO> getMonitoredLocations() {
        LocationToMonitorListDTO locationToMonitorListDTO = locationService.retrieveMonitoredLocations();
        return ResponseEntity.ok(locationToMonitorListDTO);
    }

    @PutMapping(path = "loadLocations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LocationToMonitorListDTO> loadLocationsToMonitor() {
        LocationToMonitorListDTO locationToMonitorListDTO = locationService.loadLocationsToMonitor();
        return ResponseEntity.ok(locationToMonitorListDTO);
    }
}
