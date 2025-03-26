# Proposal Agent

## Overview

The Proposal Agent module implements the Model Context Protocol (MCP) client for making AI-powered proposals. It demonstrates how to integrate Spring AI with MCP client capabilities and provides advanced PDF analysis features.

## Features

- MCP Client implementation
- WebFlux SSE Client support
- AI-powered proposal generation
- Real-time updates via SSE
- PDF Analysis capabilities:
  - Text extraction with position information
  - Image extraction and processing
  - Table structure detection
  - Header and footer detection
  - Page-to-image conversion with Base64 encoding

## Dependencies

```gradle
// MCP Client
implementation 'org.springframework.ai:spring-ai-mcp-client-spring-boot-starter'

// WebFlux Support
implementation 'org.springframework.boot:spring-boot-starter-webflux'

// Web Support
implementation 'org.springframework.boot:spring-boot-starter-web'
implementation 'org.springframework.boot:spring-boot-starter-undertow'

// Database
implementation 'com.h2database:h2'
```

## Configuration

The module is configured to use:

- Spring Boot with WebFlux for reactive programming
- H2 database for development
- Undertow as the web server
- Spring AI MCP client for AI integration

## Project Structure

The Java source code under `src/main/java/com/example/demo` is organized into the following packages:

- `chat/`: Handles chat-related functionalities and interactions
- `common/`: Contains common utilities, configurations and shared components
- `model/`: Data models and DTOs for the application
- `service/`: Business logic and service implementations
- `sse/`: Server-Sent Events (SSE) implementation for real-time updates
- `tool/`: MCP Tool implementations for AI-powered proposal generation

The main application entry point is `DemoApplication.java`
