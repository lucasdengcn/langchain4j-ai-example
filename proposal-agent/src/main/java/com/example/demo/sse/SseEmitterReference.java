package com.example.demo.sse;

import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.function.Function;

@Getter
public class SseEmitterReference {

    private SseEmitter emitter;
    private final String clientId;
    private final Function<String, Void> timeoutListener;
    private final Function<String, Void> completionListener;
    private final Function<String, Void> errorListener;

    public SseEmitterReference(String clientId,
                               Function<String, Void> timeoutListener,
                               Function<String, Void> completionListener,
                               Function<String, Void> errorListener) {
        //
        this.clientId = clientId;
        this.timeoutListener = timeoutListener;
        this.completionListener = completionListener;
        this.errorListener = errorListener;
    }

    public void create(){
        this.emitter = new SseEmitter(30000L); // 缩短超时时间为30秒
        this.emitter.onTimeout(() -> {
            this.emitter.complete();
            timeoutListener.apply(clientId);
        });

        // 释放信号量许可
        this.emitter.onCompletion(() -> {
            completionListener.apply(clientId);
        });

        this.emitter.onError((ex) -> {
            errorListener.apply(clientId);
        });
    }
}
