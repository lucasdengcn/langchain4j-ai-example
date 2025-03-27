package com.example.demo.sse;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.function.Function;

@Slf4j
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
        this.emitter = new SseEmitter(60000L); // 缩短超时时间为60秒
        this.emitter.onTimeout(() -> {
            this.emitter.complete();
            timeoutListener.apply(clientId);
        });

        // 释放信号量许可
        this.emitter.onCompletion(() -> {
            completionListener.apply(clientId);
        });

        this.emitter.onError((ex) -> {
            log.error("Error occurred for clientId: {}", clientId, ex);
            errorListener.apply(clientId);
        });
    }
}
