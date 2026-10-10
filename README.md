# Event-Driven Microservices Architecture (Production-Ready)
A highly scalable, resource-optimized, and secure event-driven microservices project built with Spring Boot, deployed on a local Kubernetes cluster (k3d).

## 🚀 Architecture Overview
This project consists of two independent microservices communicating asynchronously via Apache Kafka. The entire setup is secured using Auth0 (OAuth 2.0) and Kubernetes Secrets, with Traefik handling API Gateway and Rate Limiting duties.

```
graph TD
    Client[Client / Swagger UI] -->|1. Get JWT Token| Auth0[Auth0 Identity Provider]
    Client -->|2. POST /order/publish + Bearer Token| Ingress[Traefik Ingress + Rate Limiter]
    Ingress -->|3. Routes Traffic| OrderService[Order Service <br/> OAuth2 Resource Server]
    OrderService -->|4. Publishes JSON Event| Kafka[(Aiven Cloud Kafka <br/> SSL/SASL)]
    Kafka -->|5. Consumes Event| InventoryService[Inventory Service <br/> OAuth2 Resource Server]
    InventoryService -->|6. Persists Data| DB[(Neon Serverless PostgreSQL)]
    
    K8sSecrets[Kubernetes Secrets] -.->|Injects DB & Kafka Passwords| OrderService
    K8sSecrets -.->|Injects DB & Kafka Passwords| InventoryService

```

## 🛠️ Tech Stack
* **Language & Framework:** Java 17, Spring Boot 3.x
* **Messaging Broker:** Apache Kafka (Hosted on Aiven Cloud with SSL/SCRAM-SHA-256)
* **Database:** Serverless PostgreSQL (Hosted on Neon DB)
* **Containerization & Orchestration:** Docker, Kubernetes (k3d)
* **API Gateway & Security:** Traefik Ingress Controller (with Rate Limiting Middleware)
* **Authentication & Authorization:** OAuth 2.0 (Auth0 Identity Provider, JWT validation)
* **Secrets Management:** Kubernetes Secrets (Base64 Encoded Environment Variables)
* **API Documentation:** Swagger UI (OpenAPI 3) with Bearer Token Auth

## 🔒 Security Implementations
1. **OAuth 2.0 Resource Server:** Both microservices are protected using Auth0. API endpoints strictly require a valid JWT Bearer token in the `Authorization` header.
2. **Kubernetes Secrets:** Sensitive credentials (Database passwords, Kafka SASL passwords) are entirely removed from the source code. They are stored securely in K8s Secrets and injected into Pods at runtime via Environment Variables.
3. **API Rate Limiting:** Traefik Ingress Middleware is configured to prevent DDoS attacks by limiting requests (e.g., max 2 req/sec, burst of 5).
4. **Git Secret Scanning Safe:** `application.properties` does not contain any hardcoded credentials.

## 📦 Build & Deployment Instructions
**1. Create Kubernetes Secrets:**
Before deploying, create a `secrets.yaml` file with your Base64 encoded credentials:
```
kubectl apply -f secrets.yaml
```

**2. Build the Application Jars & Docker Images:**
```
# In order-service directory
mvn clean package -DskipTests
docker build -t order-service:v4 .
k3d image import order-service:v4 -c my-cluster

# In inventory-service directory
mvn clean package -DskipTests
docker build -t inventory-service:v4 .
k3d image import inventory-service:v4 -c my-cluster

```

**3. Deploy to Kubernetes:**
Apply the main Kubernetes manifest (Deployments, Services, Traefik Middleware, and Ingress):
```
kubectl apply -f k8s.yaml
```

## 🧪 Testing the API
1. Obtain a valid JWT Access Token from your Auth0 Tenant.
2. Access the Swagger UI via the API Gateway: `http://localhost:8081/swagger-ui/index.html`
3. Click the **Authorize** button and paste your Bearer Token.
4. Execute the `POST /order/publish` endpoint with a JSON payload:
```
{
  "productName": "MacBook Pro M3",
  "quantity": 1
}
```
5. Check the `Inventory-Service` logs or Neon DB console to verify successful message consumption and database persistence.