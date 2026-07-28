# Feature Flag Service – Design Decisions

## 1. What did I ask the AI to do, and what did I write or decide myself?

I used AI to help generate boilerplate code such as the Spring Boot project structure, DTOs, controller skeletons, repository interfaces, exception handling, and unit test templates. AI was also used to explore different ways of modelling feature flags and structuring the application.

The overall architecture, REST API design, tenant isolation approach, repository methods, service layer, evaluation flow, testing strategy, and final code structure were reviewed and decided by me. Every generated piece of code was validated, modified where necessary, and integrated into the final solution only after understanding its behavior.

---

## 2. Where did I override, correct, or discard the AI's output?

I made several changes after reviewing AI-generated suggestions.

### ResponseEntity instead of returning DTOs directly

The initial implementation returned DTOs directly from the controller. I changed the controller to return `ResponseEntity` so that HTTP status codes are explicit and RESTful. This allowed the API to return:
- **201 Created** for resource creation
- **200 OK** for successful reads and updates
- **204 No Content** for delete operations

This provides better control over the HTTP response and follows common Spring Boot best practices.

### Simplified feature flag model

One AI-generated design suggested modelling feature flags using a `FlagState` enum (`ON`, `OFF`, `DEFAULT`) along with a `defaultValue` field.

```java
public class Flag {

    private String name;

    private FlagState state;

    private boolean defaultValue;

}
```

While this design is flexible, I decided not to use it because the assignment only requires evaluating whether a feature is enabled or disabled for a tenant. Introducing multiple states would have added unnecessary complexity and additional evaluation logic without providing value for the current requirements.

Instead, I modelled the entity using simple boolean fields:

- `enabled`
- `defaultValue`

This keeps the implementation straightforward while still allowing future enhancements if more evaluation states are needed.

### Removed unnecessary Factory pattern

An earlier AI-generated design introduced a Factory pattern to create evaluation strategies. Since there is currently only one evaluation strategy, I removed the Factory to avoid unnecessary abstraction.

Instead, I kept a single `EvaluationStrategy` implementation. This keeps the design simple while still making it easy to introduce additional strategies later, such as percentage rollout or user targeting, without changing the service layer.

### Tenant isolation at the repository level

Rather than filtering tenants only in the service layer, I designed repository methods to always include `tenantId` in every query, for example:

- `findByTenantIdAndName()`
- `findByIdAndTenantId()`
- `findAllByTenantId()`

This ensures tenant isolation is enforced at the data access layer itself, reducing the possibility of accidentally exposing another tenant's data.

---

## 3. Biggest trade-offs

### 1. H2 Database instead of PostgreSQL 

I chose H2 because it allows the project to run immediately without requiring external database setup. This makes it easier for reviewers to clone the repository, run the application, and execute the tests.

### 2. Simple evaluation strategy

The assignment only requires determining whether a feature is enabled or disabled for a user. I intentionally avoided implementing advanced capabilities such as percentage rollouts, user groups, or scheduled releases. These would increase complexity without improving the assignment requirements.

However, I introduced a Strategy pattern so that additional evaluation mechanisms can be added later without modifying the existing service implementation.

### 3. Header-based tenant identification

Instead of implementing authentication or authorization, I used the `X-Tenant-ID` request header to identify tenants. This keeps the project focused on the feature flag functionality while still demonstrating strict tenant isolation.

### 4. No authentication/authorization as of now
I did not implement authentication or authorization mechanisms, as the assignment does not require them. However, I designed the service to allow for easy integration of authentication and authorization in the future, such as using Spring Security with JWT tokens or OAuth2.


---

## 4. What would I do with another day?

Given more time, I would extend the project with:

- Percentage-based feature rollout
- User or group targeting
- Time-based feature activation
- Audit history for flag changes
- Authentication and authorization
- OpenAPI/Swagger documentation
- Improved structured error responses
- Integration tests covering additional edge cases
- Optimistic locking for concurrent updates
- Metrics and monitoring for evaluation requests