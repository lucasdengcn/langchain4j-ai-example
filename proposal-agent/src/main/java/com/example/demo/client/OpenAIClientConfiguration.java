package com.example.demo.client;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OpenAIClientConfiguration {

    @Bean
    public OpenAIClient openAIClient() {
        String dashscopeApiKey = System.getenv("DASHSCOPE_API_KEY");
        log.info("Dashscope API Key: {}", dashscopeApiKey);
        //
        return OpenAIOkHttpClient.builder()
                .apiKey(dashscopeApiKey)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }

}
