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
    public String sendNotification(@RequestParam String message,
                                   @RequestParam(required = false) String recipientId) {
        boolean isGlobal = (recipientId == null || recipientId.isBlank());
        redisPublisher.publish(message, recipientId, isGlobal);
        return "Notification sent!";
    }

    @GetMapping("/all")
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotifications(@PathVariable String userId) {
        return notificationRepository.findByRecipientIdOrIsGlobal(userId, true);
    }

    @PostMapping("/test")
    public String testNotification() {
        return "✅ Test notification endpoint working!";
    }
}
