ClientRecords Application

Java
Spring Boot
Swagger
JWT
HATEOAS
Logback

ClientRecords is a robust Spring Boot application designed to manage client records with role-based access control. It provides functionalities for user management, client operations, and advanced search capabilities. The application is secured using JWT (JSON Web Tokens) and implements role-based authorization to ensure secure access to resources.
✨ Key Features

    Role-Based Access Control: Admins can manage users and clients, update roles, and perform CRUD operations.

    JWT Authentication: Secure authentication and authorization using JSON Web Tokens.

    Advanced Search: Search clients by firstName, lastName, postalCode, or any custom query.

    Pagination & Sorting: Supports pagination and sorting using HATEOAS and Pageable.

    Custom Exceptions: Handles exceptions globally with custom error responses.

    Logging: Integrated with SLF4J and Logback for detailed logging.

    Swagger API Documentation: Auto-generated API documentation for easy testing and integration.

    DTOs & Mappers: Uses DTOs (Data Transfer Objects) and mappers for clean data handling.

    PreAuthorize: Prevents unauthorized access to sensitive endpoints.

🛠️ Technical Highlights
1. Spring Security with JWT

    Implemented JWT-based authentication and authorization.

    Configured Spring Security to restrict access based on user roles.

    Used @PreAuthorize to enforce role-based access at the method level.

2. Advanced Search Functionality

    Built a flexible search mechanism in ClientSearchController to query clients by:

        firstName

        lastName

        postalCode

        Any custom query.

3. Pagination & Sorting

    Implemented pagination and sorting using Spring Data JPA and HATEOAS.

    Created a utility class SortBuilder to dynamically generate sorting criteria:
    java
    Copy

    public static Sort createSorting(String direction, String sortBy) {
        if (sortBy == null || direction == null) {
            return null;
        }
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(sortDirection, sortBy);
    }

4. Global Exception Handling

    Implemented a GlobalExceptionHandler to handle exceptions globally.

    Returned consistent API responses using ApiResponseModel:
    json
    Copy

    {
      "timestamp": "2023-10-01T12:00:00Z",
      "message": "Error message",
      "status": 400,
      "data": null
    }

5. Logging

    Integrated SLF4J with Logback for detailed logging.

    Configured logging in logback.xml to log to both console and file.

6. Swagger API Documentation

    Configured Swagger to auto-generate API documentation.

    Accessible at: http://localhost:8080/swagger-ui.html.

📂 Project Structure

The application is structured into 12 packages:

    com.jhaps.clientrecords.config: Contains configurations for CORS, Spring Security, and Swagger.

    com.jhaps.clientrecords.controller: Includes controllers for Admin, User, Role, Client, and Public operations.

    com.jhaps.clientrecords.dto: Contains DTOs for User, Role, and Client entities.

    com.jhaps.clientrecords.entity: Defines User, Role, and Client entities with Many-to-Many relationships.

    com.jhaps.clientrecords.enums: Contains enums for ResponseMessage and RoleNames.

    com.jhaps.clientrecords.exceptions: Custom exceptions for specific error handling.

    com.jhaps.clientrecords.exceptionHandler: Global exception handling with logging.

    com.jhaps.clientrecords.filter: JWT filter for authentication.

    com.jhaps.clientrecords.repository: Repository interfaces for database operations.

    com.jhaps.clientrecords.response: Contains ApiResponseModel and ApiResponseBuilder for consistent API responses.

    com.jhaps.clientrecords.utils: Utility classes like SortBuilder and Mapper.

    com.jhaps.clientrecords.service: Service interfaces and implementations for business logic.

🚀 Getting Started
Prerequisites

    Java 17

    Maven

    MySQL (or any preferred database)

Installation

    Clone the repository:
    bash
    Copy

    git clone https://github.com/your-username/clientRecords.git

    Navigate to the project directory:
    bash
    Copy

    cd clientRecords

    Build the project:
    bash
    Copy

    mvn clean install

    Run the application:
    bash
    Copy

    mvn spring-boot:run

Database Setup

    Update the application.properties file with your database credentials.

    Run the application to auto-create tables or use a database migration tool like Flyway.

📚 API Documentation

The application uses Swagger for API documentation. After running the application, access the Swagger UI at:
Copy

http://localhost:8080/swagger-ui.html

🎯 Skills Demonstrated

    Spring Boot: Built a robust backend application with Spring Boot.

    Spring Security: Implemented JWT-based authentication and role-based authorization.

    RESTful APIs: Designed clean and consistent REST APIs.

    Exception Handling: Global exception handling with custom responses.

    Logging: Integrated SLF4J and Logback for detailed logging.

    Pagination & Sorting: Implemented pagination and sorting using HATEOAS.

    Swagger: Auto-generated API documentation for easy testing.

    Clean Code: Used DTOs, mappers, and utility classes to ensure clean and maintainable code.

📧 Contact

For any inquiries or feedback, feel free to reach out:


    LinkedIn: www.linkedin.com/in/nerazoli
