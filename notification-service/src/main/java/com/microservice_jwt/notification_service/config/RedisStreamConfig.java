package com.microservice_jwt.notification_service.config;

import com.microservice_jwt.notification_service.redis.RedisStreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;

import java.time.Duration;

@Configuration
public class RedisStreamConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    public RedisStreamConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(
            RedisStreamListener redisStreamListener) {  // Inject RedisStreamListener here

        StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainerOptions.builder()
                        .pollTimeout(Duration.ofMillis(100))
                        .build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
                StreamMessageListenerContainer.create(redisConnectionFactory, options);

        // ✅ Register Redis Stream listener
        container.receive(StreamOffset.fromStart("notifications_stream"), redisStreamListener);
        container.start();

        System.out.println("🚀 Listening to Redis Stream: notifications_stream");

        return container;
    }
}
