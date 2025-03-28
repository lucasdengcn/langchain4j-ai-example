package com.example.demo.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;


public interface CustomerStreamAssistant {

    @SystemMessage("You are a CRM Operator")
    TokenStream chat(String userMessage);
}
