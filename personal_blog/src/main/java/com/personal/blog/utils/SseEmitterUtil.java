package com.personal.blog.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class SseEmitterUtil extends SseEmitter {

    public static final ConcurrentHashMap<Long,SseEmitterUtil>  sses= new ConcurrentHashMap<Long,SseEmitterUtil>();


    public void send(EventMetadata eventMetadata,String sendMsg) throws IOException {
        SseEventBuilder event = SseEmitter.event();
        event.name(eventMetadata.getName());
        event.data(sendMsg);
        super.send(event);
    }

    public SseEmitterUtil() {
    }

    public SseEmitterUtil(Long timeout) {
        super(timeout);
    }

    @Override
    public Long getTimeout() {
        return super.getTimeout();
    }

    @Override
    public synchronized void complete() {
        super.complete();
    }

    @Override
    public synchronized void onTimeout(Runnable callback) {
        super.onTimeout(callback);
    }

    @Override
    public synchronized void onError(Consumer<Throwable> callback) {
        super.onError(callback);
    }

    @Override
    public synchronized void onCompletion(Runnable callback) {
        super.onCompletion(callback);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
