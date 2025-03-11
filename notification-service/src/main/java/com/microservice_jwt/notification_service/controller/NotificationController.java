package com.microservice_jwt.notification_service.controller;

import com.microservice_jwt.notification_service.model.Notification;
import com.microservice_jwt.notification_service.redis.RedisPublisher;
import com.microservice_jwt.notification_service.repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
    @GetMapping("/user")
    public List<Notification> getUserNotifications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        return notificationRepository.findByRecipientIdOrIsGlobal(userId, true);
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
