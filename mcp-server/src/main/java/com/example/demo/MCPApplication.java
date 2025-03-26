package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class MCPApplication {

    public static void main(String[] args) {
        SpringApplication.run(MCPApplication.class, args);
    }

}
