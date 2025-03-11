package com.microservice_jwt.notification_service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice_jwt.notification_service.model.Notification;
import com.microservice_jwt.notification_service.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

@Component
public class RedisSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // ✅ JSON Parser

    public RedisSubscriber(SimpMessagingTemplate messagingTemplate, NotificationRepository notificationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String notificationText = new String(message.getBody());
            Notification notification = objectMapper.readValue(notificationText, Notification.class); // ✅ Parse JSON

            // 🔹 Save to MongoDB
            notificationRepository.save(notification);

            // 🔴 Debugging Logs
            System.out.println("🔔 RedisSubscriber received: " + notificationText);
            System.out.println("🚀 Sending to WebSocket clients on /topic/notifications");

            // 🔴 Forward/Send notification to WebSocket clients
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            System.out.println("🚀 Sent to WebSocket: " + notification);
        } catch (Exception e) {
            System.err.println("❌ Error parsing notification: " + e.getMessage());
        }
    }
}
