package com.cloudator.service;

import com.cloudator.configuration.WeatherApiProperties;
import com.cloudator.dto.MonitoredLocationItemDTO;
import com.cloudator.dto.MonitoredLocationListDTO;
import com.cloudator.exception.LoadMonitoredLocationsException;
import com.cloudator.repository.LocationRepository;
import com.cloudator.service.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.cloudator.logging.ErrorIds.WEATHER_API_REPO_UPDATE_LOCATION_MSG;

@Log4j2
@Service
@RequiredArgsConstructor
public class LocationService {

    private final WeatherApiProperties weatherApiProperties;
    private final MonitoredLocationReaderService monitoredLocationReaderService;
    private final LocationRepository locationRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional(propagation = Propagation.REQUIRED)
    public MonitoredLocationListDTO loadMonitoredLocations() {
        try {
            MonitoredLocationListDTO monitoredLocationListDTO =
                    monitoredLocationReaderService.readMonitoredLocationsFromJsonFile(weatherApiProperties.getAbsoluteFilePath());
            persistMonitoredLocations(monitoredLocationListDTO);
            return monitoredLocationListDTO;
        } catch(InvalidDataAccessApiUsageException exception) {
            log.error(String.format(WEATHER_API_REPO_UPDATE_LOCATION_MSG.getMessage(), exception.getMessage()));
            log.debug(exception.getStackTrace());
            throw new LoadMonitoredLocationsException();
        }
    }

    public MonitoredLocationListDTO retrieveMonitoredLocations() {
        List<MonitoredLocationItemDTO> monitoredLocationItemDTOList =
                locationRepository
                        .findAllByIsMonitoredTrue()
                        .stream()
                        .map(EntityMapper::convertToDTO)
                        .collect(Collectors.toList());
        return MonitoredLocationListDTO.builder().monitoredLocationItemDTOList(monitoredLocationItemDTOList).build();
    }

    private void persistMonitoredLocations(MonitoredLocationListDTO monitoredLocationListDTO) {
        @Valid List<MonitoredLocationItemDTO> monitoredLocationItemDTOList =
                monitoredLocationListDTO.getMonitoredLocationItemDTOList();
        if (!monitoredLocationItemDTOList.isEmpty()) {
            locationRepository.setMonitoredFalse();
            locationRepository.saveAll(
                    monitoredLocationItemDTOList
                            .stream()
                            .map(EntityMapper::convertToEntity)
                            .collect(Collectors.toList())
            );
        }
    }
}
