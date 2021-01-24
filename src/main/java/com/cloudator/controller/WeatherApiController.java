package com.cloudator.controller;

import com.cloudator.service.WeatherApiServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherApiController {

    private final WeatherApiServiceProxy weatherApiServiceProxy;

    @GetMapping(path = "hello")
    public ResponseEntity<String> hello() {
        return weatherApiServiceProxy.helloWorld();
    }
}
