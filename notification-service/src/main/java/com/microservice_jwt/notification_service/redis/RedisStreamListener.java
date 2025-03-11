package com.microservice_jwt.notification_service.redis;

import com.microservice_jwt.notification_service.model.Notification;
import com.microservice_jwt.notification_service.repository.NotificationRepository;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
public class RedisStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    public RedisStreamListener(SimpMessagingTemplate messagingTemplate, NotificationRepository notificationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void onMessage(MapRecord<String, String, String> record) {
        Map<String, String> messageMap = record.getValue();
        String message = messageMap.get("message");
        String recipientId = messageMap.get("recipientId");
        boolean isGlobal = Boolean.parseBoolean(messageMap.getOrDefault("isGlobal", "false"));

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRecipientId(recipientId);
        notification.setGlobal(isGlobal);
        notification.setTimestamp(Instant.now()); // ✅ Set timestamp

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
