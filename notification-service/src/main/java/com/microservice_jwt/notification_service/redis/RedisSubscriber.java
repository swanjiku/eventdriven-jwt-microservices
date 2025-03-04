package com.microservice_jwt.notification_service.redis;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@Component
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    public RedisSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String notification = new String(message.getBody());

        // 🔴 Debugging Logs
        System.out.println("🔔 RedisSubscriber received: " + notification);
        System.out.println("🚀 Sending to WebSocket clients on /topic/notifications");

        // 🔴 Forward notification to WebSocket clients
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        System.out.println("🚀 Sent to WebSocket: " + notification);
    }
}

