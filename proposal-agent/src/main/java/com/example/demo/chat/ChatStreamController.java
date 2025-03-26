package com.example.demo.chat;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.demo.service.ProposalChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatStreamController {

    private final ProposalChatService proposalChatService;

    public ChatStreamController(ProposalChatService proposalChatService) {
        this.proposalChatService = proposalChatService;
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestBody String message) {
        SseEmitter emitter = new SseEmitter();
        proposalChatService.streamChat(message, emitter);
        return emitter;
    }
}