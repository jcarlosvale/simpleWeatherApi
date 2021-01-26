package com.cloudator.service;

import com.cloudator.dto.MonitoredLocationItemDTO;
import com.cloudator.dto.MonitoredLocationListDTO;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonitoredLocationReaderServiceTest {

    @Test
    public void readJsonFileTest() throws URISyntaxException {

        List<MonitoredLocationItemDTO> expectedMonitoredLocationItemDTOList = new ArrayList<>();
        expectedMonitoredLocationItemDTOList.add(
                MonitoredLocationItemDTO.builder().latitude(10D).longitude(10D).minTemp(10.5).maxTemp(11D).build());
        expectedMonitoredLocationItemDTOList.add(
                MonitoredLocationItemDTO.builder().latitude(20D).longitude(20D).minTemp(20.5).maxTemp(21D).build());

        MonitoredLocationListDTO expectedMonitoredLocationListDTO =
                MonitoredLocationListDTO.builder().monitoredLocationItemDTOList(expectedMonitoredLocationItemDTOList).build();

        String absoluteFilePath = getAbsolutePathFrom("json/locationsToMonitor.json");
        MonitoredLocationReaderService monitoredLocationReaderService =
                new MonitoredLocationReaderService();
        MonitoredLocationListDTO actualLocationsList = monitoredLocationReaderService.readMonitoredLocationsFromJsonFile(absoluteFilePath);

        assertEquals(expectedMonitoredLocationListDTO, actualLocationsList);
    }

    private String getAbsolutePathFrom(String fullFileName) throws URISyntaxException {
        URL url = this.getClass().getClassLoader().getResource(fullFileName);
        assert url != null;
        File file = Paths.get(url.toURI()).toFile();
        return file.getAbsolutePath();
    }

}
