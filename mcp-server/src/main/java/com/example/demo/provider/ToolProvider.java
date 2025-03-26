package com.example.demo.provider;

import com.example.demo.tool.WeatherTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * expose via MCP Server
 */
@Configuration
public class ToolProvider {

    @Bean
    public ToolCallbackProvider weatherToolProvider(WeatherTool weatherTool) {
        return MethodToolCallbackProvider.builder().toolObjects(weatherTool).build();
    }
}
