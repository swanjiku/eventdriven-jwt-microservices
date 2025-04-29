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
- ✅ Service registration & discovery
- ✅ Self-preservation mode disabled (ideal for local/dev environments)
- ✅ Health checks for registered services
- ✅ Prometheus endpoint for monitoring
- ✅ Eureka dashboard UI at `localhost:8761`

## 🚀 Getting Started

### 📦 Prerequisites
- Java 17+
- Maven 3.8+

### 🏃‍♂️ Run Locally
```bash
# 1. Clone the project
cd microservice_jwt/eureka_server

# 2. Build the project
mvn clean install

# 3. Run the application
mvn spring-boot:run
```

### 4. Access the Eureka Dashboard
- Visit: [http://localhost:8761](http://localhost:8761)

## ⚙️ Configuration
- **Port:** Default is `8761` (set in `application.yml`)
- **Self-preservation:** Disabled for local/dev (change in `application.yml` if needed for production)
- **Actuator/Prometheus:** Metrics available at `/actuator/prometheus`

### Example `application.yml`
```yaml
server:
  port: 8761
spring:
  application:
    name: eureka-server
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: false
management:
  endpoints:
    web:
      exposure:
        include: health, info, prometheus
```

## 🔗 Registering Services
- Other microservices must set their `eureka.client.service-url.defaultZone` to `http://localhost:8761/eureka/` in their configs.
- Example (in a service's `application.yml`):
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

## 🛠️ Troubleshooting
- **Service Not Showing Up?**
  - Ensure the service is running and configured with the correct Eureka URL.
  - Check network/firewall settings.
- **Dashboard Not Loading?**
  - Ensure Eureka Server is running and listening on port 8761.
- **Actuator/Prometheus Not Available?**
  - Confirm actuator endpoints are enabled in `application.yml`.

## 📚 References
- [Spring Cloud Netflix Eureka Docs](https://cloud.spring.io/spring-cloud-netflix/reference/html/)
- [Spring Boot Actuator Docs](https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/)