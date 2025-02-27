package com.microservice_jwt.user_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users") // MongoDB collection name
public class User {

    @Id
    private String id; // MongoDB uses String IDs

    private String username;
    private String email;
    private String password;
    private Set<Role> roles;
}
