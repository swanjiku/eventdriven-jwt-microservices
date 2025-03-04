package com.microservice_jwt.real_time_notifications_system.service;

import com.microservice_jwt.real_time_notifications_system.model.Notification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(Notification notification) {
        rabbitTemplate.convertAndSend("notification.queue", notification);
    }
}