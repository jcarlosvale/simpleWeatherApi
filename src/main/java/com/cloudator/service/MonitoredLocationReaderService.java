package com.cloudator.service;

import com.cloudator.dto.MonitoredLocationListDTO;
import com.cloudator.logging.ErrorIds;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Service responsible by load the Location to be monitored from a Json File.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class MonitoredLocationReaderService {

    public MonitoredLocationListDTO readMonitoredLocationsFromJsonFile(@NonNull String jsonFilePath) {
        try {
            log.info("Reading the JSON file to load the monitored locations [file = {}]", jsonFilePath);
            MonitoredLocationListDTO monitoredLocationListDTO =
                    new ObjectMapper().readValue(new File(jsonFilePath), MonitoredLocationListDTO.class);
            return (Objects.isNull(monitoredLocationListDTO.getMonitoredLocationItemDTOList())) ?
                    MonitoredLocationListDTO.builder().monitoredLocationItemDTOList(new ArrayList<>()).build() :
                    monitoredLocationListDTO;
        } catch (IOException e) {
            log.error(String.format(ErrorIds.WEATHER_API_JSON_READER_MSG.getMessage(), jsonFilePath, e.getMessage()));
            log.debug(e.getStackTrace());
        }
        return MonitoredLocationListDTO.builder().monitoredLocationItemDTOList(new ArrayList<>()).build();
    }
}
