package com.example.demo.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperHolder implements InitializingBean {

    private final ObjectMapper objectMapper;

    public ObjectMapperHolder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static ObjectMapper instance;

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = objectMapper;
    }

}
