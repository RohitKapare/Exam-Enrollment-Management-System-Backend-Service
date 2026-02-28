# Learning Navigator

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen?logo=springboot)
![Gradle](https://img.shields.io/badge/Gradle-8.11.1-02303A?logo=gradle)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql)

A RESTful API service built with **Spring Boot** and **MySQL** to manage exam registrations for a Learning Management System (LMS). The service handles full CRUD operations for Students, Subjects, and Exams, enforcing business rules such as requiring subject enrollment before exam registration.
- Common errors are handled gracefully and return appropriate HTTP codes.
- Includes `postman_collection.json` file for quick API testing in postman.
- Simple CRUD related unit tests are implemented using Mockito library.

---

## What the Project Does

Learning Navigator exposes a REST API that allows clients to:

- Create and manage **Students**, **Subjects**, and **Exams**
- Enroll students in subjects
- Register students for exams — subject enrollment is enforced as a prerequisite
- Retrieve and delete all three resource types

The service returns structured JSON error responses for almost all failure scenarios, making it straightforward to integrate with frontend or other backend services.

---

## Key Features

- **Full CRUD** for Students, Subjects, and Exams
- **Enrollment workflow** — students enroll in a subject first, then register for that subject's exam
- **Global Exception Handling** via `@ControllerAdvice` with standardized `ErrorResponse` payloads (HTTP status, message, timestamp)
- **Bean Validation** on request bodies using Jakarta Validation annotations
- **MapStruct** mappers for clean, compile-time-safe DTO ↔ entity conversion
- **Lombok** for boilerplate-free model classes
- **Unit tests** with Mockito and MockMvc covering all three controller layers



---

## Tech Stack

| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.1 |
| Spring Data JPA | (managed by Spring Boot) |
| Spring Validation | 3.4.1 |
| MapStruct | 1.6.3 |
| Lombok | (managed by Spring Boot) |
| MySQL Connector/J | (managed by Spring Boot) |
| Gradle Wrapper | 8.11.1 |
| MySQL | 8.0.43 |
| JUnit 5 + Mockito | (managed by Spring Boot) |

---

## Getting Started

### Prerequisites

- **Java 21** 
- **MySQL 8.0**
- **Git**

No separate Gradle installation is needed; the project ships with the Gradle Wrapper (`gradlew` / `gradlew.bat`).

```

### Configuration

Edit [`src/main/resources/application.properties`](src/main/resources/application.properties) to match your MySQL credentials:

```properties
spring.application.name=learning_navigator
server.port=8081

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/test_db
spring.datasource.username=root
spring.datasource.password=Test@1234
spring.jpa.hibernate.ddl-auto=update
```

> `ddl-auto=update` will automatically create or update the required tables (`students`, `subjects`, `exams`, `student_subject_enrollment`, `student_exam_registration`) on first startup.

### Running the Application

**Windows:**
```bat
gradlew.bat bootRun
```

**Linux / macOS:**
```bash
./gradlew bootRun
```

**Build a JAR and run it:**
```bash
./gradlew build
java -jar build/libs/learning_navigator-0.0.1-SNAPSHOT.jar
```

The API will be available at `http://localhost:8081`.

---

## API Overview

All endpoints are relative to `http://localhost:8081`.

A ready-to-use Postman collection is included: [`postman_collection.json`](postman_collection.json).

### Students

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/students` | Create a new student |
| `GET` | `/students` | List all students |
| `GET` | `/students/{id}` | Get student by ID |
| `DELETE` | `/students/{id}` | Delete a student |
| `POST` | `/students/{studentId}/subjects/{subjectId}` | Enroll student in a subject |
| `POST` | `/students/{studentId}/exams/{examId}` | Register student for an exam |

### Subjects

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/subjects` | Create a new subject |
| `GET` | `/subjects` | List all subjects |
| `GET` | `/subjects/{id}` | Get subject by ID |
| `DELETE` | `/subjects/{id}` | Delete a subject |

### Exams

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/exams/subjects/{subjectId}` | Create an exam for a subject |
| `GET` | `/exams` | List all exams |
| `GET` | `/exams/{id}` | Get exam by ID |
| `DELETE` | `/exams/{id}` | Delete an exam |

### Error Response Format

All errors return a consistent JSON body:

```json
{
  "status": 404,
  "message": "Student with id: 99 not found",
  "timestamp": "2026-02-28T10:30:00"
}
```

| HTTP Status | Scenario |
|---|---|
| `400 Bad Request` | Validation failure or subject not enrolled before exam registration |
| `404 Not Found` | Requested resource does not exist |
| `409 Conflict` | Duplicate subject or exam enrollment |
| `500 Internal Server Error` | Unhandled exception |

---

## Running Tests

```bash
# Run all unit tests
./gradlew test

# Run tests and generate a report
./gradlew test jacocoTestReport
```

Test reports are written to `build/reports/tests/test/index.html`.

---

## ER-Diagram

Use below erDiagram code in [mermaid.live](https://mermaid.live/edit) website to see erDiagram.
```
erDiagram
    STUDENT {
        string registration_id PK "Student Registration ID (unique)"
        string name "Student Name"
    }

    SUBJECT {
        string subject_id PK "Subject ID (unique)"
        string name "Subject Name"
    }

    EXAM {
        string exam_id PK "Exam ID (unique)"
        string subject_id FK "FK -> SUBJECT.subject_id"
    }

    STUDENT_SUBJECT_ENROLLMENT {
        string registration_id FK "FK -> STUDENT.registration_id"
        string subject_id FK "FK -> SUBJECT.subject_id"
        string ATTRIBUTE_WORD PK "composite(registration_id, subject_id)"
    }

    STUDENT_EXAM_REGISTRATION {
        string registration_id FK "FK -> STUDENT.registration_id"
        string exam_id FK "FK -> EXAM.exam_id"
        string ATTRIBUTE_WORD PK "composite(registration_id, exam_id)"
    }

    %% Core relationships
    SUBJECT ||--o{ EXAM : "has exams (1 subject -> many exams)"

    %% Many-to-many: Students <-> Subjects (Enrollment)
    STUDENT ||--o{ STUDENT_SUBJECT_ENROLLMENT : enrolls
    SUBJECT  ||--o{ STUDENT_SUBJECT_ENROLLMENT : has_enrolled_students

    %% Many-to-many: Students <-> Exams (Registration)
    STUDENT ||--o{ STUDENT_EXAM_REGISTRATION : registers
    EXAM     ||--o{ STUDENT_EXAM_REGISTRATION : has_registered_students
```

---

## Author

**Rohit Kapare**
- GitHub: [@RohitKapare](https://github.com/RohitKapare)