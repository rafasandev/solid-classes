# Copilot Instructions - UniMarket Solid Classes

## Architecture Overview

This is a **Spring Boot 3.5.7 marketplace application** (uni_market) using **Java 25**, PostgreSQL, and JWT authentication. The codebase follows **Hexagonal Architecture** (Ports & Adapters) with SOLID principles.

### Core Architectural Patterns

**Domain Module Structure**: Each domain (product, cart, user, profile, etc.) in `src/main/java/com/example/solid_classes/core/` follows this consistent layout:
```
domain/
├── controller/      # REST endpoints
├── dto/            # Request/Response DTOs
├── mapper/         # Entity ↔ DTO conversion (Spring @Component)
├── model/          # JPA entities extending AuditableEntity
├── ports/          # Interface extending NamedCrudPort<T>
├── repository/     # JpaRepository interfaces
└── service/
    ├── DomainAdapter.java        # Implements port, extends NamedCrudAdapter
    ├── DomainService.java        # Business logic orchestration
    └── Register[Domain]UseCase.java  # @Transactional use cases
```

**Base Abstractions**: 
- All entities extend `AuditableEntity` (provides UUID id, createdAt, updatedAt with JPA auditing)
- All ports extend `NamedCrudPort<T>` (defines getById, save, deleteById, throwEntityNotFound)
- All adapters extend `NamedCrudAdapter<T, R>` (implements port with custom entity name for error messages)

**Use Case Pattern**: Complex operations use dedicated `Register[Entity]UseCase` classes with `@Transactional` annotation. These orchestrate multiple services, fetch dependencies, use mappers, and return DTOs. Example: `RegisterProductUseCase` coordinates `ProductService`, `CategoryService`, `CompanyProfileService`, and `ProductMapper`.

**Profile Inheritance**: Uses JPA `@MappedSuperclass` for `ProfileEntity` with two concrete implementations:
- `CompanyProfile` (company_profiles table) - has products, services, variationCategories
- `IndividualProfile` (individual_profiles table) - has cart
Both share `@MapsId` relationship with User entity.

**Bidirectional Relationship Management**: Entities like `Product`, `Category`, `CompanyProfile` implement explicit add/remove methods to maintain both sides of relationships (e.g., `Product.setCategory()` updates both product.category and category's product list).

## Development Environment

**Database**: PostgreSQL on port 5433 via Docker Compose
- Start: `docker-compose up -d`
- DB: solid_db, user: admin, pass: admin
- PgAdmin available at http://localhost:5051

**Build & Run**:
- Maven wrapper: `./mvnw spring-boot:run`
- App runs on port 8081
- JPA DDL: `create` (recreates schema on startup)

**Security**:
- JWT auth configured in `SecurityConfiguration` with stateless sessions
- Public endpoints: `/auth/**`, `/profile/**`
- JWT secret/expiration in `application.yaml` under `security.jwt.*`
- Uses `JwtAuthFilter` before `UsernamePasswordAuthenticationFilter`

## Code Conventions

**Lombok**: Use `@Getter`, `@RequiredArgsConstructor`, `@SuperBuilder`, `@NoArgsConstructor` on entities/DTOs. Avoid `@Data` and `@Setter` (prefer immutability).

**Entity Construction**: Use Lombok's `@SuperBuilder` pattern for entities:
```java
Product product = Product.builder()
    .productName(form.getProductName())
    .category(category)
    .build();
```

**Collection Initialization**: Do NOT initialize collections with `new ArrayList<>()` in entity declarations. Let Hibernate manage collection initialization to avoid masking PersistentBag behavior. Always check for null before adding/removing items in helper methods.

**Mappers**: Create `@Component` mapper classes (not MapStruct) with methods like `toEntity(Form, dependencies...)` and `toResponseDto(Entity)`.

**Exception Handling**: 
- `RestExceptionHandler` provides global error handling with `@ControllerAdvice`
- Use `EntityNotFoundException` from ports for missing resources
- Validation errors return field-level messages

**Lazy Loading**: All entity relationships use `fetch = FetchType.LAZY`. Always load required associations explicitly in service layer.

**Validation**: Use Jakarta validation annotations (`@NotNull`, `@NotBlank`, etc.) on DTOs. Validated automatically via `@RequestBody` and handled by `RestExceptionHandler`.

**Price Snapshot Pattern**: CartItem stores `unitPriceSnapshot` from Product at insertion time to maintain financial consistency even if product prices change later.

## Common Tasks

**Adding a New Domain Entity**:
1. Create entity extending `AuditableEntity` in `core/domain/model/`
2. Create `DomainPort` interface extending `NamedCrudPort<Domain>`
3. Create `JpaRepository<Domain, UUID>` in `repository/`
4. Create `DomainAdapter` extending `NamedCrudAdapter<Domain, DomainRepository>` implementing `DomainPort`
5. Create `DomainService` with business logic methods using the port
6. Create DTOs (`DomainForm`, `DomainResponseDto`) in `dto/`
7. Create `DomainMapper` @Component for conversions
8. Create `Register[Domain]UseCase` for complex operations with `@Transactional`
9. Create `DomainController` with REST endpoints

**Working with Profiles**: When creating/updating profiles, remember:
- Profiles use User's UUID as their ID via `@MapsId`
- CompanyProfile entities need cnpj (unique), companyName
- IndividualProfile entities need cpf (unique), name
- Automatically assign appropriate Role (COMPANY or INDIVIDUAL) to User during profile creation
- Cart is automatically initialized for IndividualProfile in `RegisterProfileUseCase`
- See `RegisterProfileUseCase` for the pattern

**Transaction Boundaries**: Apply `@Transactional` on use case methods, not on simple CRUD operations in adapters/services.

**Role Management**: Users must have roles assigned during signup:
- Use `RoleService.getByRoleName()` to fetch roles (COMPANY, INDIVIDUAL, ADMIN_MASTER)
- Pass roles to `UserService.signUp(email, password, roles)`
- COMPANY role for CompanyProfile, INDIVIDUAL for IndividualProfile

## Key Files

- `common/base/AuditableEntity.java` - Base entity with UUID & audit fields
- `common/base/NamedCrudAdapter.java` - Generic adapter implementation
- `common/ports/NamedCrudPort.java` - Port interface contract
- `config/SecurityConfiguration.java` - Security & CORS setup
- `config/ApplicationConfiguration.java` - Authentication beans
- `config/RestExceptionHandler.java` - Global exception handling
- `auth/service/JwtService.java` - JWT token generation/validation
- `SolidClassesApplication.java` - Entry point with `@EnableJpaAuditing`

## Notes

- Enable JPA auditing via `@EnableJpaAuditing` on main application class
- All IDs are UUIDs with `GenerationType.AUTO`
- Database recreates schema on startup (ddl-auto: create) - change for production
- Error messages in Portuguese (e.g., "não encontrado(a)")
- CORS configured to allow all origins with credentials
