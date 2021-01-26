package com.cloudator.controller;

import com.cloudator.configuration.WeatherApiProperties;
import com.cloudator.dto.ForecastAlertDTO;
import com.cloudator.dto.MonitoredLocationItemDTO;
import com.cloudator.dto.MonitoredLocationListDTO;
import com.cloudator.dto.response.WeatherForecastItem;
import com.cloudator.dto.response.WeatherResponse;
import com.cloudator.entity.ForecastAlert;
import com.cloudator.repository.ForecastAlertRepository;
import com.cloudator.repository.LocationRepository;
import com.cloudator.service.ForecastAlertService;
import com.cloudator.service.WeatherApiServiceProxy;
import com.cloudator.service.util.EntityMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.cloudator.Util.getAbsolutePathFromResource;
import static com.cloudator.Util.getTimestampInSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class WeatherApiControllerTest {

    ObjectMapper om = new ObjectMapper();

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ForecastAlertRepository forecastAlertRepository;

    @Autowired
    private WeatherApiProperties weatherApiProperties;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherApiServiceProxy weatherApiServiceProxy;

    @Autowired
    private ForecastAlertService forecastAlertService;

    @BeforeEach
    public void setup() {
        locationRepository.deleteAll();
        forecastAlertRepository.deleteAll();
    }

    @Test
    public void loadLocationsTest() throws Exception {
        //verify is empty
        MonitoredLocationListDTO actualMonitoredLocationListDTO =
                om.readValue(
                    this.mockMvc
                            .perform(get("/getMonitoredLocations"))
                            .andDo(print())
                            .andExpect(status().isOk())
                            .andReturn()
                            .getResponse()
                            .getContentAsString(),
                        MonitoredLocationListDTO.class
                );
        assertTrue(actualMonitoredLocationListDTO.getMonitoredLocationItemDTOList().isEmpty());

        //expected result
        List<MonitoredLocationItemDTO> expectedMonitoredLocationItemDTOList = new ArrayList<>();
        expectedMonitoredLocationItemDTOList.add(
                MonitoredLocationItemDTO.builder().latitude(10D).longitude(10D).minTemp(10.5).maxTemp(11D).build());
        expectedMonitoredLocationItemDTOList.add(
                MonitoredLocationItemDTO.builder().latitude(20D).longitude(20D).minTemp(20.5).maxTemp(21D).build());

        MonitoredLocationListDTO expectedMonitoredLocationListDTO =
                MonitoredLocationListDTO.builder().monitoredLocationItemDTOList(expectedMonitoredLocationItemDTOList).build();

        //set file location
        weatherApiProperties.setAbsoluteFilePath(getAbsolutePathFromResource("json/locationsToMonitor.json"));

        actualMonitoredLocationListDTO =
                om.readValue(
                        this.mockMvc
                                .perform(put("/loadLocations"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        MonitoredLocationListDTO.class
                );

        assertEquals(expectedMonitoredLocationListDTO, actualMonitoredLocationListDTO);

        //verify get
        actualMonitoredLocationListDTO =
                om.readValue(
                        this.mockMvc
                                .perform(get("/getMonitoredLocations"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        MonitoredLocationListDTO.class
                );
        assertEquals(expectedMonitoredLocationListDTO, actualMonitoredLocationListDTO);
    }

    @Test
    public void generateForecastAlertTest() throws Exception {
        //set monitored locations
        weatherApiProperties.setAbsoluteFilePath(getAbsolutePathFromResource("json/locationsToMonitor.json"));
        weatherApiProperties.setId("1");

        MonitoredLocationListDTO actualMonitoredLocationListDTO =
                om.readValue(
                        this.mockMvc
                                .perform(put("/loadLocations"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        MonitoredLocationListDTO.class
                );

        //mock the external api call
        List<WeatherForecastItem> weatherForestResponseList1 = new ArrayList<>();

        weatherForestResponseList1.add(
                WeatherForecastItem
                        .builder()
                        .timestamp(getTimestampInSeconds(LocalDateTime.now().plusDays(1)))
                        .dateInText("1")
                        .minTemp(9d)
                        .maxTemp(11d)
                        .build()
        );
        weatherForestResponseList1.add(
                WeatherForecastItem
                        .builder()
                        .timestamp(getTimestampInSeconds(LocalDateTime.now().plusDays(2)))
                        .dateInText("3")
                        .minTemp(11d)
                        .maxTemp(10d)
                        .build()
        );
        weatherForestResponseList1.add(
                WeatherForecastItem
                        .builder()
                        .timestamp(getTimestampInSeconds(LocalDateTime.now().plusDays(3)))
                        .dateInText("5")
                        .minTemp(10.5d)
                        .maxTemp(11d)
                        .build()
        );

        WeatherResponse weatherResponse1 =
                WeatherResponse
                        .builder()
                        .latitude(10d)
                        .longitude(10d)
                        .weatherForecastResponseList(weatherForestResponseList1)
                        .build();

        List<WeatherForecastItem> weatherForestResponseList2 = new ArrayList<>();
        weatherForestResponseList2.add(
                WeatherForecastItem
                        .builder()
                        .timestamp(getTimestampInSeconds(LocalDateTime.now().plusDays(1)))
                        .dateInText("2")
                        .minTemp(21d)
                        .maxTemp(20d)
                        .build()
        );
        weatherForestResponseList2.add(
                WeatherForecastItem
                        .builder()
                        .timestamp(getTimestampInSeconds(LocalDateTime.now().plusDays(2)))
                        .dateInText("4")
                        .minTemp(20.5d)
                        .maxTemp(21d)
                        .build()
        );
        weatherForestResponseList2.add(
                WeatherForecastItem
                        .builder()
                        .timestamp(getTimestampInSeconds(LocalDateTime.now().plusDays(3)))
                        .dateInText("6")
                        .minTemp(15d)
                        .maxTemp(25d)
                        .build()
        );
        WeatherResponse weatherResponse2 =
                WeatherResponse
                        .builder()
                        .latitude(20d)
                        .longitude(20d)
                        .weatherForecastResponseList(weatherForestResponseList2)
                        .build();

        Mockito
                .when(weatherApiServiceProxy.forecastWeather(weatherResponse1.getLatitude(), weatherResponse1.getLongitude(), "1"))
                .thenReturn(weatherResponse1);

        Mockito
                .when(weatherApiServiceProxy.forecastWeather(weatherResponse2.getLatitude(), weatherResponse2.getLongitude(), "1"))
                .thenReturn(weatherResponse2);

        //Expected alerts
        List<ForecastAlert> expectedAlerts = new ArrayList<>();
        expectedAlerts.add(EntityMapper.convertToEntity(weatherForestResponseList1.get(0), weatherResponse1.getLatitude(), weatherResponse1.getLongitude()));
        expectedAlerts.add(EntityMapper.convertToEntity(weatherForestResponseList1.get(2), weatherResponse1.getLatitude(), weatherResponse1.getLongitude()));

        expectedAlerts.add(EntityMapper.convertToEntity(weatherForestResponseList2.get(1), weatherResponse2.getLatitude(), weatherResponse2.getLongitude()));
        expectedAlerts.add(EntityMapper.convertToEntity(weatherForestResponseList2.get(2), weatherResponse2.getLatitude(), weatherResponse2.getLongitude()));


        //Execute external call
        List<ForecastAlert> actualAlerts = forecastAlertService.verifyWeather();

        assertEquals(expectedAlerts, actualAlerts);

        //verify by endpoint
        List<ForecastAlertDTO> actualForecastAlertListDTO =
                om.readValue(
                        this.mockMvc
                                .perform(get("/forecast"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        new TypeReference<List<ForecastAlertDTO>>(){}
                );

        expectedAlerts.sort(Comparator.comparing(ForecastAlert::getTimestamp));
        List<ForecastAlertDTO> expectedForecastAlertListDTO =
                expectedAlerts.stream().map(EntityMapper::convertToDTO).collect(Collectors.toList());

        assertEquals(expectedForecastAlertListDTO, actualForecastAlertListDTO);
    }
}
