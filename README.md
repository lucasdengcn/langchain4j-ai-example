# LangChain4j AI Example

## Project Overview

This project demonstrates the integration of LangChain4j with Spring Boot, showcasing various AI-powered features and implementation patterns. It consists of two main modules that work together to provide a complete AI-enhanced application:

- **MCP Server**: A Model Context Protocol server implementation supporting both WebFlux and WebMvc SSE
- **Proposal Agent**: An AI-powered proposal generation client with advanced PDF analysis capabilities

## Technology Stack

- Java 21
- Spring Boot 3.4.3
- LangChain4j 1.0.0-beta2
- Spring WebFlux/WebMvc
- Undertow (Web Server)
- H2 Database

## Features

### MCP Server

- Dual implementation support (WebFlux/WebMvc)
- Server-Sent Events (SSE) for real-time communication
- PostgreSQL integration with PGVector
- H2 database support for development

### Proposal Agent

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

The project uses Gradle for dependency management. Key dependencies include:

```gradle
// LangChain4j
implementation platform("dev.langchain4j:langchain4j-bom:${langchain4jVersion}")
implementation platform("dev.langchain4j:langchain4j-community-bom:${langchain4jVersion}")
implementation "dev.langchain4j:langchain4j-spring-boot-starter"
implementation "dev.langchain4j:langchain4j-reactor"

// AI Model Integrations
implementation "dev.langchain4j:langchain4j-community-dashscope"
implementation "dev.langchain4j:langchain4j-community-dashscope-spring-boot-starter"
implementation "dev.langchain4j:langchain4j-open-ai-spring-boot-starter"

// Spring Framework
implementation "org.springframework.boot:spring-boot-starter-actuator"
implementation "org.springframework.boot:spring-boot-starter-webflux"
implementation "org.springframework.boot:spring-boot-starter-undertow"
```

## Getting Started

1. Clone the repository
2. Configure your AI model credentials in `application.properties`
3. Build the project:

   ```bash
   ./gradlew build
   ```

4. Run the MCP server:

   ```bash
   ./gradlew mcp-server:bootRun
   ```

5. Run the proposal agent:

   ```bash
   ./gradlew proposal-agent:bootRun
   ```

## Project Structure

```
.
├── mcp-server/           # MCP Server implementation
│   ├── src/             # Server source code
│   └── README.md        # Server documentation
├── proposal-agent/      # MCP Client implementation
│   ├── src/             # Client source code
│   └── README.md        # Client documentation
└── src/                 # Common source code
```
