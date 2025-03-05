package com.microservice_jwt.notification_service.redis;

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

    public RedisSubscriber(SimpMessagingTemplate messagingTemplate, NotificationRepository notificationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String notificationText  = new String(message.getBody());

        // 🔹 Save to MongoDB
        Notification notification = new Notification(notificationText);
        notificationRepository.save(notification);

        // 🔴 Debugging Logs
        System.out.println("🔔 RedisSubscriber received: " + notificationText );
        System.out.println("🚀 Sending to WebSocket clients on /topic/notifications");

        // 🔴 Forward/Send notification to WebSocket clients
        messagingTemplate.convertAndSend("/topic/notifications", notificationText );
        System.out.println("🚀 Sent to WebSocket: " + notificationText );
    }
}

