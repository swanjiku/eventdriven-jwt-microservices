package com.microservice_jwt.notification_service.repository;

import com.microservice_jwt.notification_service.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByRecipientIdOrIsGlobal(String recipientId, boolean isGlobal);
}
