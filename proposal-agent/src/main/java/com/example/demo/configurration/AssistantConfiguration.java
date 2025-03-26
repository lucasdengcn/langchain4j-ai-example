package com.example.demo.configurration;

import com.example.demo.assistant.CustomerAssistant;
import com.example.demo.assistant.CustomerStreamAssistant;
import com.example.demo.tool.CustomerTools;
import com.example.demo.tool.DateTimeTools;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantConfiguration {

    @Bean
    public CustomerAssistant customerAssistant(OpenAiChatModel openAiChatModel,
                                               ToolProvider toolProvider,
                                               CustomerTools customerTools,
                                               DateTimeTools dateTimeTools) {
        //
        CustomerAssistant assistant = AiServices.builder(CustomerAssistant.class)
                .chatLanguageModel(openAiChatModel)
                .toolProvider(toolProvider)
                // .tools(customerTools, dateTimeTools)
                .build();
        return assistant;
    }

    @Bean
    public CustomerStreamAssistant customerStreamAssistant(OpenAiStreamingChatModel openAiStreamingChatModel,
                                                           ToolProvider toolProvider,
                                                           CustomerTools customerTools,
                                                           DateTimeTools dateTimeTools) {
        //
        CustomerStreamAssistant assistant = AiServices.builder(CustomerStreamAssistant.class)
                .streamingChatLanguageModel(openAiStreamingChatModel)
                .toolProvider(toolProvider)
                // .tools(customerTools, dateTimeTools)
                .build();
        return assistant;
    }
}
