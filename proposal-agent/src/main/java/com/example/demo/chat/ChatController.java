package com.example.demo.chat;

import com.example.demo.assistant.CustomerAssistant;
import com.example.demo.service.ProposalChatService;
import com.example.demo.sse.SseEmitterReference;
import com.example.demo.sse.SseEmitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Slf4j
@RestController
public class ChatController {
    //
    private final ProposalChatService proposalChatService;
    private final SseEmitterService sseEmitterService;

    public ChatController(ProposalChatService proposalChatService, SseEmitterService sseEmitterService) {
        this.proposalChatService = proposalChatService;
        this.sseEmitterService = sseEmitterService;
        //
    }

    /**
     * High-Level AI Call
     * @param name
     * @return
     */
    @GetMapping("/ai/customer")
    public Map<String,String> customerInfo(@RequestParam(value = "name", defaultValue = "lucas") String name) {
        String message = "Please help to get this customer info via name, " + name;
        String content = proposalChatService.chat(message);
        return Map.of("generation", content);
    }

    /**
     * Low-Level AI Call
     *
     * @param email
     * @return
     */
    @GetMapping("/ai/customerByEmail")
    public Map<String,String> customerInfoV2(@RequestParam(value = "email", defaultValue = "lucas@example.com") String email) {
        String message = "Please help to get this customer info via email, " + email;
        String content = proposalChatService.chatLowLevel(message);
        return Map.of("generation", content);
    }

//    @GetMapping("/ai/weather")
//    public Map<String,String> weather(@RequestParam(value = "city", defaultValue = "Shenzhen") String city) {
//        String message = "What's the weather like in " + city + " China?";
//        // String content = ChatClient.create(chatModel).prompt(message).tools(WeatherTools.CURRENT_WEATHER).call().content();
//        String content = weatherChatClient.prompt(message).call().content();
//        return Map.of("generation", content);
//    }

    @GetMapping("/ai/datetime")
    public Map<String,String> datetime(@RequestParam(value = "message", defaultValue = "What day is tomorrow?") String message) {
        String content = proposalChatService.chat(message);
        return Map.of("generation", content);
    }

    @PostMapping("/ai/alarm")
    public Map<String,String> setAlarm(@RequestParam(value = "message", defaultValue = "What is the time now? Can you set an alarm 10 minutes from now?") String message) {
        String content = proposalChatService.chat(message);
        return Map.of("generation", content);
    }

    @GetMapping("/ai/generate")
    public Map<String,String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", this.proposalChatService.chatLowLevel(message));
    }

    // @CrossOrigin(origins = "localhost:3000", allowCredentials = "true", allowedHeaders = "*")
    @PostMapping(path = "/ai/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        SseEmitterReference emitterReference = sseEmitterService.createEmitter("abc", emitter -> {
            proposalChatService.streamChat(message, emitter);
            return true;
        });
        return emitterReference.getEmitter();
    }

}
