package com.microservice_jwt.user_service.service;

import com.microservice_jwt.user_service.model.Role;
import com.microservice_jwt.user_service.model.User;
import com.microservice_jwt.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;  // ✅ Use Redis Streams

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(String id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {

            if (updatedUser.getUsername() != null) {
                existingUser.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                existingUser.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            if (updatedUser.getRoles() != null) {
                if (currentUserHasAdminRole()) {
                    existingUser.setRoles(updatedUser.getRoles());
                } else {
                    throw new RuntimeException("You are not authorized to change roles.");
                }
            }

            // ✅ Publish event to Redis Stream
            sendNotificationToRedis(existingUser.getUsername(), "User updated successfully!", existingUser.getId());

            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User partialUpdateUser(String id, Map<String, Object> updates) {
        return userRepository.findById(id).map(existingUser -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "username":
                        existingUser.setUsername((String) value);
                        break;
                    case "email":
                        existingUser.setEmail((String) value);
                        break;
                    case "password":
                        existingUser.setPassword(passwordEncoder.encode((String) value));
                        break;
                    case "roles":
                        if (currentUserHasAdminRole()) {
                            existingUser.setRoles((Set<Role>) value);
                        } else {
                            throw new RuntimeException("You are not authorized to change roles.");
                        }
                        break;
                }
            });

            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void updatePassword(String id, String newPassword) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // ✅ Publish event to Redis Stream
            sendNotificationToRedis(user.getUsername(), "User password updated successfully!", user.getId());

        }, () -> {
            throw new RuntimeException("User not found");
        });
    }

    private void sendNotificationToRedis(String username, String message, String recipientId) {
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("message", message);
        notificationData.put("recipientId", recipientId);
        notificationData.put("isGlobal", "false"); // Individual notifications

        ObjectRecord<String, Map<String, String>> record = ObjectRecord.create("notifications_stream", notificationData);

        redisTemplate.opsForStream().add(record);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    private boolean currentUserHasAdminRole() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return roles.contains("ROLE_ADMIN");
    }
}
