package com.microservice_jwt.notification_service.repository;

import com.microservice_jwt.notification_service.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
