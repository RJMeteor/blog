package com.personal.blog.utils;

import com.alibaba.fastjson2.JSONObject;
import com.personal.blog.cover.coverImpe.EventCover;
import com.personal.blog.pojo.BlogChattingRecords;
import com.personal.blog.pojo.BlogWebSocket;
import com.personal.blog.service.IBlogChattingRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/{id}")
@Component
public class WebSocketUtil{


    public static final ConcurrentHashMap<Long, Session> webSocketClient = new ConcurrentHashMap<Long, Session>();

    private static IBlogChattingRecordsService iBlogChattingRecordsService;
    private static EventCover eventCover;

    @Autowired
    public void setIBlogChattingRecordsService(IBlogChattingRecordsService iBlogChattingRecordsService){
        WebSocketUtil.iBlogChattingRecordsService = iBlogChattingRecordsService;
    }
    @Autowired
    public void setIBlogChattingRecordsService(EventCover eventCover){
        WebSocketUtil.eventCover = eventCover;
    }

    @OnClose
    public void close(Session session) {
        System.out.println("关闭");
        for (Map.Entry<Long, Session> longSessionEntry : webSocketClient.entrySet()) {
            if (longSessionEntry.getValue() == session) {
                webSocketClient.remove(longSessionEntry.getKey());
                break;
            }
        }
    }

    @OnMessage
    public void onMessage(String message) {
        BlogWebSocket blogWebSocket = JSONObject.parseObject(message, BlogWebSocket.class);
        BlogChattingRecords blogChattingRecords = iBlogChattingRecordsService.insertChattingRecords(blogWebSocket);
        sendSingleUserMessage(blogWebSocket, blogChattingRecords);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "id") String userId) throws Exception {
        System.out.println("连接" + userId);
        if (webSocketClient.contains(Long.valueOf(userId))) {
            webSocketClient.remove(Long.valueOf(userId));
        }
        webSocketClient.put(Long.valueOf(userId), session);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        for (Map.Entry<Long, Session> longSessionEntry : webSocketClient.entrySet()) {
            if (longSessionEntry.getValue() == session) {
                webSocketClient.remove(longSessionEntry.getKey());
                break;
            }
        }
    }

    public void sendSingleUserMessage(BlogWebSocket blogWebSocket, BlogChattingRecords blogChattingRecords) {
        Session senderSession = WebSocketUtil.webSocketClient.get(blogWebSocket.getSenderUserId());
        Session acceptorSession = WebSocketUtil.webSocketClient.get(blogWebSocket.getAcceptorUserId());

        String messageJson = JSONObject.toJSONString(blogChattingRecords);
        if (!ObjectUtils.isEmpty(senderSession)) {
            senderSession.getAsyncRemote().sendText(messageJson);
        }
        if (!ObjectUtils.isEmpty(acceptorSession)){
            acceptorSession.getAsyncRemote().sendText(messageJson);
        }

        SseEmitterUtil sseEmitterUtil = SseEmitterUtil.sses.get(blogWebSocket.getAcceptorUserId());
        if (!ObjectUtils.isEmpty(sseEmitterUtil)){
            String message = JSONObject.toJSONString(eventCover.blogChattingRecordsToEventPrivateLetter(blogChattingRecords));
            try {
                sseEmitterUtil.send(EventMetadata.Privateletter,message);
            } catch (IOException e) {
                e.printStackTrace();    
            }
        }
    }

}
