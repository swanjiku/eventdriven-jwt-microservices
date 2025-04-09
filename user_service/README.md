
# 👤 User Service - User Management Microservice

## 📌 Overview

The **User Service** is responsible for managing user data, roles, and permissions in a secure and scalable manner. It includes profile management, role-based access control (RBAC), and password management. It also publishes user activity events (e.g., profile updates) to **Redis Streams** for asynchronous notifications.

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot 3**
- **Spring WebFlux**
- **Spring Security**
- **Spring Data MongoDB**
- **Redis** (Streams for event-driven architecture)
- **Eureka Client** (Service Discovery)
- **Swagger / OpenAPI 3** (API documentation)

## ⚙️ Features

✅ User Profile Retrieval and Updates  
✅ Role-Based Access Control (RBAC)  
✅ Password Management (Patch Password)  
✅ Secure REST API with JWT  
✅ Redis Stream Event Publishing (for Notification Service)  
✅ Service Discovery via Eureka  
✅ CORS Support for API Gateway  
✅ Integrated Swagger UI (Bearer Token Auth)

## 🔐 Security & Role-Based Access Control

- Enforced via **Spring Security**
- JWT-based authentication (configured in API Gateway)
- Role-based access (`ROLE_USER`, `ROLE_ADMIN`) enforced using method-level security:
    - `@PreAuthorize("hasRole('ADMIN')")` for admin-only endpoints
    - `@PreAuthorize("isAuthenticated()")` for authenticated access
- Authenticated user ID is extracted from JWT for profile operations.

## 🚀 Running the User Service

### 🛠️ Prerequisites

Ensure you have the following installed and running:

- Java 17+
- Maven
- MongoDB (locally or remote)
- Redis (used for publishing user-related events)
- Eureka Server (for service discovery)
- Auth Service (for JWT token issuance)

### 💻 Steps to Run

1️⃣ Navigate to the project directory:

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

The service will register itself with Eureka and expose endpoints on its configured port.

## 🔁 Redis Stream Notifications

The service publishes user-related events to the Redis stream called `notifications_stream`. These include:

- User profile updated
- User password changed

Example from `UserService.java`:

```java
sendNotificationToRedis(user.getUsername(), "User updated successfully!", user.getId());
```

The **Notification Service** listens to this stream and sends real-time updates via WebSockets.

## 🌐 API Documentation

Swagger UI is available at:

```bash
http://localhost:<PORT>/swagger-ui.html
```

Use the Authorize button to provide your JWT token in `Bearer <token>` format.
Security config in `OpenAPIConfig.java` enables Bearer authentication across endpoints.

## 📁 Endpoints Summary
|Method|Endpoint|Description|Access Level|
|-|-|-|-|
|GET	|`/api/users/profile`	|Get current user's profile	|Authenticated
|GET	|`/api/users`	|Get all users	|Admin
|GET	|`/api/users/{id}`	|Get user by ID	|Public
|PUT	|`/api/users/{id}`	|Update full user info	|Authenticated
|PATCH	|`/api/users/{id}`	|Partial update	|Authenticated
|PATCH	|`/api/users/{id}/password`	|Change user password	|Authenticated
|DELETE	|`/api/users/{id}`	|Delete a user	|Admin

## 🛠️ Common Issues & Fixes
### ❌ Users cannot manage their profiles?
✔️ Make sure the JWT token contains the correct `userId` as the subject (`sub` claim).

### ❌ JWT Token Not Working?
✔️ Ensure the **auth-service** is running and integrated with the API Gateway.
✔️ Confirm that tokens are included in requests:

```http
Authorization: Bearer <your-token>
```

### ❌ Notifications Not Received?
✔️ Ensure **Redis** is running.
✔️ Confirm that the **Notification Service** is listening to `notifications_stream`.

## 📬 Contact & Contributions
This service is maintained as part of a microservices system. Feel free to suggest improvements or raise issues if you spot any.
