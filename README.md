# 📋 Task Tracker API

A RESTful Spring Boot application for project and task management with **role-based access control (RBAC)**. Built for collaboration across three user roles: `ADMIN`, `MANAGER`, and `USER`.

---
## ⚙️ How to Run the Application

### 🖥️ Prerequisites

- Java 17
- Maven
- H2 in-memory for dev

### 🏁 Steps

1. **Clone the project**:
   ```bash
   git clone https://github.com/mariamigogoladze02/task-tracker-api.git
   ```

2. **Set DB config** in `application.properties`.

3. **Run the app**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access Swagger docs**:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

---

## 👥 Roles & Permissions

| Role    | Project Access           | Task Access                          |
|---------|--------------------------|--------------------------------------|
| ADMIN   | Full CRUD on all         | Full CRUD on all tasks               |
| MANAGER | CRUD on own projects     | Assign/update tasks in own projects |
| USER    | None                     | View/update their own tasks         |


## 🔐 Authentication (JWT-based)

- **Login** to receive a **JWT token**
- Pass the token in the `Authorization` header as:
  ```
  Authorization: Bearer <token>
  ```
- Protects endpoints based on user roles

---

## 📌 API Overview (Sample)

### 🔑 Auth

- `POST /api/auth/authorization` – Authorization
- `POST /api/auth/refreshToken` – Refresh Token
- `GET /api/auth/logout` – Logout
---

### 📁 Projects

- `GET /api/project` – List all (MANAGER/ADMIN)
- `POST /api/project` – Create (MANAGER/ADMIN)
- `PUT /api/project` – Update (Owner/ADMIN)
- `DELETE /api/project/{id}` – Delete (Owner/ADMIN)

### 📋 Tasks

- `GET /api/task` – View all tasks
- `POST /api/task` – Create task (MANAGER/ADMIN)
- `DELETE /api/task/{id}` – Delete task
- `PUT /api/task` – Update task
- `PUT /api/task/{id}/status` – Update task status
- `PUT /api/task//{id}/assign` – Update tasks assigned to current user

---

## 📬 Postman Collection

- Available in the project in the resources folder. Import it into Postman
