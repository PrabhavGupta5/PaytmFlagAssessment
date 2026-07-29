# Feature Flag Service – Design Decisions

## 1. What did I ask the AI to do, and what did I write or decide myself?

I used AI primarily as a productivity tool to generate the initial Spring Boot project structure, boilerplate code, DTOs, controller skeletons, repository interfaces, exception handling, and unit test templates. AI was also used to explore different architectural approaches, design patterns, and feature flag evaluation strategies.

The final design decisions, REST API structure, multi-tenant approach, repository methods, service layer, evaluation flow, optimistic locking implementation, testing strategy, and overall project architecture were reviewed, modified, and finalized by me. Every AI-generated code snippet was validated, refactored where required, and integrated only after understanding its behavior and ensuring it aligned with the assignment requirements.

---

# 2. Where did I override, correct, or discard the AI's output?

## ResponseEntity instead of returning DTOs directly

The initial implementation returned DTOs directly from the controller.

I changed the controller to return `ResponseEntity` so that the API explicitly returns appropriate HTTP status codes.

This allows the service to return:

- **201 Created** for resource creation
- **200 OK** for successful reads and updates
- **204 No Content** for delete operations
- **409 Conflict** for optimistic locking failures

This makes the REST API more expressive and aligns with Spring Boot best practices.

---

## Simplified Feature Flag Model

One AI-generated suggestion modelled a feature flag using an enum (`ON`, `OFF`, `DEFAULT`) together with a default value.

```java
public class Flag {

    private String name;

    private FlagState state;

    private boolean defaultValue;
}
```

Although flexible, this introduced additional state transitions and evaluation logic that were unnecessary for the assignment.

Instead, I modelled the feature flag using:

- `enabled`
- `defaultValue`
- `rolloutPercentage`
- `targetedUsers`

This keeps the model simple while supporting realistic feature evaluation.

---

## Removed Unnecessary Factory Pattern

An earlier design introduced a Factory pattern for selecting evaluation strategies.

Since the project currently contains only one evaluation strategy, I removed the Factory to avoid unnecessary abstraction.

Instead, I kept a single `EvaluationStrategy` implementation.

If additional evaluation mechanisms are required later (role-based targeting, country-based rules, A/B testing, scheduled rollout), they can be introduced without changing the service layer.

---

## DTO Mapping Instead of Manual Builders

Initially, the service layer manually converted entities into response DTOs using builders.

As the project evolved, I moved the mapping logic into the DTO classes by introducing:

- `FlagRequest.toEntity()`
- `FlagResponse.fromEntity()`

This removed repetitive mapping code from the service layer and allowed the service to focus purely on business logic.

The service now validates business rules and orchestrates persistence rather than constructing objects manually.

---

## Tenant Isolation at the Repository Layer

Rather than filtering tenants only inside the service layer, I designed the repository so that every database query explicitly includes the tenant identifier.

Examples include:

- `findByTenantIdAndName()`
- `findByIdAndTenantId()`
- `findAllByTenantId()`

This ensures tenant isolation is enforced at the persistence layer itself and reduces the possibility of accidentally exposing another tenant's data.

---

## Feature Evaluation Strategy

Instead of evaluating feature flags directly inside the service, I introduced the Strategy pattern.

The service delegates evaluation to `EvaluationStrategy`, which currently performs:

- Global enable/disable evaluation
- Targeted user evaluation
- Stable percentage rollout
- Safe default fallback

This separates business orchestration from evaluation logic and keeps the implementation extensible.

---

## Optimistic Locking

To prevent concurrent administrators from accidentally overwriting each other's updates, I implemented optimistic locking using JPA's `@Version` annotation.

Each feature flag maintains a version number that is automatically incremented whenever the entity is updated.

If two administrators edit the same flag simultaneously:

- the first update succeeds
- the second update detects the version mismatch
- the API returns **HTTP 409 Conflict**

This prevents lost updates while avoiding pessimistic database locks.

---

# 3. Biggest Trade-offs

## 1. H2 Database instead of PostgreSQL

I chose H2 because it allows the project to run immediately without any external database setup.

This makes it easier for reviewers to clone the repository, run the application, and execute the tests without additional configuration.

---

## 2. Lightweight Feature Evaluation

Instead of implementing a complete rule engine similar to LaunchDarkly or Unleash, I implemented the core evaluation capabilities required for the assignment:

- Global enable/disable
- Stable percentage rollout
- Targeted users
- Safe default value

This keeps the implementation concise while still demonstrating realistic feature flag behaviour.

The Strategy pattern allows more advanced evaluation mechanisms to be added later.

---

## 3. Header-based Tenant Identification

Instead of implementing authentication or authorization, I used the `X-Tenant-ID` request header to identify tenants.

This keeps the project focused on feature flag functionality while still demonstrating proper tenant isolation.

---

## 4. No Authentication/Authorization

Authentication and authorization were intentionally omitted because they were outside the assignment scope.

The architecture allows Spring Security with JWT or OAuth2 to be integrated later without significant refactoring.

---

# 4. Validation Strategy

Validation is performed at multiple layers.

### Request Validation

Bean Validation annotations validate incoming requests.

Examples include:

- `@NotBlank`
- `@Min`
- `@Max`

---

### Business Validation

The service layer enforces business rules such as:

- preventing duplicate flag names within a tenant
- validating entity existence before updates or deletion

---

### Database Validation

The database also enforces integrity using a unique constraint on:

```
tenant_id + name
```

This acts as a final safety net to prevent duplicate feature flags.

---

# 5. What would I do with another day?

Given more time, I would extend the project with:

- User attribute-based targeting (country, role, environment)
- Scheduled feature activation and expiry
- Audit history for feature flag changes
- Authentication and authorization using Spring Security with JWT
- OpenAPI / Swagger documentation
- Redis caching for frequently evaluated feature flags
- Metrics and monitoring for feature evaluations
- A simple admin UI for managing feature flags

---

# 6. Summary

The project focuses on delivering a clean, maintainable, and extensible implementation rather than adding unnecessary complexity.

The final solution demonstrates:

- Multi-tenant feature management
- Layered architecture
- Strategy Pattern for feature evaluation
- DTO-based entity mapping
- Optimistic locking for concurrent updates
- Stable percentage rollout
- Targeted user evaluation
- Repository-level tenant isolation
- Proper RESTful API design using `ResponseEntity`
- Validation across API, service, and persistence layers

While intentionally lightweight, the architecture allows additional feature flag capabilities to be introduced with minimal changes to the existing codebase.