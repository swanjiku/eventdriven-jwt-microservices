# 🔔 Notification Service - Event-Based Notification System

## 📌 Overview
The Notification Service is responsible for handling real-time and asynchronous notifications within the microservices ecosystem. It consumes user events from Redis (both Streams and Pub/Sub), stores them in MongoDB, and delivers notifications via WebSocket to subscribed clients.

---

## 🛠️ Tech Stack
- **Java 17+**
- **Spring Boot 3+**
- **Spring Data MongoDB**
- **Redis** (Streams + Pub/Sub)
- **Spring WebSocket (STOMP)**
- **Spring Security + JWT**
- **SpringDoc OpenAPI (Swagger)**
- **Eureka Client** (Service Discovery)

---

## ⚙️ Features
- ✅ Dual Redis listeners: **Streams** + **Pub/Sub**
- ✅ Real-time WebSocket push notifications
- ✅ MongoDB for persistent notification storage
- ✅ Role-based access control (RBAC)
- ✅ Secure endpoints using JWT
- ✅ Swagger UI for API exploration
- ✅ Service Discovery with Eureka

---

## 🧩 Architecture Overview
1. **Publisher**: Publishes notifications to Redis Stream (`notifications_stream`).
2. **Redis Stream Listener**: Reads messages from Redis stream and forwards them via WebSocket.
3. **Redis Subscriber**: Also listens to Redis Pub/Sub channel (`notifications`) as fallback or secondary channel.
4. **WebSocket (STOMP)**: Clients connect to `/ws` and subscribe to `/topic/notifications`.
5. **REST API**: Exposes endpoints to retrieve and send notifications.
6. **MongoDB**: Stores all notifications.

---

## 🚀 How to Run

### ✅ Prerequisites
- Java 17+
- Maven
- Redis (running locally or remotely)
- MongoDB (running locally or remotely)
- Eureka Server (must be running)

### 🔧 Steps
```bash
# 1. Navigate to the project directory
cd notification-service

# 2. Build the project
mvn clean install

# 3. Run the service
mvn spring-boot:run
```

---

## 🔗 API Endpoints & WebSocket Usage
- **REST API Base:** `/api/notifications`
- **WebSocket Endpoint:** `/ws`
- **WebSocket Topic:** `/topic/notifications`
- **Swagger UI:** `http://localhost:8084/swagger-ui.html`

### Example: Send a Notification (REST)
```bash
curl -X POST -H "Authorization: Bearer <jwt-token>" -H "Content-Type: application/json" \
  -d '{"userId": "123", "message": "Hello!"}' \
  http://localhost:8084/api/notifications
```

### Example: WebSocket Client (JS)
```js
const socket = new SockJS('http://localhost:8084/ws');
const stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
  stompClient.subscribe('/topic/notifications', function(notification) {
    console.log(notification.body);
  });
});
```

---

## ⚙️ Configuration
- **MongoDB URI:** `spring.data.mongodb.uri` (default: `mongodb://localhost:27017/notifications`)
- **Redis URI:** `spring.redis.host`/`spring.redis.port` (default: `localhost:6379`)
- **JWT Secret:** `jwt.secret` in `application.yml`

---

## 🛠️ Troubleshooting
- **WebSocket Not Connecting:** Ensure port `8084` is open and CORS is configured for your frontend.
- **No Notifications Delivered:** Check Redis/MongoDB connectivity and event publishing.
- **Swagger UI Not Loading:** Confirm dependencies and actuator endpoints are enabled.

---

## 🧪 Running Tests
```bash
mvn test
```

---

## 🔄 Extending the Service
- Add new notification types by updating the event publishing logic.
- To support new channels (e.g., email/SMS), extend the notification handler.
- For advanced RBAC, update Spring Security configuration.

---

## 📚 References
- [Spring WebSocket Docs](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket)
- [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/)
- [Spring Security Docs](https://docs.spring.io/spring-security/site/docs/current/reference/html5/)

---