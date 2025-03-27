package com.example.demo.client;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import java.time.Duration;
import java.util.List;


@Slf4j
@Configuration
public class McpClientConfiguration {

    @Bean
    public McpClient mcpHttpSseClient(){
        HttpMcpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8180/sse")
                .timeout(Duration.ofSeconds(6000))
                .logRequests(true)
                .logResponses(true).build();
        //
        //
        return new DefaultMcpClient.Builder()
                .transport(transport)
                .build();
    }

    @Bean
    public ToolProvider toolProvider(McpClient mcpHttpSseClient, McpClient mcpBrowserUseClient){
        return McpToolProvider.builder()
                .mcpClients(mcpHttpSseClient, mcpBrowserUseClient)
                .failIfOneServerFails(true)
                .build();
    }

    @Bean
    public McpClient mcpBrowserUseClient(){
        StdioMcpTransport transport = new StdioMcpTransport.Builder().command(List.of("uvenv", "run", "mcp-browser-use")).logEvents(true).build();
        return new DefaultMcpClient.Builder()
                .transport(transport)
                .build();
    }
}
