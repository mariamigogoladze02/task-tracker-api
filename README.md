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
   git clone https://github.com/yourusername/task-tracker-api.git
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

| Role   | Project Access      | Task Access  |

|--------|---------------------|--------------|

| ADMIN  | Full CRUD on all    | Full CRUD on all tasks  |

| MANAGER| CRUD on own projects| Assign/update tasks in own projects |

| USER   | None                | View/update their own tasks     |

---

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

- `POST /api/auth/register` – Register new user
- `POST /api/auth/login` – Login and receive JWT

### 👤 Users

- `GET /api/users/me` – Get current user info

### 📁 Projects

- `GET /api/projects` – List all (ADMIN only)
- `POST /api/projects` – Create (MANAGER/ADMIN)
- `PUT /api/projects/{id}` – Update (Owner/ADMIN)
- `DELETE /api/projects/{id}` – Delete (Owner/ADMIN)

### 📋 Tasks

- `GET /api/tasks` – View all tasks (paginated, filterable)
- `GET /api/tasks/assigned` – View tasks assigned to current user
- `POST /api/tasks` – Create (MANAGER/ADMIN)
- `PUT /api/tasks/{id}` – Update (Assigned user or project owner)
- `PATCH /api/tasks/{id}/status` – Update status (Assigned user only)

---

## 📬 Postman Collection

- Available in the project in the resources folder. Import it into Postman
- Includes sample requests for:
    - Authentication
    - Project & Task management
    - Role-based access scenarios