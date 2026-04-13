# Library Management System

A RESTful Web application for library management. This project demonstrates the evolution of a data-driven application from a simple console-based tool with flat-file storage to a modern, robust Web API using the Spring Boot ecosystem and a relational database.

## Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot (Web, Data JPA, AOP)
* **Persistence:** Hibernate ORM, Spring Data JPA
* **Database Migrations:** Liquibase
* **Database:** PostgreSQL
* **Build Tool:** Gradle
* **Testing:** JUnit 5, Mockito, Postman

## Key Architectural Improvements

* **RESTful API Design:** Transitioned from a console UI to a fully functional REST API using Spring MVC, handling HTTP methods (GET, POST, PUT, DELETE) and standard status codes.
* **Spring Data JPA:** Replaced boilerplate Hibernate Native DAO classes with Spring Data interfaces, significantly reducing code complexity while maintaining powerful data access capabilities.
* **Database Migrations (Liquibase):** Replaced manual SQL scripts (`schema.sql`) and unsafe Hibernate `ddl-auto` with Liquibase for automated, version-controlled database schema management.
* **JSON Serialization Handling:** Resolved infinite recursion issues (circular references) in bidirectional JPA relationships (e.g., Book <-> Author) using Jackson annotations (`@JsonIgnore`).
* **Data Integrity:** Transitioned from string-based author names to a fully normalized relational model using `authorId`. 
* **Aspect-Oriented Programming (AOP):** * **Logging:** Automated logging of service-layer method executions.
    * **Caching:** Custom AOP-based caching mechanism (`@Cached`) to reduce redundant database lookups.

## Project Structure

* `com.example.library.controller`: Spring MVC REST Controllers exposing API endpoints.
* `com.example.library.service`: Business logic layer managed by Spring transactions.
* `com.example.library.repository`: Spring Data JPA repository interfaces for database access.
* `com.example.library.domain`: JPA Entities representing the database tables.
* `com.example.library.aop`: Cross-cutting concerns including custom caching and logging aspects.

## Setup and Installation

### Prerequisites
* JDK 17
* PostgreSQL instance running on `localhost:5432`

### Configuration
1.  Create a PostgreSQL database named `library`.
2.  Configure database credentials (URL, username, password) in `src/main/resources/application.properties`.
3.  *Note: Database schema and tables will be automatically created and managed by Liquibase on application startup.*

### Execution

```bash
# Build the project
./gradlew build

# Run the Spring Boot application
./gradlew bootRun

# Run tests
./gradlew test
