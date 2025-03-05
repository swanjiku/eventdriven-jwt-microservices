# Microservices JWT Authentication System

This is a microservices-based system implementing JWT authentication with Spring Cloud, Eureka, Redis, and MongoDB.

## 🔗 Microservices Architecture
The system consists of the following services:
- [Eureka Server](./eureka_server/README.md) - Service Discovery
- [API Gateway](./api_gateway/README.md) - Routing and Security
- [Auth Service](./auth-service/README.md) - Authentication & JWT Issuance
- [User Service](./user_service/README.md) - User Management
- [Notification Service](./notification-service/README.md) - Real-time Notifications

## 🛠️ Tech Stack
- **Backend**: Spring Boot, Spring Security, Spring Cloud Gateway, Spring Data JPA, MongoDB, Redis
- **Communication**: RESTful APIs, Redis Pub/Sub
- **Authentication**: JWT-based authentication
- **Service Discovery**: Eureka Server

## 📌 Running the Project
Start the services in the following order:

1. Start **Eureka Server**:
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```
2. Start **Auth Service**:
   ```bash
   cd auth-service
   mvn spring-boot:run
   ```
3. Start **API Gateway**:
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```
4. Start **User Service**:
   ```bash
   cd user-service
   mvn spring-boot:run
   ```
5. Start **Notification Service**:
   ```bash
   cd notification-service
   mvn spring-boot:run
   ```
## 📌 Future Enhancements
- Implement Redis Streams for better notification handling.
- Add recipient tracking in notification-service.
- Introduce rate limiting in api-gateway.
- Add centralized logging and monitoring.
