package com.microservice_jwt.user_service.service;

import com.microservice_jwt.user_service.model.User;
import com.microservice_jwt.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ✅ Get all users (Admin only)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Get user by ID
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    // ✅ Get user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // ✅ Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ✅ Update user details (excluding roles)
    public User updateUser(String id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {

            // Only update fields if provided in the request
            if (updatedUser.getUsername() != null) {
                existingUser.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            // ✅ Update roles only if the current user is an admin
            if (updatedUser.getRoles() != null) {
                if (currentUserHasAdminRole()) {
                    existingUser.setRoles(updatedUser.getRoles());
                } else {
                    throw new RuntimeException("You are not authorized to change roles.");
                }
            }

            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Patch user (Partial update)
    public User partialUpdateUser(String id, Map<String, Object> updates) {
        return userRepository.findById(id).map(user -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "username" -> user.setUsername((String) value);
                    case "email" -> user.setEmail((String) value);
                }
            });
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ✅ Update password (separate method)
    public void updatePassword(String id, String newPassword) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }, () -> {
            throw new RuntimeException("User not found");
        });
    }

    // ✅ Delete user (Admin only)
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    // 🚨 Check if current user is an ADMIN
    private boolean currentUserHasAdminRole() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return roles.contains("ROLE_ADMIN");
    }
}
