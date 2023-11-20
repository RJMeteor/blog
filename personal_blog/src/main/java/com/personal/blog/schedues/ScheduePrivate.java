package com.personal.blog.schedues;

import com.personal.blog.utils.EventMetadata;
import com.personal.blog.utils.SseEmitterUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
public class ScheduePrivate {
    @Scheduled(cron = "0/20 * * * * ?")
    public void schedue() throws IOException {
        SseEmitterUtil sseEmitterUtil = SseEmitterUtil.sses.get("1");
        sseEmitterUtil.send(EventMetadata.Privateletter,"nihoa");
    }
}
