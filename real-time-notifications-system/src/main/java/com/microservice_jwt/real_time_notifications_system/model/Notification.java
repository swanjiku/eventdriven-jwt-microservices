package com.microservice_jwt.real_time_notifications_system.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;
    private String message;
    private String recipient;
    private boolean read;
    private LocalDateTime timestamp = LocalDateTime.now();
}
