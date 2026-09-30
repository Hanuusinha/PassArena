# PassArena

A Spring Boot **microservices** backend built around centralized configuration, service discovery, an API gateway, and JWT-based authentication.

> **Status:** work in progress. `auth-service` is complete; `user-service` is implemented but its API endpoints are not yet tested. The gateway only routes to `auth-service` so far.

## Architecture

```
                 ┌──────────────┐
   Client ─────▶ │ api-gateway  │ :8080
                 └──────┬───────┘
                        │ lb://  (Eureka lookup)
          ┌─────────────┼──────────────┐
          ▼                            ▼
   ┌──────────────┐  Feign/HTTP  ┌──────────────┐
   │ auth-service │ ───────────▶ │ user-service │
   │    :8083     │              │  (PostgreSQL)│
   │   (MySQL)    │              └──────────────┘
   └──────────────┘

   config-server :8888  ──▶ serves shared config to all services
   service-discovery :8761 (Eureka) ◀── all services register here
```

| Module | Port | Responsibility |
|---|---|---|
| `service-discovery` | 8761 | Eureka server; service registry |
| `config-server` | 8888 | Spring Cloud Config (native profile) serving files from `application properties/` |
| `api-gateway` | 8080 | Spring Cloud Gateway (WebFlux); routing, retry with backoff, Resilience4j circuit breaker |
| `auth-service` | 8083 | Signup, login, JWT access tokens, refresh-token rotation, logout; MySQL |
| `user-service` | – | User profile management; PostgreSQL |

## Tech stack

- Java 21, Spring Boot, Maven (wrapper included in each module)
- Spring Cloud: Config, Netflix Eureka, Gateway, LoadBalancer, Resilience4j
- Spring Security, JJWT
- Spring Data JPA, MySQL (auth), PostgreSQL (users)
- Caffeine cache, Lombok

## API

Requests go through the gateway. `/auth-service/**` is rewritten and forwarded to `auth-service`.

### Auth (`/auth`)

| Method | Path | Description |
|---|---|---|
| POST | `/auth/signup` | Register a user, returns tokens (`201`) |
| POST | `/auth/login` | Authenticate, returns access + refresh tokens |
| POST | `/auth/refresh` | Exchange a refresh token for new tokens |
| POST | `/auth/logout` | Invalidate a refresh token |

### Users (`/users`)

| Method | Path | Description |
|---|---|---|
| POST | `/users` | Create a user (used by auth-service on signup) |
| GET | `/users/me` | Current profile, identified by the `X-User-Id` header |
| GET | `/users/{userId}` | Get user by UUID |
| GET | `/users/email/{email}` | Get user by email |

## Getting started

### Prerequisites

- JDK 21
- MySQL (auth database) and PostgreSQL (user database)

### Configuration

1. **Config server path.** `config-server/src/main/resources/application.yaml` points `search-locations` at an absolute local path. Change it to where you cloned this repo's `application properties/` folder.
2. **Environment variables** for `auth-service`: `AUTHDB_URL`, `AUTHDB_USERNAME`, plus the database password as configured in `application properties/auth-service.yml`.
3. **JWT secret.** Set a strong secret in `application properties/application.yml` (`jwt.secret`). Don't commit real secrets; prefer environment variables or a secrets manager.
4. Create the tables with the scripts in `auth-service/src/main/resources/db/migration/`.

### Run order

Start each module from its own directory, in this order:

```bash
cd service-discovery && ./mvnw spring-boot:run   # 1. Eureka   (8761)
cd config-server     && ./mvnw spring-boot:run   # 2. Config   (8888)
cd auth-service      && ./mvnw spring-boot:run   # 3. Auth     (8083)
cd user-service      && ./mvnw spring-boot:run   # 4. Users
cd api-gateway       && ./mvnw spring-boot:run   # 5. Gateway  (8080)
```

On Windows use `mvnw.cmd` instead of `./mvnw`. Open <http://localhost:8761> to confirm the services have registered.

### Example

```bash
curl -X POST http://localhost:8080/auth-service/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"your-password"}'
```

Adjust the body fields to match `LoginRequest` in `auth-service`.

## Project structure

```
PassArena/
├── application properties/   # config served by config-server
├── api-gateway/
├── auth-service/
├── config-server/
├── service-discovery/
└── user-service/
```

## Roadmap

- [ ] Test `user-service` endpoints
- [ ] JWT validation and `X-User-Id` propagation in the gateway
- [ ] Gateway route for `user-service`
- [ ] Input validation on signup/login
- [ ] Docker Compose setup
- [ ] Automated tests
