package com.cloudator.service;

import com.cloudator.configuration.WeatherApiProperties;
import com.cloudator.dto.LocationToMonitorItemDTO;
import com.cloudator.dto.LocationToMonitorListDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocationToMonitorReaderServiceTest {

    @Test
    public void readJsonFileTest() throws URISyntaxException {

        List<LocationToMonitorItemDTO> expectedLocationToMonitorItemDTOList = new ArrayList<>();
        expectedLocationToMonitorItemDTOList.add(
                LocationToMonitorItemDTO.builder().latitude(10D).longitude(10D).minTemp(10.5).maxTemp(11D).build());
        expectedLocationToMonitorItemDTOList.add(
                LocationToMonitorItemDTO.builder().latitude(20D).longitude(20D).minTemp(20.5).maxTemp(21D).build());

        LocationToMonitorListDTO expectedLocationToMonitorListDTO =
                LocationToMonitorListDTO.builder().locationToMonitorItemDTOList(expectedLocationToMonitorItemDTOList).build();

        String absoluteFilePath = getAbsolutePathFrom("json/locationsToMonitor.json");
        WeatherApiProperties weatherApiProperties = new WeatherApiProperties();
        weatherApiProperties.setAbsoluteFilePath(absoluteFilePath);
        ObjectMapper objectMapper = new ObjectMapper();
        LocationToMonitorReaderService locationToMonitorReaderService =
                new LocationToMonitorReaderService(weatherApiProperties, objectMapper);
        LocationToMonitorListDTO actualLocationsList = locationToMonitorReaderService.readLocations();

        assertEquals(expectedLocationToMonitorListDTO, actualLocationsList);
    }

    private String getAbsolutePathFrom(String fullFileName) throws URISyntaxException {
        URL url = this.getClass().getClassLoader().getResource(fullFileName);
        File file = Paths.get(url.toURI()).toFile();
        return file.getAbsolutePath();
    }

}
