# 🌐 Microservices JWT Authentication System

This is a microservices-based system implementing JWT authentication with Spring Cloud, Eureka, Redis, and MongoDB.

## 🧩 Microservices Overview

| Service	          | Description                   |
|-------------------|-------------------------------|
| 🧭 Eureka Server	 | Service Discovery             |
| 🚪 API Gateway	   | Routing and JWT Validation    |
| 🔐 Auth Service	  | Authentication & JWT Issuance |
| 👤 User Service	  | User Profile Management       |
|🔔 Notification Service	|Real-Time Event-Based Notifications|


---

## ⚙️ Tech Stack
- **Spring Boot 3+**
- **Spring Cloud Gateway + Eureka Discovery**
- **Spring Security + JWT**
- **Redis (Streams & Pub/Sub)**
- **MongoDB**
- **WebSocket (STOMP + SockJS)**
- **Resilience4j** (Circuit Breaker + Retry)
- **Prometheus + Grafana** (Centralized Monitoring)

---

## 🚀 Running the Project
Ensure all dependencies (MongoDB, Redis, Eureka) are up and running.

### 1️⃣ Start Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
``

### 2️⃣ Start Auth Service
```bash
cd auth-service
mvn spring-boot:run
```

### 3️⃣ Start API Gateway
```bash
Copy code
cd api-gateway
mvn spring-boot:run
```

### 4️⃣ Start User Service
```bash
cd user-service
mvn spring-boot:run
```
### 5️⃣ Start Notification Service
```bash
cd notification-service
mvn spring-boot:run
```

---

## 📊 Centralized Monitoring (Prometheus + Grafana)

Docker Compose is used to run Prometheus and Grafana.

### ▶️ Start Monitoring Stack
```bash
docker-compose up -d
```

### 🔍 Prometheus
- URL: http://localhost:9090

### 📈 Grafana
- URL: http://localhost:3000
- Login: `admin / admin`

Prometheus scrapes metrics from:

- `eureka_server:8761`
- `auth-service:8081`
- `api_gateway:8082`
- `user-service:8084`
- `notification-service:8084`

---

## 🔔 Notification Workflow
1. Other services publish events (e.g., profile update) to Redis.
2. Notification Service listens via **Redis Streams** and **Redis Pub/Sub**.
3. Notifications are stored in MongoDB.
4. Real-time updates are pushed to WebSocket clients via `/topic/notifications`.

---

## ✅ Features Summary

- 🔐 JWT Authentication and Role-Based Access Control (RBAC)
- 🧠 Service Discovery via Eureka
- 📩 Event-driven notifications using Redis
- 📡 Real-time WebSocket broadcasting
- 📊 Prometheus + Grafana metrics dashboard
- 💥 Resilience4j for fault-tolerance (circuit breaker + retry)

---