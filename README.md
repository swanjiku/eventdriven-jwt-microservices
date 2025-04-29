# 🌐 Microservices JWT Authentication System

A robust, production-ready microservices system implementing JWT authentication and authorization using Spring Cloud, Eureka, Redis, MongoDB, and WebSocket. This project demonstrates scalable, event-driven architecture with centralized monitoring and real-time notifications.

---

## 🧩 Microservices Overview

| Service | Description |
|---------|-------------|
| 🧭 [Eureka Server](./eureka_server/README.md) | Service Discovery & Registry |
| 🚪 [API Gateway](./api_gateway/README.md) | Routing, JWT Validation, Centralized Entry Point |
| 🔐 [Auth Service](./auth-service/README.md) | Authentication, JWT Issuance, RBAC |
| 👤 [User Service](./user_service/README.md) | User Profile, Roles, Event Publishing |
| 🔔 [Notification Service](./notification-service/README.md) | Real-Time Notifications, WebSocket |

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

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven
- Docker (for Prometheus & Grafana)
- MongoDB, Redis running locally or via Docker

### 1️⃣ Start Core Dependencies
Ensure MongoDB, Redis, and Eureka Server are running.

```bash
# MongoDB (Docker)
docker run -d --name mongo -p 27017:27017 mongo
# Redis (Docker)
docker run -d --name redis -p 6379:6379 redis
```

### 2️⃣ Start Microservices (in separate terminals)
```bash
cd eureka_server && mvn spring-boot:run
cd auth-service && mvn spring-boot:run
cd api_gateway && mvn spring-boot:run
cd user_service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

### 3️⃣ Start Monitoring Stack
```bash
docker-compose up -d
```
- **Prometheus:** http://localhost:9090
- **Grafana:** http://localhost:3000 (login: admin/admin)

---

## 📚 Service Endpoints
- **Eureka Dashboard:** http://localhost:8761
- **API Gateway:** http://localhost:8082
- **Swagger UI:** http://localhost:8082/swagger-ui.html
- **Prometheus Metrics:** http://localhost:8082/actuator/prometheus
- **Notification WebSocket:** ws://localhost:8084/ws

---

## 🔔 Notification Workflow
1. Services publish events (e.g., user update) to Redis Streams.
2. Notification Service consumes events, stores them in MongoDB.
3. Real-time notifications are pushed to clients via WebSocket `/topic/notifications`.

---

## ✅ Features Summary
- 🔐 JWT Authentication & RBAC
- 🛡️ Centralized API Gateway Security
- 🧭 Service Discovery (Eureka)
- 📬 Real-Time Event-Based Notifications
- 📊 Centralized Monitoring (Prometheus + Grafana)
- 🔄 Circuit Breaker, Retry, and Fault Tolerance

---

## 📝 References & Docs
- [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)
- [Resilience4j](https://resilience4j.readme.io/docs)
- [Prometheus](https://prometheus.io/docs/introduction/overview/)
- [Grafana](https://grafana.com/docs/)

---

## 🤝 Contributing
Pull requests and issues are welcome! See each service's README for details on architecture, configuration, and extension.

---