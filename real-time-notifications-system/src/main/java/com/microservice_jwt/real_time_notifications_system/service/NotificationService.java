package com.microservice_jwt.real_time_notifications_system.service;

import com.microservice_jwt.real_time_notifications_system.model.Notification;
import com.microservice_jwt.real_time_notifications_system.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    public NotificationService(SimpMessagingTemplate messagingTemplate, NotificationRepository notificationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    public void notifyUser(Notification notification) {
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
