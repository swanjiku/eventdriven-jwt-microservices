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
- **Testcontainers** for MongoDB integration testing

---

## ⚙️ Features
- ✔️ **User Registration & Login**
- ✔️ **JWT Token Generation**
- ✔️ **Role-Based Access Control (RBAC)**
- ✔️ **Password Hashing with BCrypt**
- ✔️ **Redis Stream-based Event Publishing**
- ✔️ **Service Discovery via Eureka**
- ✔️ **Unit & Integration Tests for Auth Workflows**

---

## 🚀 Running the Auth Service

### ⚙️ Prerequisites
Ensure the following are installed and running:
- Java 17+
- Maven
- MongoDB (Local or Docker)
- Redis (Local or Docker)
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

---

## 🔐 JWT Authentication & RBAC
- All endpoints (except `/api/auth/login` and `/api/auth/register`) require a valid JWT in the `Authorization: Bearer <token>` header.
- JWT tokens include user ID and roles; RBAC is enforced via Spring Security annotations.

### Example: Login & Token Usage
```bash
# Register a new user
curl -X POST -H "Content-Type: application/json" -d '{"username":"alice","password":"password"}' http://localhost:8081/api/auth/register

# Login to get JWT
curl -X POST -H "Content-Type: application/json" -d '{"username":"alice","password":"password"}' http://localhost:8081/api/auth/login

# Use JWT to access a protected endpoint
curl -H "Authorization: Bearer <your-jwt-token>" http://localhost:8081/api/auth/me
```

---

## ⚙️ Configuration
- **MongoDB URI:** Set via `spring.data.mongodb.uri` (default: `mongodb://localhost:27017/authdb`)
- **Redis URI:** Set via `spring.redis.host`/`spring.redis.port` (default: `localhost:6379`)
- **JWT Secret:** Set via `jwt.secret` in `application.yml`

---

## 🧪 Running Tests
```bash
mvn test
```
- Uses Testcontainers for MongoDB integration tests.

---

## 🔄 Extending the Service
- Add new roles by updating the `Role` enum and security configuration.
- To support OAuth providers, integrate with Spring Security OAuth2 modules.
- To publish additional events, use Redis Streams in the relevant service logic.

---

## 🛠️ Common Issues & Fixes
- **Invalid Token Error?**
  - Ensure the token is included in the `Authorization` header as `Bearer <your-token>`.
  - Decode and verify token expiry & signature.
- **DB Connection Issues?**
  - Check MongoDB/Redis are running and URIs are configured correctly.
- **Service Discovery Fails?**
  - Ensure Eureka Server is running and reachable.

---

## 📚 References
- [Spring Security Docs](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- [JWT Introduction](https://jwt.io/introduction/)
- [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/)

---