package com.microservice_jwt.user_service.repository;

import com.microservice_jwt.user_service.config.MongoDbContainerInitializer;
import com.microservice_jwt.user_service.model.Role;
import com.microservice_jwt.user_service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ContextConfiguration(initializers = MongoDbContainerInitializer.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // Clean slate before each test

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setRoles(Set.of(Role.USER));

        userRepository.save(testUser);
    }

    @Test
    void findByUsername_shouldReturnUser_whenUsernameExists() {
        Optional<User> result = userRepository.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void findByEmail_shouldReturnUser_whenEmailExists() {
        Optional<User> result = userRepository.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUsernameNotExists() {
        Optional<User> result = userRepository.findByUsername("nonexistent");

        assertFalse(result.isPresent());
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenEmailNotExists() {
        Optional<User> result = userRepository.findByEmail("nope@example.com");

        assertFalse(result.isPresent());
    }
}
