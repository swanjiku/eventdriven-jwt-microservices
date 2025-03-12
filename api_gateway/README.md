

# 🚀 API Gateway - Centralized Gateway for Microservices

## 📌 Overview

The API Gateway serves as the entry point for all microservices. It routes requests, handles authentication, and enforces security using Spring Cloud Gateway.

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Cloud Gateway**
- **Spring Security**
- **JWT Authentication**
- **Eureka Client** (Service Discovery)

## ⚙️ Features

✅ Routes and Load Balances Requests to Microservices<br>
✅ JWT Authentication and Authorization<br>
✅ Service Discovery via Eureka<br>
✅ Role-Based Access Control (RBAC)<br>

## 🚀 Running the API Gateway
### 🛠️ Prerequisites
Ensure you have installed:

Java 17+
Maven
Eureka Server (Running)
Auth Service, User Service, Notification Service (Running)
### 💻 Steps to Run
#### 1️⃣ Navigate to the project directory:

```bash
cd api-gateway
```
#### 2️⃣ Build the project:

```bash
mvn clean install
```
#### 3️⃣ Run the service:

```bash
mvn spring-boot:run
```
## 🔌 Route Configuration (`application.yml`)
The API Gateway routes requests to different services based on their paths:

```yaml
server:
  port: 8082

spring:
  cloud:
    gateway:
      routes:
        - id: auth-service
          uri: lb://AUTH-SERVICE
          predicates:
            - Path=/api/auth/**

        - id: user-service
          uri: lb://USER-SERVICE
          predicates:
            - Path=/api/users/**

        - id: notification-service
          uri: lb://NOTIFICATION-SERVICE
          predicates:
            - Path=/api/notifications/**

```
## 🔐 Security & JWT Authentication
- **JWT tokens are validated before requests reach other services.**
- Users must include a **JWT token** in the `Authorization` header:
```makefile
Authorization: Bearer <your-token>
```
- Unauthorized requests will be blocked.

## 🛠️ Common Issues & Fixes
### ❌ Microservices not reachable?
✔️ Ensure **Eureka Server** is running.<br>
✔️ Check if services are registered in Eureka (`http://localhost:8761`).

### ❌ JWT Token Not Working?
✔️ Verify that JWT tokens are correctly generated in `auth-service`.<br>
✔️ Ensure tokens are passed in the `Authorization` header.