package com.example.demo.chat;

import com.example.demo.service.ProposalChatService;
import com.example.demo.sse.SseEmitterReference;
import com.example.demo.sse.SseEmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/analysis")
@Slf4j
public class AnalysisStreamController {

    private final ProposalChatService proposalChatService;
    private final SseEmitterService sseEmitterService;

    public AnalysisStreamController(ProposalChatService proposalChatService, SseEmitterService sseEmitterService) {
        this.proposalChatService = proposalChatService;
        this.sseEmitterService = sseEmitterService;
    }

    @CrossOrigin(origins = "localhost:3000", allowCredentials = "true", allowedHeaders = "*")
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamAnalysis(@RequestParam String clientId) {
        log.debug("Streaming analysis started for client: {}", clientId); // 使用debug级别日志减少I/O开销
        SseEmitterReference emitterReference = sseEmitterService.createEmitter(clientId, emitter -> {
            proposalChatService.streamAnalysis(emitter);
            return true;
        });
        return emitterReference.getEmitter();
    }

    @PostMapping("/sendMessage")
    public void sendMessageToEmitter(@RequestParam String clientId, @RequestBody String message) {
        sseEmitterService.sendMessageToEmitter(clientId, message);
        log.debug("Message sent to client: {}", clientId);
    }
}