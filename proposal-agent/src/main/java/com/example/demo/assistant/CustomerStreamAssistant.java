package com.example.demo.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

@AiService
public interface CustomerStreamAssistant {

    @SystemMessage("You are a CRM Operator")
    Flux<String> chat(String userMessage);
}
