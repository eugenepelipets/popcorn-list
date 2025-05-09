# Popcorn List

**RESTful API service for managing movies and users, enabling:**

* CRUD operations for movies and users
* Movie ratings and popularity tracking
* Friend relationships and recommendations

---

## 📐 Architecture

```mermaid
flowchart LR
  Client[Client (Postman / Swagger UI / Frontend)] -->|HTTP requests| API[Spring Boot REST API]
  API -->|Calls| Service[Service Layer]
  Service -->|Transactions| Storage[Repository Layer (JdbcTemplate + SimpleJdbcInsert)]
  Storage -->|JDBC| DB[(H2 / PostgreSQL)]
  Service -->|Caching| Cache[Caffeine Cache (Genres, MPA)]
  API -->|Documentation| Swagger[OpenAPI (springdoc)]
```

**Component Layers:**

1. **Controller Layer**: Exposes endpoints (`/movies`, `/users`, `/genres`, `/mpa`).
2. **Service Layer**: Business logic, validation, transactions.
3. **Repository Layer**: JDBC-based storage implementations for Movies, Users, Genres, MPA.
4. **Database**: Development uses H2; production-ready with PostgreSQL.

---

## 🛠 Technologies & Patterns

* **Language & Framework**: Java 17, Spring Boot 3
* **Data Access**: Spring JDBC (`JdbcTemplate`, `SimpleJdbcInsert`)
* **Validation**: Jakarta Validation (`@NotBlank`, `@Size`, `@Past`, `@Positive`), custom exceptions
* **Error Handling**: Global `@RestControllerAdvice` (`ErrorHandler`) with custom exceptions (`NotFoundException`, `AlreadyExistException`, etc.)
* **Logging**: SLF4J + Logback (via Lombok `@Slf4j`)
* **DTO Mapping**: (to be added) using MapStruct or manual mappers
* **Transactions**: Spring `@Transactional` on service methods
* **Caching**: Spring Cache with Caffeine for static data
* **Testing**: JUnit 5, Mockito for unit tests; Testcontainers for integration tests
* **Documentation**: OpenAPI 3.0 via springdoc-openapi
* **Build & CI**: Maven, JaCoCo, Checkstyle, GitHub Actions
* **Containerization**: Docker, Docker Compose (PostgreSQL + Adminer)

---

## 🚀 Getting Started

### Prerequisites

* Java 17+
* Maven 3.8+
* Docker & Docker Compose (optional for PostgreSQL)

### Local Setup

1. **Clone the repo**:

   ```bash
   git clone https://github.com/eugenepelipets/popcorn-list.git
   cd popcorn-list
   ```
2. **Build and run** with H2 (default):

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
3. **Access the API** at: `http://localhost:8080`
4. **Swagger UI** at: `http://localhost:8080/swagger-ui.html`

### Using Docker Compose

```bash
docker-compose up --build
```

Services:

* **app**: Popcorn List API
* **db**: PostgreSQL
* **adminer**: Database GUI at `http://localhost:8080/adminer`

---

## 📚 API Endpoints

### Movies

| Method | Endpoint                     | Description              |
| ------ | ---------------------------- | ------------------------ |
| GET    | `/movies`                    | List all movies          |
| GET    | `/movies/{id}`               | Get movie by ID          |
| POST   | `/movies`                    | Create new movie         |
| PUT    | `/movies`                    | Update movie             |
| PUT    | `/movies/{id}/like/{userId}` | Add like to a movie      |
| DELETE | `/movies/{id}/like/{userId}` | Remove like from a movie |
| GET    | `/movies/popular?count=n`    | Top *n* popular movies   |

**Example: Create Movie**

```bash
curl -X POST http://localhost:8080/movies \
  -H 'Content-Type: application/json' \
  -d '{
    "name":"Con Air",
    "description":"A group of dangerous convicts hijacks the plane transporting them, and a former US Ranger tries to stop them.",
    "releaseDate":"1997-06-06",
    "duration":115,
    "mpa":{"id":4},
    "genres":[{"id":1},{"id":2}]
  }'
```

### Users

| Method | Endpoint                             | Description         |
| ------ | ------------------------------------ | ------------------- |
| GET    | `/users`                             | List all users      |
| GET    | `/users/{id}`                        | Get user by ID      |
| POST   | `/users`                             | Create new user     |
| PUT    | `/users`                             | Update user         |
| PUT    | `/users/{id}/friends/{friendId}`     | Add friend          |
| DELETE | `/users/{id}/friends/{friendId}`     | Remove friend       |
| GET    | `/users/{id}/friends`                | List user's friends |
| GET    | `/users/{id}/friends/common/{other}` | Get common friends  |

---

## 🧪 Testing

* **Unit tests**: `mvn test` (JUnit 5 + Mockito)
* **Integration tests**: Spring Boot Test + TestRestTemplate + Testcontainers
* **Coverage**: JaCoCo report available after `mvn verify`

---

## ⚙️ CI/CD

**GitHub Actions workflow:**

1. **build**: compile, run tests, generate coverage report
2. **lint**: Checkstyle, SpotBugs
3. **publish**: build and push Docker image

---

## 🐳 Docker

* **Dockerfile** for the application
* **docker-compose.yml** to spin up API + PostgreSQL + Adminer

---

## 🤝 Contributing

Contributions are welcome! Please open an issue or pull request.

---

## 📝 License

This project is licensed under the MIT License.
