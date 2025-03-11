package com.microservice_jwt.notification_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;
    private String message;
    private String recipientId; // Stores recipient ID (null for global messages)
    private boolean isGlobal;
    private Instant timestamp;
}
