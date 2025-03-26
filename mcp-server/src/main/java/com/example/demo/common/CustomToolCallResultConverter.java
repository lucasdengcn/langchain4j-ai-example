package com.example.demo.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.execution.ToolCallResultConverter;

import java.lang.reflect.Type;

@Slf4j
public class CustomToolCallResultConverter implements ToolCallResultConverter {

    @Override
    public String convert(Object result, Type returnType) {
        log.info("CustomToolCallResultConverter.convert: result is {}", result);
        try {
            return ObjectMapperHolder.instance.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
