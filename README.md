# PassArena

A Spring Boot **microservices** backend built around centralized configuration, service discovery, an API gateway, and JWT-based authentication.

> **Status:** work in progress. `auth-service`, `user-service` and `movie-service` are implemented and their endpoints have been tested. The gateway routes to `auth-service` and `user-service` (with a circuit breaker on the latter); a movie route and active JWT enforcement are still pending.

## Architecture

```
                 ┌──────────────┐
   Client ─────▶ │ api-gateway  │ :8080
                 └──────┬───────┘
                        │ lb://  (Eureka lookup)
          ┌─────────────┼──────────────┐
          ▼                            ▼
   ┌──────────────┐  Feign/HTTP  ┌──────────────┐     ┌───────────────┐
   │ auth-service │ ───────────▶ │ user-service │     │ movie-service │
   │    :8083     │              │    :8084     │     │     :9091     │
   │   (MySQL)    │              │ (PostgreSQL) │     │    (MySQL)    │
   └──────────────┘              └──────────────┘     └───────────────┘

   config-server :8888  ──▶ serves shared config to all services
   service-discovery :8761 (Eureka) ◀── all services register here
```

| Module | Port | Responsibility |
|---|---|---|
| `service-discovery` | 8761 | Eureka server; service registry |
| `config-server` | 8888 | Spring Cloud Config (native profile) serving files from `application properties/` |
| `api-gateway` | 8080 | Spring Cloud Gateway (WebFlux); routing, retry with backoff, Resilience4j circuit breaker, fallback endpoints, JWT validation helpers (not yet active) |
| `auth-service` | 8083 | Signup, login, JWT access tokens, refresh-token rotation, logout; MySQL |
| `user-service` | 8084 | User profile management; PostgreSQL |
| `movie-service` | 9091 | Movie catalog CRUD, search and status filtering; MySQL |

## Tech stack

- Java 21, Spring Boot, Maven (wrapper included in each module)
- Spring Cloud: Config, Netflix Eureka, Gateway, LoadBalancer, Resilience4j
- Spring Security, JJWT
- Spring Data JPA, MySQL (auth, movies), PostgreSQL (users)
- Bean Validation, Caffeine cache, Lombok

## API

Requests go through the gateway. `/auth-service/**` and `/user-service/**` are rewritten (prefix stripped) and forwarded to the matching service. The `user-service` route is wrapped in a circuit breaker that falls back to `/fallback/user-service`.

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
| DELETE | `/users/{userId}` | Delete a user (`204`) |

### Movies (`/movies`)

Served by `movie-service` (port 9091). Not yet routed through the gateway.

| Method | Path | Description |
|---|---|---|
| POST | `/movies` | Create a movie |
| GET | `/movies/{movieId}` | Get movie by UUID |
| GET | `/movies/status/{status}` | List movies by status (`UPCOMING`, `NOW_SHOWING`, `ENDED`) |
| GET | `/movies/search?title=` | Search movies by title |
| PUT | `/movies/{movieId}` | Update a movie |
| DELETE | `/movies/{movieId}` | Delete a movie |

## Getting started

### Prerequisites

- JDK 21
- MySQL (auth database) and PostgreSQL (user database)

### Configuration

1. **Config server path.** `config-server/src/main/resources/application.yaml` points `search-locations` at an absolute local path. Change it to where you cloned this repo's `application properties/` folder.
2. **Environment variables.** Real credentials are never stored in the config files; they use `${...}` placeholders that each service resolves from its own environment. Set these (e.g. in the IntelliJ run configuration or as Windows user variables):

   | Service | Variables |
   |---|---|
   | `auth-service` | `AUTHDB_URL`, `AUTHDB_USERNAME`, `AUTHDB_PASSWORD`, `JWT_SECRET` |
   | `user-service` | `USERDB_URL`, `USERDB_USERNAME`, `USERDB_PASSWORD` |
   | `movie-service` | `MOVIEDB_URL`, `MOVIEDB_USERNAME`, `MOVIEDB_PASSWORD` |

3. **JWT secret.** `jwt.secret` in `application properties/application.yml` should be `${JWT_SECRET}`. Generate a strong random value and set it as an environment variable in every service that uses it (`auth-service` today, `api-gateway` once it validates tokens).
   ```powershell
   [Convert]::ToBase64String((1..48 | ForEach-Object { Get-Random -Maximum 256 }))
   ```
   
4. **Keep secrets out of git.** `.idea/workspace.xml` (where IntelliJ stores run-configuration env vars) is ignored by `.idea/.gitignore`. Never `git add -f` it.
5. Create the tables with the SQL scripts in each service:
   - `auth-service/src/main/resources/db/migration/` (MySQL)
   - `user-service/src/main/resources/db/create_users_table.sql` (PostgreSQL)
   - `movie-service/src/main/resources/db/create_movies_table.sql` (MySQL)

   `user-service` and `movie-service` also run Hibernate with `ddl-auto: update`, so their scripts mainly document and pin the schema.

### Run order

Start each module from its own directory, in this order:

```bash
cd service-discovery && ./mvnw spring-boot:run   # 1. Eureka   (8761)
cd config-server     && ./mvnw spring-boot:run   # 2. Config   (8888)
cd auth-service      && ./mvnw spring-boot:run   # 3. Auth     (8083)
cd user-service      && ./mvnw spring-boot:run   # 4. Users    (8084)
cd movie-service     && ./mvnw spring-boot:run   # 5. Movies   (9091)
cd api-gateway       && ./mvnw spring-boot:run   # 6. Gateway  (8080)
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
├── movie-service/
├── service-discovery/
└── user-service/
```

## Roadmap

- [x] Test `user-service` and `movie-service` endpoints
- [x] SQL table scripts for `user-service` and `movie-service`
- [x] Gateway route and circuit breaker for `user-service`
- [ ] Activate JWT validation and `X-User-Id` propagation in the gateway (`JwtAuthenticationFilter` exists but isn't registered as a `WebFilter`/`GlobalFilter`; also remove the debug `System.out` secret logging in `JwtService`)
- [ ] Gateway route for `movie-service` (fallback endpoint already exists)
- [ ] Move `jwt.secret` to `${JWT_SECRET}` and rotate the committed value
- [ ] Add a root `.gitignore`
- [ ] Input validation on signup/login
- [ ] Docker Compose setup
- [ ] Automated tests
