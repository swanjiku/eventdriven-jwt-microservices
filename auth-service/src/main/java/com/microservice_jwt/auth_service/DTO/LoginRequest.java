package com.microservice_jwt.auth_service.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String usernameOrEmail;
    private String password;
}
