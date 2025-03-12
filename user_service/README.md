
# 👤 User Service - User Management Microservice

## 📌 Overview

The **User Service** handles user-related operations such as registration, profile management, and role-based access control.

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Security**
- **Spring Data MongoDB**
- **Redis** (for event publishing)
- **Eureka Client** (Service Discovery)

## ⚙️ Features
✅ User Registration & Authentication<br>
✅ Retrieve and Update User Profiles<br>
✅ Role-Based Access Control (RBAC)<br>
✅ Service Discovery via Eureka<br>
✅ Publishes Events to Redis for Notifications<br>

## 🚀 Running the User Service
### 🛠️ Prerequisites
Ensure you have installed:

- Java 17+
- Maven
- MongoDB (Database)
- Redis (for event publishing)
- Eureka Server (Running)
### 💻 Steps to Run
#### 1️⃣ Navigate to the project directory:

```bash
cd user-service
```
#### 2️⃣ Build the project:

```bash
mvn clean install
```
#### 3️⃣ Run the service:

```bash
mvn spring-boot:run
```

## 🔐 Security & Role-Based Access Control
- Uses **Spring Security** to protect endpoints.
- Users are assigned **roles** (`ROLE_USER`, `ROLE_ADMIN`).
- Access to APIs is controlled using **method-level** security.
- Security Configuration (`SecurityConfig.java`)


## 🛠️ Redis Event Publishing (Notifications)
Whenever a user updates their profile, **an event is published to Redis.**

### Publishing Events in `UserService.java`
```java

sendNotificationToRedis(existingUser.getUsername(), "User updated successfully!", existingUser.getId());
```
## 🛠️ Common Issues & Fixes
### ❌ Users cannot register?
✔️ Ensure the database is running and accessible.<br>
✔️ Check if the username or email already exists.

### ❌ JWT Token Not Working?
✔️ Ensure **auth-service** is running.<br>
✔️ Verify that **JWT** tokens are included in the request headers.

