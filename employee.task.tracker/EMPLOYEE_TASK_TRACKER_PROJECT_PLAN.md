# 🧠 Employee Task Tracker - MVP GitHub Project Plan

This GitHub Project outlines the complete MVP roadmap for your multi-tenant Employee Task Tracker application using Spring Boot, PostgreSQL, MongoDB, Kafka, RabbitMQ, Spring Security (JWT), Swagger, and Docker.

---

## 📅 Project Created
**2025-07-17**

---

## 🗂️ Features Overview (Epics)

Each feature below is broken down into actionable tasks and sub-tasks to follow step by step during development.

---

### ✅ Feature 1: Authentication & Authorization with JWT

- [ ] Create `User` or use `Employee` with `username`, `password`, `role`
- [ ] Add BCrypt password hashing
- [ ] Set up Spring Security basic config
- [ ] Implement JWT Token Provider (create/validate tokens)
- [ ] Store `departmentId` in token claims
- [ ] Secure endpoints via `@PreAuthorize`

---

### 🌐 Feature 2: Multi-Tenancy with Department

- [ ] Model `Department` as tenant with hierarchy (parent-child)
- [ ] Add `DepartmentEmployeesHistory` entity
- [ ] Determine active department per employee
- [ ] Filter queries by department (tenant scoping)
- [ ] Load department info into JWT during login

---

### 🧱 Feature 3: Full CRUD APIs

#### 🔹 Employee
- [ ] Create employee
- [ ] Update employee
- [ ] Assign department
- [ ] List/filter employees by department

#### 🔹 Task
- [ ] Assign task to employee
- [ ] Update task status
- [ ] List tasks by department/employee
- [ ] TaskHistory audit trail

#### 🔹 Department
- [ ] Create/edit/delete departments
- [ ] Support parent-child structure

---

### 📣 Feature 4: Messaging with Kafka & RabbitMQ

- [ ] Configure Kafka + RabbitMQ in Docker
- [ ] Send event on task assignment
- [ ] Send event on task status update
- [ ] Implement Kafka Consumer to log events

---

### 🧾 Feature 5: MongoDB for Auditing

- [ ] Connect MongoDB with Spring Boot
- [ ] Create `AuditLog` document model
- [ ] Save Kafka events to Mongo
- [ ] Add audit service to log custom actions

---

### 🧪 Feature 6: Swagger & OpenAPI Integration

- [ ] Add springdoc-openapi dependency
- [ ] Annotate all endpoints with descriptions
- [ ] Configure Swagger UI for JWT Auth
- [ ] Generate OpenAPI docs

---

### 🐳 Feature 7: Dockerize All Services

- [ ] Multi-stage Dockerfile for backend
- [ ] Docker Compose with:
  - app
  - postgres
  - mongo
  - kafka + zookeeper
  - rabbitmq
- [ ] Use `.env` for secrets & configuration
- [ ] Add volume for local Maven cache

---

## ✅ How to Use

1. Create a GitHub repo.
2. Copy this file into `PROJECT_PLAN.md` or `README.md`.
3. Use GitHub Projects or Notion to track progress per feature.
4. Each checkmark becomes your next coding task.

---

Happy coding! 🚀