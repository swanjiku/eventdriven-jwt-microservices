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

## ⚙️ Key Features
- ✅ Routes and Load Balances Requests to Microservices
- ✅ JWT Authentication and Role-Based Access Control (RBAC)
- ✅ Service Discovery via Eureka
- ✅ Circuit Breaker + Retry Logic for Fault Tolerance
- ✅ Centralized OpenAPI Documentation (via Swagger UI)
- ✅ Prometheus Metrics Export (via Actuator)

## 🚀 Running the API Gateway

### 🛠️ Prerequisites
- Java 17+
- Maven
- Redis (running on default port 6379)
- Eureka Server running at `http://localhost:8761`
- Microservices (`auth-service`, `user-service`, `notification-service`) running and registered with Eureka

### 💻 Steps to Run
```bash
cd api-gateway
mvn clean install
mvn spring-boot:run
```

### 🔗 Accessing Services
- **Swagger UI (Aggregated Docs):** `http://localhost:8082/swagger-ui.html`
- **Prometheus Metrics:** `http://localhost:8082/actuator/prometheus`

## 🔌 Route Configuration (`application.yml`)
The gateway uses path-based routing, with fallback and retry filters for resilience. Example:
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
                name: authServiceCB
                fallbackUri: forward:/fallback/auth
            - Retry=3
        # Add more routes as needed
```

## 🔒 JWT Authentication
- All protected routes require a valid JWT in the `Authorization: Bearer <token>` header.
- The gateway validates JWTs and enforces RBAC based on roles embedded in the token.

### Example Request
```bash
curl -H "Authorization: Bearer <your-jwt-token>" http://localhost:8082/api/user/profile
```

## ➕ Adding New Routes
1. Edit `application.yml` and add a new entry under `spring.cloud.gateway.routes`.
2. Specify the service ID, path predicate, and any filters (e.g., circuit breaker, retry).
3. Restart the gateway for changes to take effect.

## 🛠️ Troubleshooting
- **Service Not Found:** Ensure services are registered with Eureka and healthy.
- **JWT Errors:** Check that the `Authorization` header is present and the token is valid.
- **Redis Issues:** Ensure Redis is running on the expected host/port.
- **Prometheus/Swagger Not Loading:** Confirm actuator and Swagger dependencies are included and enabled.

## 📝 Useful Commands
- **Build only:** `mvn clean install`
- **Run tests:** `mvn test`
- **View logs:** Check `logs/` directory or console output for errors.

## 🧩 Extending the Gateway
- To add custom filters, implement `GatewayFilterFactory` and register in the configuration.
- For more advanced authentication, integrate with OAuth2 providers using Spring Security.

## 📚 References
- [Spring Cloud Gateway Docs](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
- [Spring Security Docs](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- [Resilience4j Docs](https://resilience4j.readme.io/docs)