package com.microservice_jwt.user_service.service;

import com.microservice_jwt.user_service.model.User;
import com.microservice_jwt.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
            // Only update fields if they are not null in the request
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
                existingUser.setRoles(updatedUser.getRoles());
            }

            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}

