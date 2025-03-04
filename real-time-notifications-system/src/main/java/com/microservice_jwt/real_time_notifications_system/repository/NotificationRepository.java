package com.microservice_jwt.real_time_notifications_system.repository;

import com.microservice_jwt.real_time_notifications_system.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
