# Mock Exchange Pro 🏦📈

A full-stack, high-concurrency trading platform built with Java 17, Spring Boot microservices, Kafka, Redis, MongoDB, Docker, Kubernetes, and deployed to GCP.  
This project aims to simulate a simplified stock exchange infrastructure similar to those used in financial institutions like HSBC.

---

## 🚀 Project Vision

> A modern trading system architecture designed for speed, scalability, and resiliency.  
It supports user registration, order placement, real-time order matching, trade broadcasting, and secure authentication with full DevOps lifecycle.

---

## 📦 Tech Stack (Backend + Frontend + DevOps)

| Layer       | Tech Used                                                                 |
|-------------|---------------------------------------------------------------------------|
| Backend     | Java 17, Spring Boot 3, Spring Cloud, Spring Security, JPA, Redis, Kafka |
| Frontend    | React (Vite), Zustand, React Hook Form, Chart.js, WebSocket              |
| Database    | PostgreSQL, Redis, MongoDB                                               |
| Messaging   | Apache Kafka (for async order processing and auditing)                   |
| DevOps      | Docker, Kubernetes, Helm, GitHub Actions, GCP (GKE, Cloud SQL)           |
| Architecture| Microservices, Config Server, Eureka, REST APIs, WebSocket               |

---

## 📁 Project Structure

```bash
mock-exchange-pro/
├── service-registry/       # ✅ Eureka Server (service discovery)
├── config-server/          # ✅ Centralized configuration management
├── auth-service/           # 🔜 Spring Security + JWT Authentication (Next Step)
├── user-service/           # 🔜 User profile and asset info
├── order-service/          # 🔜 Order submission + Kafka producer
├── match-engine-service/   # 🔜 Real-time order matching engine
├── trade-service/          # 🔜 Trade record persistence
├── quote-service/          # 🔜 WebSocket price broadcasting
├── audit-service/          # 🔜 MongoDB + Kafka consumer for audit logging
├── common-lib/             # 🔜 Shared DTOs, exception handlers, utils
├── frontend/               # 🔜 React-based UI (Vite, Zustand, Charts)  React + Axios
├── docker/                 # 🔜 Docker Compose for local envs (Postgres, Kafka, Redis)
├── helm/                   # 🔜 Helm charts for GCP Kubernetes deployment
└── .github/workflows/      # 🔜 GitHub Actions for CI/CD


GCP  +  Cloud Run、Artifact Registry、Cloud Build Deployment Docs
#  jar
mvn clean package -DskipTests

# （auth-service ）
docker build -t us-central1-docker.pkg.dev/mock-exchange-pro/mock-exchange-docker/auth-service:latest .

#  GCP 
gcloud auth print-access-token | docker login -u oauth2accesstoken --password-stdin https://us-central1-docker.pkg.dev
 
docker push us-central1-docker.pkg.dev/mock-exchange-pro/mock-exchange-docker/auth-service:latest
Cloud Run ：
gcloud run deploy auth-service \
  --image=us-central1-docker.pkg.dev/mock-exchange-pro/mock-exchange-docker/auth-service:latest \
  --platform=managed \
  --region=us-central1 \
  --allow-unauthenticated \
  --port=8080


Frontend:
React   TypeScript   TailwindCSS  