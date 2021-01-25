package com.cloudator.service;

import com.cloudator.configuration.WeatherApiProperties;
import com.cloudator.dto.LocationToMonitorItem;
import com.cloudator.dto.LocationToMonitorList;
import com.cloudator.logging.ErrorIds;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class LocationToMonitorReaderService {

    private final WeatherApiProperties weatherApiProperties;
    private  final ObjectMapper objectMapper;

    public List<LocationToMonitorItem> readLocations() {
        String absoluteFilePath = weatherApiProperties.getAbsoluteFilePath();
        try {
            return readLocations(absoluteFilePath);
        } catch (IOException e) {
            log.error(String.format(ErrorIds.WEATHER_API_JSON_READER_MSG.getMessage(), absoluteFilePath, e.getMessage()));
            log.debug(e.getStackTrace());
        }
        return new ArrayList<>();
    }

    private List<LocationToMonitorItem> readLocations(String absoluteFilePath) throws IOException {
        LocationToMonitorList locationToMonitorList =
                objectMapper.readValue(new File(absoluteFilePath), LocationToMonitorList.class);
        return locationToMonitorList.getLocationToMonitorItemList();
    }
}
