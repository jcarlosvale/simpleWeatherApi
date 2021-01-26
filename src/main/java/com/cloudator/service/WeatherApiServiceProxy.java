package com.cloudator.service;

import com.cloudator.dto.response.WeatherResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="open-weather-api-service", url="${weather.url}", fallbackFactory = HystrixClientFallbackFactory.class)
public interface WeatherApiServiceProxy {

    @GetMapping
    WeatherResponse forecastWeather(@RequestParam(name = "lat")   final double latitude,
                                    @RequestParam(name = "lon")   final double longitude,
                                    @RequestParam(name = "appid") final String id);

}

@Log4j2
@Component
class HystrixClientFallbackFactory implements FallbackFactory<WeatherApiServiceProxy> {
    @Override
    public WeatherApiServiceProxy create(Throwable cause) {
        return new WeatherApiServiceProxy() {
            @Override
            public WeatherResponse forecastWeather(double latitude, double longitude, String id) {
                log.error(cause);
                return null;
            }
        };
    }
}
