# Feature Flag Service

A lightweight multi-tenant **Feature Flag Service** built using **Spring Boot**. The service allows multiple tenants to manage feature flags independently while ensuring complete tenant isolation.

---

# Overview

This service provides REST APIs to:

- Create, update, retrieve and delete feature flags
- Evaluate whether a feature is enabled for a given user
- Support stable percentage rollouts
- Support targeted users
- Prevent concurrent update conflicts using optimistic locking
- Ensure strict tenant isolation
- Demonstrate clean layered architecture and extensible design

---

# Tech Stack

- Java 21
- Spring Boot 3
- Spring Data JPA
- H2 In-Memory Database
- Maven
- JUnit 5
- Mockito
- Lombok

---

# Project Structure

```text
src
├── client
├── controller
├── service
├── repository
├── entity
├── DTO
├── strategy
├── exception
└── FeatureFlagApplication
```

---

# Architecture

The project follows a layered architecture.

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

---

# Architecture Highlights

- Multi-tenant data isolation enforced at the repository layer.
- Layered architecture with clear separation of concerns.
- DTO-based request and response models.
- Strategy Pattern for feature evaluation.
- Repository Pattern for persistence.
- Optimistic locking using JPA `@Version`.
- RESTful APIs using `ResponseEntity`.
- Bean Validation for request validation.

---

# Design Patterns Used

- Repository Pattern
- Strategy Pattern
- DTO Pattern
- Service Layer Pattern

The evaluation logic is delegated to an `EvaluationStrategy`, making it easy to introduce future evaluation mechanisms such as role-based targeting, country-based targeting, scheduled releases, or A/B testing without changing the service layer.

---

# Core Functionalities

## Feature Flag CRUD

- Create Feature Flag
- Retrieve Feature Flags
- Update Feature Flag
- Delete Feature Flag

---

## Feature Evaluation

Evaluate whether a feature is enabled for a user using:

- Global enable/disable switch
- Stable percentage rollout (deterministic hash-based bucketing)
- Targeted users
- Safe default fallback

---

## Multi-Tenancy

Every request contains an:

```
X-Tenant-ID
```

header.

Each tenant can only access and evaluate its own feature flags.

---

## Concurrency

The service uses **Optimistic Locking** (`@Version`) to prevent lost updates.

If two administrators update the same feature flag simultaneously:

- First update succeeds
- Second update detects the version mismatch
- API returns **HTTP 409 Conflict**

---

# API Endpoints

## 1. Create Feature Flag

### POST

```
POST /flags
```

### Headers

```
X-Tenant-ID: tenant1
Content-Type: application/json
```

### Request

```json
{
    "name": "NEW_UI",
    "enabled": true,
    "rolloutPercentage": 25,
    "targetedUsers": [
        "john",
        "alice"
    ],
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
    "rolloutPercentage": 25,
    "targetedUsers": [
        "john",
        "alice"
    ],
    "defaultValue": false,
    "version": 0
}
```

---

## 2. Get All Feature Flags

### GET

```
GET /flags
```

### Headers

```
X-Tenant-ID: tenant1
```

### Response

```json
[
    {
        "id": 1,
        "name": "NEW_UI",
        "enabled": true,
        "rolloutPercentage": 25,
        "targetedUsers": [
            "john",
            "alice"
        ],
        "defaultValue": false,
        "version": 0
    }
]
```

---

## 3. Update Feature Flag

### PUT

```
PUT /flags/{id}
```

### Headers

```
X-Tenant-ID: tenant1
Content-Type: application/json
```

### Request

```json
{
    "name": "NEW_UI",
    "enabled": true,
    "rolloutPercentage": 50,
    "targetedUsers": [
        "john",
        "alice",
        "bob"
    ],
    "defaultValue": false,
    "version": 0
}
```

### Response

**200 OK**

```json
{
    "id": 1,
    "name": "NEW_UI",
    "enabled": true,
    "rolloutPercentage": 50,
    "targetedUsers": [
        "john",
        "alice",
        "bob"
    ],
    "defaultValue": false,
    "version": 1
}
```

---

## 4. Delete Feature Flag

### DELETE

```
DELETE /flags/{id}
```

### Headers

```
X-Tenant-ID: tenant1
```

### Response

```
204 No Content
```

---

## 5. Evaluate Feature Flag

### GET

```
GET /flags/eval?flag=NEW_UI&user=john
```

### Headers

```
X-Tenant-ID: tenant1
```

### Response

```json
{
    "flag": "NEW_UI",
    "user": "john",
    "enabled": true
}
```

---

# Evaluation Flow

Feature evaluation follows this order:

1. If the feature is globally disabled, return the configured default value.
2. If the user exists in the targeted user list, enable the feature.
3. Otherwise, evaluate the user using a stable hash-based percentage rollout.
4. Users outside the rollout receive the configured default value.

---

# Running the Application

## Prerequisites

- Java 21
- Maven 3.9+

---

## Clone Repository

```bash
git clone https://github.com/PrabhavGupta5/PaytmFlagAssessment.git
cd PaytmFlagAssessment
```

---

## Build

```bash
mvn clean install
```

---

## Run

```bash
mvn spring-boot:run
```

Application starts on:

```
http://localhost:8080
```

---

# Running Tests

Execute:

```bash
mvn test
```

---

# Testing with Postman

Use this header for every request.

```
X-Tenant-ID: tenant1
```

Suggested workflow:

1. Create a feature flag.
2. Retrieve all feature flags.
3. Evaluate for a targeted user.
4. Evaluate for a non-targeted user (percentage rollout).
5. Update the rollout percentage.
6. Disable the feature and verify the default value.
7. Delete the feature.
8. Repeat using another tenant to verify tenant isolation.

---

# Assumptions

- Tenant identity is provided through the `X-Tenant-ID` request header.
- Feature flag names are unique within a tenant.
- Different tenants may use the same feature flag name.
- Stable percentage rollout uses deterministic hashing, ensuring the same user always receives the same evaluation result.
- Targeted users always receive the feature when it is globally enabled.
- If the feature is globally disabled, the configured default value is returned.

---

# Future Enhancements

- Role and attribute-based targeting
- Scheduled feature activation and expiry
- Audit history for feature flag updates
- Authentication and authorization using Spring Security + JWT
- Redis caching for feature evaluations
- OpenAPI / Swagger documentation
- Metrics and monitoring
- Admin UI for feature flag management

---

# Author

**Prabhav Gupta**
