package com.microservice_jwt.notification_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSocketSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for WebSockets
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws/**").permitAll()  // Allow WebSockets without authentication
                        .requestMatchers("/notifications/send").permitAll()  // Allow sending notifications
                        .anyRequest().authenticated())  // Protect all other routes
                .httpBasic(httpBasic -> httpBasic.disable());  // Disable Basic Auth (Optional)

        return http.build();
    }
}
