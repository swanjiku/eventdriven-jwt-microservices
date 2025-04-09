

# 🚀 API Gateway - Centralized Gateway for Microservices

## 📌 Overview

The API Gateway serves as the entry point for all microservices. It routes requests, handles authentication, and enforces security using Spring Cloud Gateway.

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3+**
- **Spring Cloud Gateway**
- **Spring Security**
- **JWT Authentication**
- **Eureka Client (Service Discovery)**
- **Resilience4j (Circuit Breaker & Retry)**
- **Redis (for reactive WebSocket support)**
- **Micrometer + Prometheus (Monitoring)**
- **Swagger UI (Aggregated API Docs)**
- 
## ⚙️ Key Features

✅ Routes and Load Balances Requests to Microservices<br>
✅ JWT Authentication and Role-Based Access Control (RBAC)<br>
✅ Service Discovery via Eureka<br>
✅ Circuit Breaker + Retry Logic for Fault Tolerance<br>
✅ Centralized OpenAPI Documentation (via Swagger UI)<br> 
✅ Prometheus Metrics Export (via Actuator)<br>

## 🚀 Running the API Gateway

### 🛠️ Prerequisites

- Java 17+
- Maven
- Redis (running on default port 6379)
- Eureka Server running at `http://localhost:8761`
- Microservices (`auth-service`, `user-service`, `notification-service`) running and registered with Eureka

### 💻 Steps to Run

#### 1️⃣ Navigate to the project directory:

```bash
cd api-gateway
mvn clean install
mvn spring-boot:run
```

## 🔌 Route Configuration (`application.yml`)

The gateway uses path-based routing, with fallback and retry filters for resilience.

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://AUTH-SERVICE
          predicates:
            - Path=/api/auth/**
          filters:
            - name: CircuitBreaker
              args:
                name: authServiceCircuitBreaker
                fallbackUri: forward:/fallback/auth-service
            - name: Retry
              args:
                retries: 5
                statuses:
                  - INTERNAL_SERVER_ERROR
                  - SERVICE_UNAVAILABLE

        - id: user-service
          uri: lb://USER-SERVICE
          predicates:
            - Path=/api/users/**
          filters:
            - name: CircuitBreaker
              args:
                name: userServiceCircuitBreaker
                fallbackUri: forward:/fallback/user-service
            - name: Retry
              args:
                retries: 3

        - id: notification-service
          uri: lb://NOTIFICATION-SERVICE
          predicates:
            - Path=/api/notifications/**
          filters:
            - name: CircuitBreaker
              args:
                name: notificationServiceCircuitBreaker
                fallbackUri: forward:/fallback/notification-service
```

## 🔐 Security: JWT Authentication & Role-Based Headers

The `JwtAuthenticationFilter` performs:

- Signature validation using a shared secret key
- Expiry check
- Extracts user ID and roles
- Forwards roles as `"X-User-Roles"` and user ID as `"X-User-Id"`

**Public paths that skip JWT validation:**

```bash
/api/auth/login  
/api/auth/register  
/swagger-ui.html  
/swagger-ui/**  
/v3/api-docs  
/actuator  
/fallback/**
```

## 📊 Monitoring: Prometheus Integration

Exposes metrics for scraping by Prometheus at:

```bash
GET http://localhost:8082/actuator/prometheus
```

Enable via:

```yaml
management:
endpoints:
web:
exposure:
include: health, metrics, prometheus
```

## 📘 Swagger API Documentation

The API Gateway exposes a centralized Swagger UI for all services.

📍 Visit: http://localhost:8082/swagger-ui.html

### 🔗 Integrated Service Docs

|Service|Docs URL|
|-|-|
|User Service|`http://localhost:8083/v3/api-docs`|
|Auth Service|`http://localhost:8081/v3/api-docs`|
|Notification Service|`http://localhost:8084/v3/api-docs`|

## 🚨 Fallback Endpoints
These are called when a service fails or times out:

|Endpoint|Response|
|-|-|
|`/fallback/auth-service`|Auth Service is currently unavailable
|`/fallback/user-service`|User Service is unavailable
|`/fallback/notification-service`|Notification Service is unavailable

## 🛠️ Troubleshooting
### ❌ Gateway returns 401 Unauthorized?
- Ensure Authorization header is present:

```makefile
Authorization: Bearer <JWT>
```

- Token must be valid and unexpired
- Roles must be properly embedded in the JWT

### ❌ Service calls fail?
- Check Eureka registration at: `http://localhost:8761`
- Verify services are running and registered
- Review fallback messages in logs

## 📬 Contact
For issues, contributions, or improvements, reach out to the maintainers or raise a pull request 🚀