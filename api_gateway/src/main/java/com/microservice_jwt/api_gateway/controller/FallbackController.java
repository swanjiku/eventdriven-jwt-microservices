package com.microservice_jwt.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @PostMapping("/auth-service")
    public ResponseEntity<String> authServiceFallback() {
        return ResponseEntity.status(503).body("Auth Service is currently unavailable. Try again later.");
    }

    @GetMapping("/user-service")
    public ResponseEntity<String> userServiceFallback() {
        return ResponseEntity.status(503).body("User Service is unavailable. Try again later.");
    }

    @GetMapping("/notification-service")
    public ResponseEntity<String> notificationServiceFallback() {
        return ResponseEntity.status(503).body("Notification Service is unavailable. Try again later.");
    }
}

