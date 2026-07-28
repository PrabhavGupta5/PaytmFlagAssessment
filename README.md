# Feature Flag Service

A lightweight multi-tenant Feature Flag Service built using **Spring Boot**. The service allows multiple projects (tenants) to manage feature flags independently while ensuring complete tenant isolation.

---

## Overview

This service provides REST APIs to:

- Create, update, retrieve and delete feature flags
- Evaluate whether a feature is enabled for a given user
- Support multiple tenants with strict data isolation
- Demonstrate clean layered architecture and extensible design

---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- H2 In-Memory Database
- Maven
- JUnit 5
- Mockito
- Lombok

---

## Project Structure

```text
src
├── controller
├── service
├── repository
├── entity
├── dto
├── strategy
├── exception
└── FeatureFlagApplication
```

---

## Design

The project follows a layered architecture:

```text
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
H2 Database
```

### Design Patterns Used

- Repository Pattern
- Strategy Pattern (for feature evaluation)
- DTO Pattern
- Service Layer Pattern

The evaluation logic is implemented using the Strategy Pattern, making it easy to introduce future evaluation mechanisms such as percentage rollouts or user-based targeting without modifying the existing service layer.

---

## Core Functionalities

### Feature Flag CRUD

- Create Feature Flag
- Retrieve Feature Flags for a Tenant
- Update Feature Flag
- Delete Feature Flag

### Feature Evaluation

Evaluate whether a feature is enabled for a specific user.

### Multi-Tenancy

Each request contains an `X-Tenant-ID` header.

Feature flags are isolated per tenant, ensuring one tenant cannot access or evaluate another tenant's data.

---

# API Endpoints

## 1. Create Feature Flag

**POST** `/flags`

### Headers

```
X-Tenant-ID: tenant1
Content-Type: application/json
```

### Request Body

```json
{
    "name": "NEW_UI",
    "enabled": true,
    "defaultValue": false
}
```

### Response

**201 Created**

```json
{
    "id": 1,
    "name": "NEW_UI",
    "enabled": true,
    "defaultValue": false
}
```

---

## 2. Get All Feature Flags

**GET** `/flags`

### Headers

```
X-Tenant-ID: tenant1
```

### Response

**200 OK**

```json
[
  {
    "id":1,
    "name":"NEW_UI",
    "enabled":true,
    "defaultValue":false
  }
]
```

---

## 3. Update Feature Flag

**PUT** `/flags/{id}`

### Headers

```
X-Tenant-ID: tenant1
Content-Type: application/json
```

### Request Body

```json
{
    "name":"NEW_UI",
    "enabled":false,
    "defaultValue":false
}
```

### Response

**200 OK**

---

## 4. Delete Feature Flag

**DELETE** `/flags/{id}`

### Headers

```
X-Tenant-ID: tenant1
```

### Response

**204 No Content**

---

## 5. Evaluate Feature Flag

**GET** `/flags/eval?flag=NEW_UI&user=john`

### Headers

```
X-Tenant-ID: tenant1
```

### Response

**200 OK**

```json
{
    "flag":"NEW_UI",
    "user":"john",
    "enabled":true
}
```

---

# Running the Application

## Prerequisites

- Java 21
- Maven 3.9+

## Clone the Repository

```bash
git clone https://github.com/PrabhavGupta5/PaytmFlagAssessment.git
cd feature-flag-service
```

## Build the Project

```bash
mvn clean install
```

## Run the Application

```bash
mvn spring-boot:run
```

The application will start on:

```
http://localhost:8080
```

---

# Running Tests

Execute all unit tests using:

```bash
mvn test
```

---

# Testing with Postman

Use the following request header for every API:

```
X-Tenant-ID: tenant1
```

Example workflow:

1. Create a feature flag
2. Retrieve all feature flags
3. Update the feature flag
4. Evaluate the feature flag
5. Delete the feature flag

To verify tenant isolation, repeat the same requests using a different tenant ID (for example, `tenant2`) and confirm that each tenant only sees its own feature flags.

---

# Assumptions

- Tenant identity is provided through the `X-Tenant-ID` request header.
- Feature flag names are unique within a tenant.
- Different tenants can have feature flags with the same name.
- Evaluation returns the current enabled state of the feature for the specified tenant.

---

# Future Enhancements

- Percentage-based rollout
- User or group targeting
- Scheduled feature activation
- Authentication and authorization
- Audit history
- OpenAPI / Swagger documentation
- Caching for feature evaluations

---

# Author

Prabhav Gupta
