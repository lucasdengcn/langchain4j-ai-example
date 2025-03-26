package com.example.demo.sse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

@Service
public class SseEmitterService {

    private final ExecutorService executorService;
    private final Semaphore semaphore;
    private final ConcurrentHashMap<String, SseEmitterReference> emitters = new ConcurrentHashMap<>();

    private final Function<String, Void> timeoutListener = new Function<>() {
        @Override
        public Void apply(String clientId) {
            semaphore.release(); // 释放信号量许可
            emitters.remove(clientId);
            return null;
        }
    };

    private final Function<String, Void> completionListener = new Function<>() {
        @Override
        public Void apply(String clientId) {
            semaphore.release(); // 释放信号量许可
            emitters.remove(clientId);
            return null;
        }
    };
    private final Function<String, Void> errorListener = new Function<>() {
        @Override
        public Void apply(String clientId) {
            semaphore.release(); // 释放信号量许可
            emitters.remove(clientId);
            return null;
        }
    };

    public SseEmitterService() {
        this.executorService = Executors.newFixedThreadPool(50); // 创建固定大小的线程池
        this.semaphore = new Semaphore(100); // 限制最大并发连接数为100
    }

    public SseEmitterReference createEmitter(String clientId, SseEmitterTask task) {
        // 获取信号量许可
        try {
            semaphore.acquire();
            SseEmitterReference reference = new SseEmitterReference(clientId, timeoutListener, completionListener, errorListener);
            reference.create();
            emitters.put(clientId, reference); // 将emitter存储在map中
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    task.apply(reference.getEmitter());
                }
            }); // 使用线程池提交任务
            return reference;
        } catch (InterruptedException e) {
            throw new RuntimeException("Server is too busy, please try later.");
        }
    }

    public void sendMessageToEmitter(String clientId, String message) {
        SseEmitterReference emitterReference = emitters.get(clientId);
        if (emitterReference != null) {
            try {
                emitterReference.getEmitter().send(SseEmitter.event().data(message));
            } catch (Exception e) {
                emitters.remove(clientId); // 移除失效的emitter
            }
        }
    }

}