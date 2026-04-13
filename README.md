# Library Management System

A console-based application for library management. This project demonstrates the evolution of a data-driven application from flat-file storage to a relational database using a modern Spring and Hibernate stack.

## Tech Stack
* **Language:** Java 17
* **Framework:** Spring Framework (Context, AOP, TX, ORM)
* **Persistence:** Hibernate Native (Jakarta Persistence API)
* **Database:** PostgreSQL
* **Build Tool:** Gradle
* **Testing:** JUnit 5, Mockito

## Key Architectural Improvements
* **Data Integrity:** Transitioned from string-based author names to a relational model using `authorId`. This prevents data duplication and ensures normalization within the database.
* **Hibernate Optimization:**
    * Implemented **Criteria API** for type-safe queries.
    * Resolved the **N+1 select problem** by using `left join fetch` for book-author relationships.
    * Implemented **Business Key** equality in domain entities (`equals` and `hashCode`) for consistent behavior across Hibernate session states.
* **Aspect-Oriented Programming (AOP):**
    * **Logging:** Automated logging of service-layer method executions.
    * **Caching:** Custom AOP-based caching mechanism to reduce redundant database lookups.
* **Localization (i18n):** Multi-language support (English/Polish) for all console interactions, determined at application startup.

## Project Structure
* `com.example.library.domain`: JPA Entities and Data Transfer Objects (DTOs).
* `com.example.library.service`: Business logic layer managed by Spring transactions.
* `com.example.library.storage.hibernate`: Data access layer using Hibernate SessionFactory.
* `com.example.library.ui`: Console-based interface and command patterns.
* `com.example.library.aop`: Cross-cutting concerns including caching and logging.

## Setup and Installation

### Prerequisites
* JDK 17
* PostgreSQL instance 

### Configuration
1. Create a PostgreSQL database.
2. Configure database credentials in `src/main/resources/db.properties`.
3. The database schema is located in `src/main/resources/db/schema.sql`.

### Execution
```bash
# Build the project
./gradlew build

# Run the application
./gradlew run

# Run tests
./gradlew test