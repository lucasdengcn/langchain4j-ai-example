# Proposal Agent

## Overview

The Proposal Agent module is a sophisticated AI-powered proposal generation system that implements the Model Context Protocol (MCP) client. It seamlessly integrates Spring AI with MCP client capabilities and provides advanced PDF analysis features for comprehensive document processing.

## Features

### AI Integration

- MCP Client implementation with Spring AI
- AI-powered proposal generation
- Real-time updates via Server-Sent Events (SSE)

### PDF Analysis

- Text extraction with position information
- Image extraction and processing
- Table structure detection
- Header and footer detection
- Page-to-image conversion with Base64 encoding

### Real-time Communication

- WebFlux SSE Client support
- Bi-directional data flow
- Event-driven architecture

## Configuration

The module leverages:

- Spring Boot with WebFlux for reactive programming
- H2 database for development
- Undertow as the web server
- Spring AI MCP client for AI integration

## Project Structure

The source code under `src/main/java/com/example/demo` is organized into:

- `chat/`: Chat-related functionalities and interactions
- `common/`: Common utilities and shared components
- `model/`: Data models and DTOs
- `service/`: Business logic implementations
- `sse/`: Server-Sent Events implementation
- `tool/`: MCP Tool implementations

The main application entry point is `DemoApplication.java`

## MCP Client Implementation

- langchain4j implementation is not ready for production, it lacks of reconnect and retry features, connection pools.
