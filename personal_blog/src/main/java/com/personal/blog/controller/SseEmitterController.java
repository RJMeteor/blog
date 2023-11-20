package com.personal.blog.controller;

import com.personal.blog.utils.SseEmitterUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/sse")
public class SseEmitterController {

    @GetMapping(value = "connection/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public SseEmitter connection(@PathVariable("id") Long id){
        if (SseEmitterUtil.sses.containsKey(id)) {
            SseEmitterUtil.sses.remove(id);
        }
        SseEmitterUtil sseEmitterUtil = new SseEmitterUtil(0L);
        SseEmitterUtil.sses.put(id,sseEmitterUtil);
        System.out.println();
        System.out.print("size"+SseEmitterUtil.sses.keySet().size());
        for (Object o : SseEmitterUtil.sses.keySet().toArray()) {
            System.out.print("=="+o);
        }
        return sseEmitterUtil;
    }
}

