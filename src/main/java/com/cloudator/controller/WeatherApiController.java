package com.cloudator.controller;

import com.cloudator.dto.ForecastAlertDTO;
import com.cloudator.dto.MonitoredLocationListDTO;
import com.cloudator.service.ForecastAlertService;
import com.cloudator.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Main Controller with endpoints:
 * - GET
 *    a) forecast : retrieve all next alerts
 *    b) getMonitoredLocations: list of monitored locations
 * - PUT
 *    a) loadLocations: reload the locations to be monitored using the JSON file configured in the application.yaml
 */
@Controller
@RequiredArgsConstructor
@Log4j2
public class WeatherApiController {

    private final LocationService locationService;
    private final ForecastAlertService forecastAlertService;

    @GetMapping(path = "/forecast", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ForecastAlertDTO>> getAllForecastAlert() {
        List<ForecastAlertDTO> forecastAlertDTOList = forecastAlertService.retrieveForecastAlertFrom(LocalDateTime.now());
        return ResponseEntity.ok(forecastAlertDTOList);
    }

    @GetMapping(path = "/getMonitoredLocations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitoredLocationListDTO> getMonitoredLocations() {
        MonitoredLocationListDTO monitoredLocationListDTO = locationService.retrieveMonitoredLocations();
        return ResponseEntity.ok(monitoredLocationListDTO);
    }

    @PutMapping(path = "/loadLocations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitoredLocationListDTO> loadLocationsToMonitor() {
        MonitoredLocationListDTO monitoredLocationListDTO = locationService.loadMonitoredLocations();
        return ResponseEntity.ok(monitoredLocationListDTO);
    }
}
