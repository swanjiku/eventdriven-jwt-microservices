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
- ✅ User Profile Retrieval and Updates
- ✅ Role-Based Access Control (RBAC)
- ✅ Password Management (Patch Password)
- ✅ Secure REST API with JWT
- ✅ Redis Stream Event Publishing (for Notification Service)
- ✅ Service Discovery via Eureka
- ✅ CORS Support for API Gateway
- ✅ Integrated Swagger UI (Bearer Token Auth)

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
```bash
# 1️⃣ Navigate to the project directory:
cd user-service

# 2️⃣ Build the project:
mvn clean install

# 3️⃣ Run the service:
mvn spring-boot:run
```

---

## 🔗 API Examples
### Register a New User
```bash
curl -X POST -H "Content-Type: application/json" -d '{"username":"bob","password":"password","email":"bob@email.com"}' http://localhost:8083/api/users/register
```

### Update Profile
```bash
curl -X PATCH -H "Authorization: Bearer <jwt-token>" -H "Content-Type: application/json" \
  -d '{"firstName": "Bob", "lastName": "Smith"}' \
  http://localhost:8083/api/users/me
```

### Change Password
```bash
curl -X PATCH -H "Authorization: Bearer <jwt-token>" -H "Content-Type: application/json" \
  -d '{"oldPassword": "password", "newPassword": "newpass123"}' \
  http://localhost:8083/api/users/password
```

### Get User Profile
```bash
curl -H "Authorization: Bearer <jwt-token>" http://localhost:8083/api/users/me
```

---

## ⚙️ Configuration
- **MongoDB URI:** `spring.data.mongodb.uri` (default: `mongodb://localhost:27017/users`)
- **Redis URI:** `spring.redis.host`/`spring.redis.port` (default: `localhost:6379`)
- **JWT Secret:** `jwt.secret` in `application.yml`

---

## 🛠️ Troubleshooting
- **JWT Errors:** Ensure token is valid and present in the `Authorization` header.
- **DB Connectivity Issues:** Confirm MongoDB and Redis are running and URIs are correct.
- **Service Discovery:** Make sure Eureka Server is running and reachable.
- **CORS Issues:** Confirm API Gateway is forwarding correct headers and CORS is enabled.

---

## 🧪 Running Tests
```bash
mvn test
```

---

## 🔄 Extending the Service
- Add new roles by updating the roles enum and security config.
- To add new profile fields, update the user entity and validation logic.
- To publish new event types, use Redis Streams in the relevant business logic.

---

## 📚 References
- [Spring WebFlux Docs](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
- [Spring Security Docs](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/)

---

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
