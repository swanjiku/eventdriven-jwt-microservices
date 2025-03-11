package com.microservice_jwt.notification_service.redis;

import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String STREAM_NAME = "notifications_stream";

    public RedisPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void publish(String message, String recipientId, boolean isGlobal) {
        Map<String, Object> notificationData = Map.of(
                "message", message,
                "recipientId", isGlobal ? "ALL_USERS" : recipientId,
                "isGlobal", isGlobal
        );

        ObjectRecord<String, Map<String, Object>> record = ObjectRecord.create(STREAM_NAME, notificationData);
        redisTemplate.opsForStream().add(record);
    }
}
