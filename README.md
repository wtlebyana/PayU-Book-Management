# 📚 Bookstore Management System

A multi-tiered module Spring Boot application for managing a bookstore. It includes:
- A **Management Service** for CRUD operations on books (REST API).
- A **Web Service** with a Thymeleaf UI that interacts with the management service via HTTP.
- A **Shared Module** containing common DTOs and enums (like `BookType`) reused across both services.

## ⚙️ Tech Stack

- Java 1.8
- Spring Boot 2.7.18
- Maven
- Spring MVC / Spring Data JPA
- H2 (In-Memory DB)
- Thymeleaf (for UI)
- Jersey Client (for REST calls)
- Lombok

---

## 🧪 Running the Application Locally
1. Clone the Repository
- git clone https://github.com/wtlebyana/PayU-Book-Management.git
- cd bookstore-management

## 🔧 How to Build

From the root project directory (`bookstore-management`):

```bash

mvn clean install

```
## How to run
---
## 1.Run the Management Service
- cd management-service
- mvn spring-boot:run

- The service will start on: http://localhost:8081

## 2.  Run the Web Service
- On a new terminal:
- cd web-service
- mvn spring-boot:run

## The web UI will be available at:
- http://localhost:8080/books


## ✏️ Features
- Add, view, update, delete books
- ISBNs are unique and 13-digit validated
- Enum BookType is used for classifying books:
- HARD_COVER, SOFT_COVER, EBOOK
- Thymeleaf UI includes:
- Form validations
- Enum dropdown selection

## Sample Data (H2 Console)
- Access the H2 console for debugging:
- http://localhost:8081/h2-console