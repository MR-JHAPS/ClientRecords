# 🔥 ClientRecords - Enterprise Client Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)
![HATEOAS](https://img.shields.io/badge/HATEOAS-5C2D91?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

---

## 📌 Overview
**ClientRecords** is a robust, scalable, and secure **Enterprise Client Management System** built with **Spring Boot**. It provides a streamlined approach to managing clients, users, and roles with a **JWT-based authentication system, soft deletion, audit logging**, and **HATEOAS-powered API pagination**.

The **Angular** front-end implementation is currently in progress and will be available in a separate repository. I will keep you updated once it is ready.


---

## 🚀 Key Features

### 🗃️ Client Management
✔ **Full CRUD Operations** (Create, Read, Update, Delete) with validation  
✔ **Soft Deletion System** (`ClientBin`) with recovery options  
✔ **Audit Logging** (`ClientLog`) to track all changes  
✔ **Advanced Search & Filtering** for efficient data retrieval  

### 👥 User & Role Management
✔ **Admin Dashboard** for user/role management  
✔ **Role-Based Access Control (RBAC)** using JWT  
✔ **Password Policy Enforcement** for enhanced security  
✔ **User Activity Logging** to track changes and authentication history  

### 🔐 Security & API
✔ **JWT Authentication & Authorization**  
✔ **Spring Security Implementation**  
✔ **HATEOAS-powered API Pagination**  
✔ **Swagger UI** for interactive API testing  
✔ **Global Exception Handling** for error management  

---

## 🏗 Project Structure

```plaintext
ClientRecords
│── src/main/java/com/jhaps/clientrecords
│   ├── apiResponse/            # Standardized API response models
│   ├── config/                 # Security, CORS, and Swagger configs
│   ├── controller/             # REST API endpoints
│   ├── dto/                    # Data Transfer Objects
│   ├── entity/                 # JPA database entities
│   ├── enums/                  # Enum constants
│   ├── exception/              # Custom exception handling
│   ├── repository/             # Spring Data repositories
│   ├── security/               # JWT and authentication
│   ├── service/                # Business logic interfaces
│   ├── serviceImpl/            # Service implementations
│   └── util/                   # Utility classes and mappers
│── src/main/resources
│   ├── application.properties  # DB and app configuration
└── pom.xml                     # Maven dependencies
```

---

## 🚀 Getting Started

### 🛠 Prerequisites
Ensure you have the following installed:

- **Java 17+**
- **Maven 3.8+**
- **MySQL 8.0+**

### 📥 Installation & Setup

#### 1️⃣ Clone the repository
```bash
git clone https://github.com/MR-JHAPS/ClientRecords.git

cd ClientRecords
```

#### 2️⃣ Configure database connection
Edit `application.properties` in `src/main/resources/`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clientdb
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
```

#### 3️⃣ Build and run the project
```bash
mvn clean install
mvn spring-boot:run
```

---

## 📡 API Documentation
Access interactive API documentation via Swagger:
🔗 **[Swagger UI](http://localhost:8080/swagger-ui.html)**

### 🔑 Key Endpoints
| Method | Endpoint          | Description                 | Access |
|--------|------------------|-----------------------------|--------|
| POST   | `/api/auth/login` | Authenticate user          | Public |
| GET    | `/api/clients`    | List all clients           | USER   |
| GET   | `/api/admin/user/find-all`    | Get All Users          | ADMIN  |
| PUT    | `/api/admin/user/update-role/{id}` | Update the role of User      | ADMIN  |
| DELETE    | `/api/client-bin/delete/{id}` | Permanently Deletes Client From Bin        | ADMIN  |

---

## 🎭 Demo Credentials

**Admin User:**
```plaintext
Email: admin@gmail.com
Password: 1111
```

---

## 💡 Why Choose ClientRecords?
✅ **Enterprise-Level Security** – JWT, RBAC, password policies  
✅ **Full Stack Solution** – Spring Boot backend & Angular frontend  
✅ **Scalable & Maintainable** – Modular design with clean architecture  
✅ **Optimized for Performance** – HATEOAS and pagination  
✅ **Developer-Friendly** – Well-documented API and code structure  

---

## 📞 Contact
📌 **Developer:** [Neraz Oli](www.linkedin.com/in/nerazoli)  
📧 **Email:** nerazoli.developer@gmail.com  
🌐 **GitHub:** [github.com/MR-JHAPS](https://github.com/MR-JHAPS)  

🚀 **If you find this project helpful, don’t forget to give it a star! ⭐**
