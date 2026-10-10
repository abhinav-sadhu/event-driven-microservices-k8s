# Event-Driven Microservices Architecture (Production-Ready)

A highly scalable, resource-optimized, event-driven microservices project built with Spring Boot, Apache Kafka, and Kubernetes. Designed and deployed locally using k3d with cloud-based dependencies to minimize local RAM usage.

## 🚀 Architecture Overview

This project consists of two independent microservices communicating asynchronously via Apache Kafka. It uses Traefik as an API Gateway with built-in Rate Limiting to prevent DDoS attacks.

```mermaid
graph TD
    Client[Client / Swagger UI] -->|POST /order/publish| Ingress[Traefik Ingress Gateway<br/>(Rate Limiter: 2 req/sec)]
    Ingress -->|Routes traffic| OrderService[Order Service<br/>(Kafka Producer)]
    OrderService -->|Publishes JSON Event| Kafka[(Aiven Cloud Kafka<br/>SSL/SASL)]
    Kafka -->|Consumes Event| InventoryService[Inventory Service<br/>(Kafka Consumer)]
    InventoryService -->|Persists Data| DB[(Neon Serverless PostgreSQL)]
```

## 🛠️ Tech Stack
* **Language & Framework:** Java 17, Spring Boot 3.x
* **Messaging Broker:** Apache Kafka (Hosted on Aiven Cloud with SSL/PEM security)
* **Database:** Serverless PostgreSQL (Hosted on Neon DB)
* **Containerization & Orchestration:** Docker, Kubernetes (k3d)
* **API Gateway & Security:** Traefik Ingress Controller (Rate Limiting Middleware)
* **API Documentation:** Swagger UI (OpenAPI 3)

## 🌟 Key Features
* **Event-Driven Communication:** Decoupled architecture using Kafka topics.
* **RAM-Optimized Environment:** Uses cloud databases and Kafka to run smoothly on 8GB RAM local machines (WSL2/Ubuntu).
* **Security & Resilience:** Configured with Traefik Rate Limiting (HTTP 429 Too Many Requests) to protect against traffic spikes.
* **Standardized Payloads:** Uses JSON Data Transfer Objects (DTOs) for strict data contracts.

## 🚦 Prerequisites
* Docker Desktop & WSL2 (Ubuntu)
* k3d & kubectl installed
* Java 17 & Maven

## 📦 Build & Deployment Instructions

**1. Build the Application Jars:**
```bash
# In order-service directory
mvn clean package -DskipTests

# In inventory-service directory
mvn clean package -DskipTests
```

**2. Build & Import Docker Images to k3d:**
```bash
docker build -t order-service:v3 ./order-service
docker build -t inventory-service:v2 ./inventory-service

k3d image import order-service:v3 -c my-cluster
k3d image import inventory-service:v2 -c my-cluster
```

**3. Deploy to Kubernetes:**
```bash
kubectl apply -f k8s.yaml
```

## 🧪 Testing the API

Once the pods are in the `Running` state, access the Swagger UI through the API Gateway:

**Swagger UI URL:** `http://localhost:8081/swagger-ui/index.html`

**Sample JSON Request Body:**
```json
{
  "productName": "MacBook Air M3",
  "quantity": 2
}
```

*Note: Rapid successive clicks on "Execute" will trigger the Traefik Rate Limiter, returning a `429 Too Many Requests` status.*