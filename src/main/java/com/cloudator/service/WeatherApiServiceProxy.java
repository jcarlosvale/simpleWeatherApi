package com.cloudator.service;

import com.cloudator.dto.WeatherForecastItem;
import com.cloudator.dto.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="open-weather-api-service", url="${weather.url}", primary = true)
public interface WeatherApiServiceProxy {

    @GetMapping
    WeatherResponse forecastWeather(@RequestParam(name = "lat")   final double latitude,
                                    @RequestParam(name = "lon")   final double longitude,
                                    @RequestParam(name = "appid") final String id);

}
