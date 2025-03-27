package com.example.demo.service;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import com.example.demo.assistant.CustomerAssistant;
import com.example.demo.assistant.CustomerStreamAssistant;
import com.example.demo.tool.DateTimeTools;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.DefaultToolExecutor;
import dev.langchain4j.service.tool.ToolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.model.AnalysisResult;
import com.example.demo.model.AnalysisResultMessage;
import com.example.demo.model.MyChatMessage;
import com.example.demo.tool.CustomerTools;

import reactor.core.publisher.Flux;

@Slf4j
@Service
public class ProposalChatService {

    private final OpenAiChatModel openAiChatModel;
    private final OpenAiStreamingChatModel openAiStreamingChatModel;
    //
    private final CustomerAssistant customerAssistant;
    private final CustomerStreamAssistant customerStreamAssistant;
    //
    private final List<ToolSpecification> toolSpecifications;

    List<MyChatMessage> messages = new ArrayList<>();
    AnalysisResultMessage analysisResultMessage = null;

    public ProposalChatService(OpenAiChatModel openAiChatModel,
                               OpenAiStreamingChatModel openAiStreamingChatModel,
                               CustomerAssistant customerAssistant,
                               CustomerStreamAssistant customerStreamAssistant) {
        //
        this.openAiChatModel = openAiChatModel;
        this.openAiStreamingChatModel = openAiStreamingChatModel;
        this.customerAssistant = customerAssistant;
        this.customerStreamAssistant = customerStreamAssistant;
        //
        this.toolSpecifications = ToolSpecifications.toolSpecificationsFrom(CustomerTools.class);
        toolSpecifications.addAll(ToolSpecifications.toolSpecificationsFrom(DateTimeTools.class));
        //
        messages.add(new MyChatMessage("Analyzing document structure..."));
        messages.add(new MyChatMessage("Extracting coverage information..."));
        messages.add(new MyChatMessage("Evaluating risk factors..."));
        messages.add(new MyChatMessage("Analyzing document structure..."));
        //
        Map<String, Double> insuranceMap = Map.of(
                "Auto Insurance", 45.0,
                "Home Insurance", 30.0,
                "Health Insurance", 20.0,
                "Life Insurance", 5.0);
        Map<String, Double> riskMap = Map.of(
                "Market Risk", 6.2,
                "Health Risk", 8.1,
                "Liability Risk", 4.5,
                "Natural Disaster Risk", 3.8);
        analysisResultMessage = new AnalysisResultMessage("Analysis complete!",
                new AnalysisResult(insuranceMap, riskMap));
    }

    /**
     * High-Level
     * @param message
     * @return
     */
    public String chat(String message) {
        return customerAssistant.chat(message);
    }

    /**
     * Low-Level Tool Execution
     * @param message
     * @return
     */
    @Deprecated
    public String chatLowLevel(String message) {
        UserMessage userMessage = UserMessage.from(message);
        ChatRequest chatRequest = ChatRequest.builder()
                .messages(userMessage)
                .toolSpecifications(toolSpecifications)
                .build();
        ChatResponse chatResponse = this.openAiChatModel.chat(chatRequest);
        AiMessage aiMessage = chatResponse.aiMessage();
        if (aiMessage.hasToolExecutionRequests()){
            List<ChatMessage> chatMessages = new ArrayList<>();
            chatMessages.add(userMessage);
            //
            chatMessages.add(AiMessage.aiMessage(aiMessage.toolExecutionRequests()));
            aiMessage.toolExecutionRequests().forEach(toolExecutionRequest -> {
                try {
                    ToolExecutor toolExecutor = null;
                    if (toolExecutionRequest.name().contains("getCustomer")){
                        toolExecutor = new DefaultToolExecutor(new CustomerTools(), toolExecutionRequest);
                    } else if (toolExecutionRequest.name().contains("getCurrentDateTime")){
                        toolExecutor = new DefaultToolExecutor(new DateTimeTools(), toolExecutionRequest);
                    } else if (toolExecutionRequest.name().contains("setAlarm")){
                        toolExecutor = new DefaultToolExecutor(new DateTimeTools(), toolExecutionRequest);
                    }
                    assert toolExecutor != null;
                    String result = toolExecutor.execute(toolExecutionRequest, UUID.randomUUID().toString());
                    ToolExecutionResultMessage toolExecutionResultMessage = ToolExecutionResultMessage.from(toolExecutionRequest, result);
                    chatMessages.add(toolExecutionResultMessage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            //
            ChatRequest chatRequest2 = ChatRequest.builder()
                    .messages(chatMessages)
                    .toolSpecifications(toolSpecifications)
                    .build();
            ChatResponse chatResponse2 = this.openAiChatModel.chat(chatRequest2);
            return chatResponse2.aiMessage().text();
        }
        return aiMessage.text();
    }

    @Deprecated
    public void streamChatLowLevel(String message, SseEmitter emitter) {
        ChatRequest chatRequest = ChatRequest.builder()
                .messages(UserMessage.from(message))
                .toolSpecifications(this.toolSpecifications)
                .build();
        this.openAiStreamingChatModel.chat(chatRequest, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                try {
                    emitter.send(partialResponse);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                try {
                    emitter.send(completeResponse.aiMessage().text());
                    emitter.complete();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(Throwable error) {
                emitter.completeWithError(error);
            }
        });
    }

    public void streamChat(String message, SseEmitter emitter) {
        log.info("streamChat Accept: {}, {}", message, emitter);
        TokenStream tokenStream = this.customerStreamAssistant.chat(message);
        log.info("tokenStream: {}", tokenStream);
        tokenStream.onPartialResponse(text -> {
            try {
                emitter.send(text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).onRetrieved(content -> {
            log.info("contents: {}", content);
        }).onToolExecuted(toolExecutionResult -> {
            log.info("toolExecutionResult: {}", toolExecutionResult);
        }).onCompleteResponse(completeResponse -> {
            log.info("completeResponse: {}", completeResponse);
            try {
                emitter.send(completeResponse.aiMessage().text());
            } catch (IOException e) {
                emitter.completeWithError(e);
                throw new RuntimeException(e);
            }finally {
                emitter.complete();
            }
        }).onError(errorContext -> {
            log.info("onError(): {}", errorContext.getMessage());
            emitter.completeWithError(errorContext);
        }).start();
        log.info("streamChat Start: {}", message);

//        this.customerStreamAssistant.chat(message)
//                .log("streamChat-flow")
//                .doOnNext(text -> {
//            try {
//                emitter.send(text);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).doOnComplete(emitter::complete)
//        .doOnError(emitter::completeWithError)
//                .subscribe();
    }

    public void streamAnalysis(SseEmitter emitter) {
        //
        Flux.interval(Duration.ofSeconds(1))
                .take(messages.size())
                .map(i -> messages.get(i.intValue()))
                .doOnNext(message -> {
                    try {
                        emitter.send(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnComplete(() -> {
                    try {
                        emitter.send(analysisResultMessage);
                        emitter.complete();
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                })
                .doOnError(emitter::completeWithError)
                .subscribe();
    }
}