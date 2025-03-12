
# 🚀 Eureka Server - Service Discovery

## 📌 Overview

The Eureka Server acts as a service registry where microservices register themselves and discover other services dynamically. This enables **load balancing, failover handling, and dynamic service discovery** in a microservices architecture.

## 🛠️ Tech Stack

- **Java**
- **Spring Boot**
- **Spring Cloud Netflix Eureka**

## ⚙️ Features

✅ Service Discovery & Registration<br>
✅ Dynamic Load Balancing<br>
✅ High Availability with Peer Replication<br>
✅ Health Checks for Registered Services<br>

## 🚀 Running the Eureka Server
### 🛠️ Prerequisites

Ensure you have installed:

- Java 17+
- Maven

### 💻 Steps to Run
#### 1️⃣ Navigate to the project directory:
```bash
cd eureka-server
```
#### 2️⃣ Build the project:
```bash
mvn clean install
```
#### 3️⃣ Run the server:
```bash
mvn spring-boot:run
```

### 🔍 Access Eureka Dashboard
Once running, open the Eureka dashboard in your browser:

📌 `http://localhost:8761`

## 🔧 Configuration (`application.yml`)
Eureka Server is configured in `src/main/resources/application.yml`:

```yaml
server:
port: 8761

eureka:
instance:
hostname: localhost
client:
registerWithEureka: false  # Since this is the server, it doesn't register itself
fetchRegistry: false
```
## 📌 Service Registration

Other microservices (like `auth-service`, `user-service`, etc.) should register themselves by adding the following in their `application.yml`:

```yaml
eureka:
client:
serviceUrl:
defaultZone: http://localhost:8761/eureka/
```
## 🛠️ Common Issues & Fixes

### ❌ Eureka Dashboard Not Showing Services?

✔️ Ensure the services are correctly configured to register with Eureka.<br>
✔️ Check the Eureka client `serviceUrl` in each microservice's `application.yml`.