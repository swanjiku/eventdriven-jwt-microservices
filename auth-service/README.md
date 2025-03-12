
# 🔐 Auth Service - JWT Authentication & Authorization

## 📌 Overview

The Auth Service handles **user authentication, authorization, and JWT token generation** in the microservices system. It ensures secure access to other services using **Spring Security and JWT tokens**.

## 🛠️ Tech Stack

- **Java**
- **Spring Boot**
- **Spring Security**
- **Spring Data MongoDB**
- **Eureka Client** (Service Discovery)
- **API Gateway Integration**
- **Redis** (For event publishing)


## ⚙️ Features

✅ User Registration & Login<br>
✅ JWT Token Generation<br>
✅ Role-Based Access Control (RBAC)<br>
✅ Password Hashing with BCrypt<br>
✅ Redis Event Publishing for Notifications<br>
✅ Service Discovery via Eureka<br>

## 🚀 Running the Auth Service
### 🛠️ Prerequisites
Ensure you have installed:

-Java 17+
-Maven
-MongoDB

### 💻 Steps to Run

#### 1️⃣ Navigate to the project directory:

```bash
cd auth-service
```
#### 2️⃣ Build the project:

```bash
mvn clean install
```
#### 3️⃣ Run the service:

```bash
mvn spring-boot:run
```

## 🔐 JWT Authentication
- Upon login, users receive a **JWT** token.
- This token must be sent in the `Authorization` header as:
```makefile
Authorization: Bearer <your-token>
```
- JWT tokens are validated in Spring Security filters.

## 🔧 Configuration (application.yml)
```yaml
server:
  port: 8081

spring:
  application:
    name: auth-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
```

## 📡 Redis Event Publishing
Auth events (e.g., user registration) are published to Redis:

```java
sendNotificationToRedis(username, "User registered successfully!", user.getId());
```

## 🛠️ Common Issues & Fixes
### ❌ Invalid Token Error?
✔️ Ensure the token is correctly passed in the `Authorization` header.<br>
✔️ Verify token expiration & signature using a JWT decoder.

### ❌ User Already Exists?
✔️ Check if the username/email is unique before registering.