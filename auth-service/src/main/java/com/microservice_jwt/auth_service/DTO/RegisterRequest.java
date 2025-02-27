package com.microservice_jwt.auth_service.DTO;

import com.microservice_jwt.auth_service.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;
}
