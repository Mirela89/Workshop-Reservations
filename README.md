# Workshop Reservations System

## Overview  
The application enables organizers to create and manage workshops, while users can view available events and make reservations. It provides a centralized and reliable solution for handling workshop scheduling, participant registration, and capacity constraints.

## Technologies & Frameworks

- **Programming Language:** Java 17  
- **Framework:** Spring Boot 3.x  
  - Spring Web (REST APIs)
  - Spring Data JPA (persistence)
  - Spring Validation (input validation)
- **ORM:** Hibernate (JPA provider)  
- **Database:** MySQL  
- **Build Tool:** Maven  
- **API Documentation:** Swagger / OpenAPI (springdoc-openapi)  
- **Testing:** JUnit 5, Mockito  
- **Utilities:** Lombok

## System Architecture

The application is built using a **layered, MVC-inspired architecture**, which ensures a clear separation of responsibilities and improves maintainability and testability.

- **Controllers** expose REST endpoints and handle incoming HTTP requests.
- **Services** implement the business logic and enforce application rules such as capacity limits, validation, and cancellation behavior.
- **Repositories** manage data persistence using Spring Data JPA, with one repository defined per entity.
- **DTOs and Mappers** control the flow of data between layers and convert entities to API-friendly representations.
- **Entities** define the persistence model and are mapped to database tables using JPA.

## Implemented Functionalities

| Module | Functionalities |
|------|-----------------|
| **Workshop Management** | <ul><li>Create, update, cancel, and delete workshops</li><li>Associate workshops with categories, locations, and organizers</li><li>Display available and reserved seats dynamically</li></ul> |
| **Category Management** | <ul><li>Create and list workshop categories</li><li>Associate workshops with categories</li></ul> |
| **Location Management** | <ul><li>Create and manage physical locations</li><li>Associate workshops with locations</li></ul> |
| **Organizer Management** | <ul><li>Create and manage organizers</li><li>View organizers and their related workshops</li></ul> |
| **Reservation Management** | <ul><li>Create reservations for workshops</li><li>Prevent overbooking beyond workshop capacity</li><li>Cancel reservations</li><li>Automatically update available seats</li></ul> |
| **Participant Management** | <ul><li>Manage participants associated with reservations</li><li>View participants per reservation/workshop</li></ul> |
| **User Management** | <ul><li>Create users</li><li>Retrieve users (by ID)</li></ul> |

## API Documentation

All REST endpoints are documented using **Swagger / OpenAPI**.  
After starting the application, the Swagger UI can be accessed at: http://localhost:8081/swagger

## Entity-Relationship Diagram (ERD)

The application persists data in a **MySQL database** using JPA/Hibernate.
The database schema was designed using **dbdiagram.io**.

<img width="1171" height="703" alt="PWJ" src="https://github.com/user-attachments/assets/31f76671-2ffc-4067-969d-186ae00b30f9" />

## Unit Testing

The application includes **unit tests for both service and controller layers**.

- **Service tests** validate business logic using JUnit 5 and Mockito.
- **Controller tests** validate REST endpoints using MockMvc.
- External dependencies such as repositories are mocked to ensure isolated and fast tests.
