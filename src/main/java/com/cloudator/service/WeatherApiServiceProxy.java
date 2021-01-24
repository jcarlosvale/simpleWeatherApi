package com.cloudator.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="open-weather-api-service", url="localhost:8080")
public interface WeatherApiServiceProxy {

    @GetMapping(path = "/hello-world", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> helloWorld();

}
