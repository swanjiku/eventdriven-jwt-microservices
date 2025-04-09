
# 🔐 Auth Service - JWT Authentication & Authorization

## 📌 Overview

The Auth Service handles **user authentication, authorization, and JWT token generation** in the microservices system. It ensures secure access to downstream services using **Spring Security and JWT tokens**, supports **RBAC**, and emits events via **Redis**.

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3+**
- **Spring Security**
- **Spring Data MongoDB**
- **Spring Cloud Eureka Client**
- **Spring Cloud Gateway Integration**
- **Redis** (For event publishing)
- **JWT (JSON Web Token)**

## ⚙️ Features

- ✔️ **User Registration & Login**
- ✔️ **JWT Token Generation**
- ✔️ **Role-Based Access Control (RBAC)**
- ✔️ **Password Hashing with BCrypt**
- ✔️ **Redis Stream-based Event Publishing**
- ✔️ **Service Discovery via Eureka**
- ✔️ **Unit & Integration Tests for Auth Workflows**

## 🚀 Running the Auth Service

### ⚙️ Prerequisites

Ensure the following are installed and running:

- Java 17+
- Maven
- MongoDB
- Redis
- Eureka Server

### 💻 Steps to Run
```bash
# 1️⃣ Navigate to the project directory:
cd auth-service

# 2️⃣ Build the project:
mvn clean install

# 3️⃣ Run the service:
mvn spring-boot:run
```

## 🔐 JWT Authentication

### 📥 Login Response

On successful login, a JWT token is returned. Use it as follows:
```http
Authorization: Bearer <your-token>
```
### 🔍 Token Validation
- Validated in custom Spring Security filters.
- Signature is verified using a Base64-encoded secret key.
- Tokens expire after 1 hour (configurable).

##  🧪 Testing

### ✅ AuthService Unit Tests

Located in `AuthServiceTest.java`, these tests verify:

- ✅ **Successful registration** of a new user
- ✅ **Exception** when registering an existing user
- ✅ **Valid login** returns a JWT token
- ✅ **Invalid login** due to user not found or wrong password
- ✅ Redis stream ops are mocked to avoid external dependencies

### ✅ AuthController Integration Tests

Located in `AuthControllerTest.java`, these tests verify:

- ✅ `/api/auth/register` works with CSRF and proper body
- ✅ `/api/auth/login` returns a JWT on valid credentials
- ✅ All requests are authenticated using `@WithMockUser`

### 📡 Redis Event Publishing

Upon user registration, a Redis stream event is sent to notify downstream services (e.g., notification-service):

```java
sendNotificationToRedis(username, "User registered successfully!", user.getId());
```

## 🔧 Configuration (`application.yml`)
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

## 🛠️ Common Issues & Fixes
### ❌ Invalid Token Error?
- ✅ Ensure token is in the Authorization header.
- ✅ Decode and verify token expiry & signature.

### ❌ User Already Exists?
- ✅ Username/email must be unique in MongoDB.

### ❌ CSRF Token Missing in Tests?
- ✅ Use `.with(csrf())` in Spring MockMvc tests.