package com.cloudator.service;

import com.cloudator.dto.LocationToMonitorListDTO;
import com.cloudator.exception.LoadToLocationsToMonitorException;
import com.cloudator.repository.LocationRepository;
import com.cloudator.service.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.cloudator.logging.ErrorIds.WEATHER_API_REPO_UPDATE_LOCATION_MSG;

@Log4j2
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationToMonitorReaderService locationToMonitorReaderService;
    private final LocationRepository locationRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public LocationToMonitorListDTO loadLocationsToMonitor() {
        try {
            LocationToMonitorListDTO locationsToMonitorList = locationToMonitorReaderService.readLocations();
            if (!locationsToMonitorList.getLocationToMonitorItemDTOList().isEmpty()) {
                locationRepository.setMonitoredFalse();
                locationRepository.saveAll(EntityMapper.convertToListOfLocation(locationsToMonitorList.getLocationToMonitorItemDTOList()));
            }
            return locationsToMonitorList;
        } catch(InvalidDataAccessApiUsageException exception) {
            log.error(String.format(WEATHER_API_REPO_UPDATE_LOCATION_MSG.getMessage(), exception.getMessage()));
            log.debug(exception.getStackTrace());
            throw new LoadToLocationsToMonitorException();
        }
    }

    public LocationToMonitorListDTO retrieveMonitoredLocations() {
        return EntityMapper.convertToLocationToMonitorListDTO(locationRepository.findAllByIsMonitoredTrue());
    }
}
