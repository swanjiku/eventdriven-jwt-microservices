
# 🔔 Notification Service - Event-Based Notification System

## 📌 Overview

The Notification Service handles real-time notifications by consuming events from Redis and sending notifications to users via different channels (e.g., push notifications).

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Data MongoDB**
- **Redis** (Event Subscription)
- **Spring WebSocket** (Real Time Updates)
- **Eureka Client** (Service Discovery)

## ⚙️ Features

**✅ Listens to User Events from Redis**<br>
**✅ Processes and Sends Notifications**<br>
**✅ WebSockets for Real-Time Updates**<br>
**✅ Service Discovery via Eureka**<br>

## 🚀 Running the Notification Service
### 🛠️ Prerequisites
Ensure you have installed:

- Java 17+
- Maven
- Redis (for message publishing and event handling)
- Eureka Server (Running)

### 💻 Steps to Run
#### 1️⃣ Navigate to the project directory:

```bash
cd notification-service
```

#### 2️⃣ Build the project:

```bash
mvn clean install
```
#### 3️⃣ Run the service:

```bash
mvn spring-boot:run
```

## 🔔 Notification Processing Flow
1. **User-Service publishes an event to Redis** when a user updates their profile.
2. **Notification-Service listens to the event** and processes the message.
3. **A notification is created and stored** in the database.
4. **A WebSocket connection is used** to push real-time notifications to clients.

## 📢 Future Enhancements
 **📧 Email and Push Notification Support**

## 🛠️ Common Issues & Fixes
### ❌ Notifications Not Being Stored?
✔️ Ensure Redis is running and accessible.<br>
✔️ Check if `auth-service` and `user-service` are correctly publishing events.
