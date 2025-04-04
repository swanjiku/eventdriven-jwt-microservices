
# 🧭 Eureka Server - Service Discovery

## 📘 Overview

The **Eureka Server** is the core of service discovery in this microservices architecture. It allows services like `auth-service`, `user-service`, and others to register and discover each other without hardcoded IPs or URLs. This promotes flexibility, fault tolerance, and scalability.

## 🧰 Tech Stack

- **Java 17**
- **Spring Boot 3.4**
- **Spring Cloud Netflix Eureka**
- **Micrometer + Prometheus** (for monitoring)
- **Spring Boot Actuator**

## ⚙️ Features

✅ Service registration & discovery<br>
✅ Self-preservation mode disabled (ideal for local/dev environments)<br>
✅ Health checks for registered services<br>
✅ Prometheus endpoint for monitoring<br>
✅ Eureka dashboard UI at `localhost:8761`<br>

## 🚀 Getting Started
### 📦 Prerequisites
- Java 17+
- Maven 3.8+

### 🏃‍♂️ Run Locally

#### 1️. Clone the project
```bash
git clone https://github.com/swanjiku/microservice_jwt.git
cd microservice_jwt/eureka_server
```

#### 2️. Build the project
```bash
mvn clean install
```

#### 3️. Run the application
```bash
mvn spring-boot:run
```

### 4. Access the Eureka Dashboard
```arduino
http://localhost:8761
```

## ⚙️ Configuration
`application.yml`

```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: false

management:
  endpoints:
    web:
      exposure:
        include: health, metrics, prometheus
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      uri: http.request.uri
```
**🔐 Self-preservation is disabled for development. Enable it in production to prevent mass deregistration during network issues.**

## 🔗 How Other Services Register

Each microservice (e.g., `auth-service`, `user-service`) should include this config in their `application.yml`:
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

And include the dependency in `pom.xml`:
```xml
<dependency>
<groupId>org.springframework.cloud</groupId>
<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

## 🔗 How Other Services Register

Eureka Server exposes a `/actuator/prometheus` endpoint, which Prometheus can scrape.
### Prometheus Scrape Config:
```yaml
- job_name: 'eureka-server'
  metrics_path: '/actuator/prometheus'
  static_configs:
    - targets: ['localhost:8761']
```

## 🐞 Common Issues

| Problem      | Solution |
| ----------- | ----------- |
| ❌ Services not showing in UI      | ✅ Ensure correct Eureka client URL in services       |
| ❌ Prometheus not scraping metrics   | ✅ Check if `/actuator/prometheus` is enabled and exposed        |
| ❌ `404 Not Found` for dashboard   | ✅ Confirm server runs on `localhost:8761`        |