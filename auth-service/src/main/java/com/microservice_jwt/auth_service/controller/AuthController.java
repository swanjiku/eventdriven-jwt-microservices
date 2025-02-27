package com.microservice_jwt.auth_service.controller;

import com.microservice_jwt.auth_service.DTO.LoginRequest;
import com.microservice_jwt.auth_service.DTO.RegisterRequest;
import com.microservice_jwt.auth_service.model.User;
import com.microservice_jwt.auth_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String responseMessage = authService.register(request.getUsername(), request.getEmail(), request.getPassword(), request.getRoles());
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.getUsernameOrEmail(), request.getPassword());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}
