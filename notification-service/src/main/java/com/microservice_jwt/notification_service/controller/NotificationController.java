package com.microservice_jwt.notification_service.controller;

import com.microservice_jwt.notification_service.redis.RedisPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final RedisPublisher redisPublisher;

    public NotificationController(RedisPublisher redisPublisher) {
        this.redisPublisher = redisPublisher;
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
}
