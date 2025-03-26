package com.example.demo.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

public interface CustomerAssistant {

    @SystemMessage("You are a CRM Operator")
    String chat(String userMessage);
}
