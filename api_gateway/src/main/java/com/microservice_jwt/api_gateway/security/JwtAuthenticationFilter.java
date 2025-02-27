package com.microservice_jwt.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        System.out.println("Incoming request: " + path);

        // Allow public access to /api/auth/register and /api/auth/login
        if (path.startsWith("/api/auth/register") || path.startsWith("/api/auth/login")) {
            System.out.println("Skipping JWT filter for: " + path);
            return chain.filter(exchange); // Skip JWT validation
        }

        List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);

        if (authHeaders == null || authHeaders.isEmpty()) {
            System.out.println("No Authorization header found. Rejecting request.");
            return unauthorizedResponse(exchange);
        }

        String authHeader = authHeaders.get(0);
        if (!authHeader.startsWith("Bearer ")) {
            System.out.println("Invalid Authorization header format: " + authHeader);
            return unauthorizedResponse(exchange);
        }

        String token = authHeader.substring(7);
        System.out.println("Received JWT: " + token);
        Claims claims = validateToken(token);

        if (claims == null) {
            System.out.println("Invalid JWT token.");
            return unauthorizedResponse(exchange);
        }

        String userId = claims.getSubject();
//        String roles = String.join(",", claims.get("roles", List.class));
        List<String> rolesList = claims.get("roles", List.class);
        List<String> prefixedRoles = rolesList.stream()
                .map(role -> "ROLE_" + role.toUpperCase()) // Prefix "ROLE_"
                .toList();

        String roles = String.join(",", prefixedRoles);


        ServerHttpRequest modifiedRequest = request.mutate()
                .header(HttpHeaders.AUTHORIZATION, authHeader) // Forward the Authorization header
                .header("X-User-Id", userId)
                .header("X-User-Roles", roles)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.WWW_AUTHENTICATE, "Bearer realm=\"Access to API\"");
        return response.setComplete();
    }

    private Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for debugging
            return null;
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public int getOrder() {
        return -1; // High priority to execute before routing
    }
}
