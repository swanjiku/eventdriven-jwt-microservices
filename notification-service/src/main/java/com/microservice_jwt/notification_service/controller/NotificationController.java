package com.microservice_jwt.notification_service.controller;

import com.microservice_jwt.notification_service.model.Notification;
import com.microservice_jwt.notification_service.redis.RedisPublisher;
import com.microservice_jwt.notification_service.repository.NotificationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final RedisPublisher redisPublisher;
    private final NotificationRepository notificationRepository;

    public NotificationController(RedisPublisher redisPublisher, NotificationRepository notificationRepository) {
        this.redisPublisher = redisPublisher;
        this.notificationRepository = notificationRepository;
    }

    @PostMapping("/send")
    public String sendNotification(@RequestParam String message) {
        redisPublisher.publish("notifications", message);
        return "Notification sent!";
    }

    // ✅ Test endpoint to send a default notification
    @GetMapping("/test")
    public String sendTestNotification() {
        String testMessage = "🚀 Test notification from backend!";
        redisPublisher.publish("notifications", testMessage);
        return "Test notification sent!";
    }

    @GetMapping("/history")
    public List<Notification> getNotificationHistory() {
        return notificationRepository.findAll();
    }
}
