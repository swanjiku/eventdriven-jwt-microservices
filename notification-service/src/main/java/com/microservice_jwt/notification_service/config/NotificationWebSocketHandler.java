//package com.microservice_jwt.notification_service.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.microservice_jwt.notification_service.model.Notification;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class NotificationWebSocketHandler extends TextWebSocketHandler {
//
//    private final StringRedisTemplate redisTemplate;
//    private final ObjectMapper objectMapper;
//    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//
//    public NotificationWebSocketHandler(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
//        this.redisTemplate = redisTemplate;
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) {
//        sessions.put(session.getId(), session);
//        System.out.println("New WebSocket Connection: " + session.getId());
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        Notification notification = objectMapper.readValue(message.getPayload(), Notification.class);
//        redisTemplate.convertAndSend("notifications", objectMapper.writeValueAsString(notification));
//    }
//
//    public void sendNotification(Notification notification) throws Exception {
//        for (WebSocketSession session : sessions.values()) {
//            if (session.isOpen()) {
//                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(notification)));
//            }
//        }
//    }
//}
