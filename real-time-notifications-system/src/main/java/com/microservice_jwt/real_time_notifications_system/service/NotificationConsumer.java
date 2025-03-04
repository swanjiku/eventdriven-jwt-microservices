package com.microservice_jwt.real_time_notifications_system.service;

import com.microservice_jwt.real_time_notifications_system.model.Notification;
import com.microservice_jwt.real_time_notifications_system.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;

    public NotificationConsumer(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @RabbitListener(queues = "notification.queue")
    public void receiveNotification(Notification notification) {
        notificationRepository.save(notification);
        System.out.println("Received Notification: " + notification.getMessage());
    }
}
