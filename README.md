# user-service

REST microservice for user management with JWT authentication. Handles registration, sign-in, and CRUD operations on users. Designed as a standalone service (part of a shop backend).

## Tech Stack

- Java 17, Spring Boot 3.2, Spring Security, Spring Data JPA
- PostgreSQL (prod), H2 (tests), Hibernate Validator, MapStruct
- JJWT 0.11.5 (HS256), Logback, Docker Compose
- JUnit 5, Mockito, `@DataJpaTest`

## Notable Implementation Details

- **AOP-based structured logging.** `LoggingAspect` uses three separate named loggers (`AUTH_LOGGER`, `USER_LOGGER`, `EXCEPTION_LOGGER`) wired to separate rolling file appenders in `logback.xml`. Auth events are captured with `@Around` on `AuthenticationServiceImpl`, service operations with `@AfterReturning` across all service impls - separating auth audit logs from general operation logs.
- **`UserValidator` as a Spring `Validator` registered via `@InitBinder`.** Duplicate username/email checks happen at the Spring MVC validation layer before the service is called, not inside the service itself. `AuthController.initBinder()` registers it only for the auth endpoints.
- **`JwtUserDetails` factory pattern.** `JwtUserFactory.create(User)` maps the JPA entity to a `JwtUserDetails` implementing `UserDetails`, keeping Spring Security types separate from the JPA entity. `JwtServiceImpl.generateToken()` checks `instanceof User` to embed custom claims (id, email, role) - though this checks the wrong type since the method receives `UserDetails`, not `User` directly.
- **`findAllUserRole()` filters in-memory.** `UserServiceImpl` loads all users via `findAll()` and streams with `filter(role == ROLE_USER)` instead of a JPQL query - pragmatic for small datasets but bypasses DB-level filtering.
- **H2 for `@DataJpaTest`.** Repository tests run against H2 in-memory with `@BeforeEach` clearing the table, covering save, update, findById, existsByUsername/Email, and delete scenarios without needing a real Postgres instance.
- **GitHub Actions CI** runs on `main`/`develop` push and `main` PRs - build with Maven, then tests as a separate step.

## Quick Start

```bash
docker-compose up
# app on http://localhost:8000
# postgres on localhost:5433

# Sign up
curl -X POST http://localhost:8000/api/v1/auth/sign-up \
  -H "Content-Type: application/json" \
  -d @json/user1.json

# Sign in
curl -X POST http://localhost:8000/api/v1/auth/sign-in \
  -H "Content-Type: application/json" \
  -d @json/logUser1.json

# Get all users (requires Bearer token)
curl http://localhost:8000/api/v1/users \
  -H "Authorization: Bearer <token>"
```

Tables are initialized from `src/main/resources/db/init/user.sql` via `docker-entrypoint-initdb.d`.
