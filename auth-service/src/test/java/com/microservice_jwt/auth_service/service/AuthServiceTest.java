package com.microservice_jwt.auth_service.service;

import com.microservice_jwt.auth_service.model.Role;
import com.microservice_jwt.auth_service.model.User;
import com.microservice_jwt.auth_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private StreamOperations<String, Object, Object> streamOperations;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        // Allow optional stubbing to avoid UnnecessaryStubbingException
        lenient().when(redisTemplate.opsForStream()).thenReturn(streamOperations);

        // Generate a secure 256-bit Base64-encoded secret key
        String secureSecretKey = Base64.getEncoder().encodeToString(
                "this_is_a_very_secure_jwt_secret_key!".getBytes(StandardCharsets.UTF_8)
        );

        authService = new AuthService(userRepository, passwordEncoder, secureSecretKey, redisTemplate, 3600000L);
    }

    @Test
    void register_ShouldCreateUser_WhenUserDoesNotExist() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        String response = authService.register("testUser", "test@example.com", "password", Set.of(Role.USER));

        assertEquals("User registered successfully", response);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenUserAlreadyExists() {
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(new User()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                authService.register("testUser", "test@example.com", "password", Set.of(Role.USER))
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        User user = new User();
        user.setId("12345"); // Ensure ID is set
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(Role.USER)); // ✅ Set user roles to avoid NPE

        when(userRepository.findByUsernameOrEmail("testUser", "testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        String token = authService.login("testUser", "password");

        assertNotNull(token);
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsernameOrEmail("invalidUser", "invalidUser")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                authService.login("invalidUser", "password")
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsIncorrect() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsernameOrEmail("testUser", "testUser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                authService.login("testUser", "wrongPassword")
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }
}
