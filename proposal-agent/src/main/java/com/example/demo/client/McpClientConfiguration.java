package com.example.demo.client;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;


@Slf4j
@Configuration
public class McpClientConfiguration {

    @Bean
    public McpTransport mcpHttpClientTransport() {
        return new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8180/sse")
                .timeout(Duration.ofSeconds(60))
                .logRequests(true)
                .logResponses(true).build();
    }

    @Bean
    public McpClient mcpHttpClient(McpTransport mcpHttpClientTransport){
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(mcpHttpClientTransport)
                .build();
        //
//        mcpClient.listTools().forEach(toolSpecification -> {
//            log.info("mcpHttpClient toolSpecification: {}", toolSpecification);
//        });
        return mcpClient;
    }

    @Bean
    public ToolProvider toolProvider(McpClient mcpHttpClient, McpClient mcpBrowserUseClient){
        return McpToolProvider.builder()
                .mcpClients(mcpHttpClient)
                .failIfOneServerFails(true)
                .build();
    }

    @Bean
    public McpTransport mcpBrowserUseTransport() {
        return new StdioMcpTransport.Builder().command(List.of("uvenv", "run", "mcp-browser-use")).logEvents(true).build();
    }

    @Bean
    public McpClient mcpBrowserUseClient(McpTransport mcpBrowserUseTransport){
        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(mcpBrowserUseTransport)
                .build();
        //
//        mcpClient.listTools().forEach(toolSpecification -> {
//            // log.info("mcpBrowserUseClient toolSpecification: {}", toolSpecification);
//        });
        return mcpClient;
    }
}
