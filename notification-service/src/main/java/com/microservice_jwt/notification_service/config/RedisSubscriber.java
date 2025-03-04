//package com.microservice_jwt.notification_service.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.microservice_jwt.notification_service.model.Notification;
//import org.springframework.data.redis.connection.Message;
//import org.springframework.data.redis.connection.MessageListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RedisSubscriber implements MessageListener {
//
//    private final NotificationWebSocketHandler webSocketHandler;
//    private final ObjectMapper objectMapper;
//
//    public RedisSubscriber(NotificationWebSocketHandler webSocketHandler, ObjectMapper objectMapper) {
//        this.webSocketHandler = webSocketHandler;
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        try {
//            Notification notification = objectMapper.readValue(message.getBody(), Notification.class);
//            webSocketHandler.sendNotification(notification);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//
