package com.cloudator.service;

import com.cloudator.dto.MonitoredLocationItemDTO;
import com.cloudator.dto.MonitoredLocationListDTO;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.cloudator.Util.getAbsolutePathFromResource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MonitoredLocationReaderServiceTest {

    private final MonitoredLocationReaderService monitoredLocationReaderService = new MonitoredLocationReaderService();

    @Test
    public void readJsonFileTest() throws URISyntaxException {

        List<MonitoredLocationItemDTO> expectedMonitoredLocationItemDTOList = new ArrayList<>();
        expectedMonitoredLocationItemDTOList.add(
                MonitoredLocationItemDTO.builder().latitude(10D).longitude(10D).minTemp(10.5).maxTemp(11D).build());
        expectedMonitoredLocationItemDTOList.add(
                MonitoredLocationItemDTO.builder().latitude(20D).longitude(20D).minTemp(20.5).maxTemp(21D).build());

        MonitoredLocationListDTO expectedMonitoredLocationListDTO =
                MonitoredLocationListDTO.builder().monitoredLocationItemDTOList(expectedMonitoredLocationItemDTOList).build();

        String absoluteFilePath = getAbsolutePathFromResource("json/locationsToMonitor.json");

        MonitoredLocationListDTO actualLocationsList = monitoredLocationReaderService.readMonitoredLocationsFromJsonFile(absoluteFilePath);

        assertEquals(expectedMonitoredLocationListDTO, actualLocationsList);
    }

    @Test
    public void readInvalidJsonFileFormatTest() throws URISyntaxException {

        String absoluteFilePath = getAbsolutePathFromResource("json/invalidFormat.json");

        MonitoredLocationListDTO actualLocationsList = monitoredLocationReaderService.readMonitoredLocationsFromJsonFile(absoluteFilePath);

        assertTrue(actualLocationsList.getMonitoredLocationItemDTOList().isEmpty());
    }

    @Test
    public void readInvalidPathJsonFileTest() {

        String absoluteFilePath = "invalid path";

        MonitoredLocationListDTO actualLocationsList = monitoredLocationReaderService.readMonitoredLocationsFromJsonFile(absoluteFilePath);

        assertTrue(actualLocationsList.getMonitoredLocationItemDTOList().isEmpty());
    }
}
