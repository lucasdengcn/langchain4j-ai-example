# LangChain4j AI Example

## Overview

This project demonstrates the integration of LangChain4j with Spring Boot, showcasing AI-powered features through Model Context Protocol (MCP). It consists of two main modules:

- **MCP Server**: A server implementation supporting both WebFlux and WebMvc SSE for real-time AI model interactions
- **Proposal Agent**: An AI-powered proposal generation client with advanced PDF analysis capabilities

## Architecture

The project follows a modular architecture:

- **MCP Server Module**
  - Implements the Model Context Protocol server
  - Supports both reactive (WebFlux) and traditional (WebMvc) approaches
  - Handles AI model interactions and tool executions
  - Provides real-time communication via Server-Sent Events (SSE)

- **Proposal Agent Module**
  - Implements the MCP client for AI-powered proposal generation
  - Features advanced PDF analysis and processing
  - Provides real-time updates through SSE client
  - Integrates with Spring AI for enhanced capabilities

## Technology Stack

- Java 21
- Spring Boot 3.4.3
- LangChain4j 1.0.0-beta2
- Spring WebFlux/WebMvc
- PostgreSQL with PGVector
- H2 Database (for development)
- Undertow (Web Server)

## Features

### MCP Server

- Dual implementation support (WebFlux/WebMvc)
- Server-Sent Events (SSE) for real-time communication
- PostgreSQL integration with PGVector for production
- H2 database support for development
- Custom tool implementations for AI interactions
- Configurable model providers

### Proposal Agent

- MCP Client implementation with Spring AI integration
- WebFlux SSE Client support for real-time updates
- AI-powered proposal generation
- Advanced PDF Analysis capabilities:
  - Text extraction with position information
  - Image extraction and processing
  - Table structure detection
  - Header and footer detection
  - Page-to-image conversion with Base64 encoding

## Dependencies

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
2. Configure your AI model credentials in `application.properties`:

   ```properties
   # OpenAI Configuration
   langchain4j.open-ai.api-key=your_openai_api_key
   langchain4j.open-ai.model-name=gpt-3.5-turbo
   langchain4j.open-ai.temperature=0.7
   
   # DashScope Configuration (Optional)
   langchain4j.dashscope.api-key=your_dashscope_api_key
   ```

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
    ├── main/            # Main source code
    └── test/            # Test source code
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
