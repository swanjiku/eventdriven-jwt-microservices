package com.microservice_jwt.auth_service.service;

import com.microservice_jwt.auth_service.model.Role;
import com.microservice_jwt.auth_service.model.User;
import com.microservice_jwt.auth_service.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final SecretKey key;
    private final long expirationTime;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${jwt.secret}") String secretKey,
            RedisTemplate<String, String> redisTemplate,
            @Value("${jwt.expiration}") long expirationTime) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
        this.expirationTime = expirationTime;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }


    public String register(String username, String email, String password, Set<Role> roles) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        userRepository.save(user);

        // ✅ Publish event to Redis Stream
        sendNotificationToRedis(username, "User registered successfully!", user.getId());

        return "User registered successfully";
    }

    public String login(String usernameOrEmail, String password) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        // ✅ Publish event to Redis Stream
        sendNotificationToRedis(user.getUsername(), "User logged in!", user.getId());

        return generateToken(user);
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles().stream().map(Role::name).collect(Collectors.toList()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private void sendNotificationToRedis(String username, String message, String recipientId) {
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("message", message);
        notificationData.put("recipientId", recipientId);
        notificationData.put("isGlobal", "false"); // Individual notifications

        ObjectRecord<String, Map<String, String>> record = ObjectRecord.create("notifications_stream", notificationData);

        redisTemplate.opsForStream().add(record);
    }
}
