package com.microservice_jwt.user_service.controller;

import com.microservice_jwt.user_service.model.User;
import com.microservice_jwt.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves the authenticated user's profile.
     */
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName(); // Extracts user ID from token

        Optional<User> user = userService.getUserById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all users (Only accessible by ADMIN).
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Retrieves a user by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates a user by ID.
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Ensure only authenticated users can update
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    /**
     * Deletes a user (ADMIN only).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
