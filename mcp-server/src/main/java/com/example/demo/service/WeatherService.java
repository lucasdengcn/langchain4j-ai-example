package com.example.demo.service;

import com.example.demo.model.Unit;
import com.example.demo.model.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WeatherService {

    public WeatherResponse getWeather(String cityName) {
        log.info("Weather Request: {}", cityName);
        return new WeatherResponse(20.0, Unit.CELSIUS);
    }

}
