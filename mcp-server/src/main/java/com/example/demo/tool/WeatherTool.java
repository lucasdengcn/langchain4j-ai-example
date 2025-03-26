package com.example.demo.tool;

import com.example.demo.common.CustomToolCallResultConverter;
import com.example.demo.model.WeatherResponse;
import com.example.demo.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WeatherTool {

    private final WeatherService weatherService;
    public WeatherTool(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @Tool(description = "Get weather information by city name", name = "getWeather", resultConverter = CustomToolCallResultConverter.class)
    public WeatherResponse getWeather(String cityName) {
        return weatherService.getWeather(cityName);
    }

}
