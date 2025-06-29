# 🟦 Shape Management Application

A full-stack Shape Management System to create, update, delete, and manage geometric shapes.

---

## 📦 Tech Stack

- **Backend**: Java 21, Spring Boot 3.5.3, Spring Data JPA, Spring Security, Swagger (springdoc-openapi), Lombok
- **Frontend**: React.js, Tailwind CSS, Axios
- **Database**: MySQL 8 (Dockerized)

---

## 📁 Project Structure

Shape-Management-Application/
├── backend/ # Spring Boot backend API
├── frontend/ # React + Tailwind frontend
├── docker-compose.yml # Docker for MySQL DB



---

## ⚙️ Backend Setup (Spring Boot)

### 🔧 Prerequisites

- Java 21+
- Maven
- Docker & Docker Compose

### 1️⃣ Start MySQL using Docker

In the project root, run:

```bash
docker-compose up -d
```
- MySQL on port 3307
- Database: shapedb
- Username: shapeuser
- Password: shapepassword

application.properties:

spring.application.name=backEnd
server.port=8000
spring.datasource.url=jdbc:mysql://localhost:3307/shapedb
spring.datasource.username=shapeuser
spring.datasource.password=shapepassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=always
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

cd backend
mvn clean install
mvn spring-boot:run

http://localhost:8000/swagger-ui/index.html

## 🌐 Frontend Setup (React + Tailwind CSS + MUI)

### 📁 Navigate to the frontend

```bash
cd shape-management-frontend
```

npm install

npm start

REACT_APP_API_BASE_URL=http://localhost:8000

---Aauthentication details----
Username: admin
Password: admin



