package com.microservice_jwt.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notifications")
public class Notification implements Serializable {

    @Id
    private String id;
    private String recipient; // For later
    private String message;
    private Instant timestamp;

    public Notification(String message) {
        this.message = message;
        this.timestamp = Instant.now();
    }
}
