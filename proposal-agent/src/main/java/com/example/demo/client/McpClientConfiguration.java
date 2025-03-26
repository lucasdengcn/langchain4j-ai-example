package com.example.demo.client;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class McpClientConfiguration {

    @Bean
    public McpTransport mcpClientTransport() {
        return new HttpMcpTransport.Builder().sseUrl("http://localhost:8180/sse").logRequests(true).logResponses(true).build();
    }

    @Bean
    public McpClient mcpClient(McpTransport transport){
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();
        //
        mcpClient.listTools().forEach(toolSpecification -> {
            log.info("MCP toolSpecification: {}", toolSpecification);
        });
        return mcpClient;
    }

    @Bean
    public ToolProvider toolProvider(McpClient mcpClient){
        return McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();
    }
}
