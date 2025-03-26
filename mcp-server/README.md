# MCP Server Implementation

## Overview

The MCP Server module provides a robust implementation of the Model Context Protocol (MCP) server with support for both WebFlux and WebMvc SSE (Server-Sent Events).

## Features

- Dual implementation support (WebFlux/WebMvc)
- Server-Sent Events (SSE) for real-time communication
- PostgreSQL integration with PGVector
- H2 database support for development

## Implementation Options

### WebFlux SSE

Reactive implementation using Spring WebFlux:

```gradle
// WebFlux Implementation
implementation 'io.modelcontextprotocol.sdk:mcp-spring-webflux'
implementation 'org.springframework.boot:spring-boot-starter-webflux'
implementation 'org.springframework.ai:spring-ai-mcp-server-webflux-spring-boot-starter'
```

### WebMvc SSE

Traditional implementation using Spring WebMvc:

```gradle
// WebMvc Implementation
implementation 'org.springframework.ai:spring-ai-mcp-server-webmvc-spring-boot-starter'
implementation 'org.springframework.boot:spring-boot-starter-web'
```

## Additional Dependencies

```gradle
// Database Support
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation 'org.postgresql:postgresql'
implementation 'com.h2database:h2'

// Web Server
implementation 'org.springframework.boot:spring-boot-starter-undertow'
```

## Configuration

The server is configured to use:

- Spring Boot with choice of WebFlux or WebMvc
- PostgreSQL with PGVector for production
- H2 database for development
- Undertow as the web server
- Spring AI MCP server integration

## Project Structure

The Java source code under `src/main/java/com/example/demo` is organized into the following packages:

- `common/`: Contains common utilities and configurations
  - `CustomToolCallResultConverter.java`: Custom converter for tool call results
  - `ObjectMapperHolder.java`: Centralized JSON object mapper configuration

- `model/`: Data models and DTOs
  - `Unit.java`: Unit representation model
  - `WeatherRequest.java`: Weather request data model
  - `WeatherResponse.java`: Weather response data model

- `provider/`: MCP Tools providers and factories
  - `ToolProvider.java`: Provider for tool-related functionalities

- `service/`: Business logic and services
  - `WeatherService.java`: Service handling weather-related operations

- `tool/`: MCP Tool implementations
  - `WeatherTool.java`: Implementation of weather-related tools

The main application entry point is `MCPApplication.java`
