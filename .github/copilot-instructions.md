# Copilot Instructions - UniMarket Solid Classes

## Project Context

**UniMarket** is a local e-commerce platform for a university campus. The system manages shopping carts for various vendors and autonomous entrepreneurs within the university, serving as a display window for their products and services - ranging from sweets and crafts to massage services and academic tutoring.

**Key Business Rules:**
- Payment is NOT processed through the platform - it facilitates connection between seller and buyer
- Product reservations are managed through the platform with secure data handling
- Students cannot make mistakes or jokes when reserving products (strict validation required)
- No delivery system - local pickup only at campus vendor stalls
- Users must see available product types and variations at physical vendor locations on campus

## Architecture Overview

This is a **Spring Boot 3.5.7 marketplace application** (uni_market) using **Java 25**, PostgreSQL, and JWT authentication. The codebase follows **Hexagonal Architecture** (Ports & Adapters) with SOLID principles.

### Core Architectural Patterns

**Domain Module Structure**: Each domain (product, cart, user, profile, etc.) in `src/main/java/com/example/solid_classes/core/` follows this consistent layout:
```
domain/
├── controller/      # REST endpoints with @Valid annotations
├── dto/            # Request/Response DTOs with validation
├── mapper/         # Entity ↔ DTO conversion (Spring @Component)
├── model/          # JPA entities extending AuditableEntity
│   └── enums/      # Domain-specific enums
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

**Padrão Arquitetural (Regras a Seguir)**
Para garantir consistência em toda a base de código, siga este padrão rigoroso ao adicionar domínios, entidades e casos de uso. Use estes nomes, responsabilidades e localizações de arquivo como contrato para revisões de código e PRs.

- **Estrutura de diretórios por domínio:**
    - `core/<domain>/controller` — controladores REST, mapeamento de endpoints e uso de `@Valid` em `@RequestBody`.
    - `core/<domain>/dto` — `*Form` (entrada), `*ResponseDto` (saída). Mensagens de validação em Português.
    - `core/<domain>/mapper` — `@Component` que converte `Form` ↔ `Entity` ↔ `ResponseDto` (não usar MapStruct).
    - `core/<domain>/model` — JPA entities que estendem `AuditableEntity` ou `ProfileEntity` quando aplicável.
    - `core/<domain>/ports` — interfaces `*Port` que estendem `NamedCrudPort<T>` (contrato da camada de domínio).
    - `core/<domain>/repository` — `JpaRepository<T, UUID>` para persistência.
    - `core/<domain>/service` — `*Service` para validações leves e orquestração local (delegam persistência ao Port).
    - `core/<domain>/service/Register*UseCase.java` — caso de uso transacional para operações complexas envolvendo múltiplos serviços/adapters.

- **Nomenclatura e arquivos de classe (padrão):**
    - Port: `DomainPort` (ex.: `ProductPort`)
    - Adapter: `DomainAdapter` (ex.: `ProductAdapter`) que estende `NamedCrudAdapter<Entity, Repository>` e implementa `DomainPort`
    - Service: `DomainService` (ex.: `ProductService`) — contém validações de negócio e métodos reutilizáveis entre UseCases
    - UseCase: `RegisterDomainUseCase` — método público `execute(...)` anotado com `@Transactional` para coordenar operações que afetam múltiplos domínios
    - Mapper: `DomainMapper` — `toEntity(Form, deps...)` e `toResponseDto(Entity)`
    - DTOs: `DomainForm`, `DomainResponseDto`

- **Entidades e JPA:**
    - Todas as entidades persistentes devem estender `AuditableEntity` (exceto `ProfileEntity` e suas subtipo quando aplicável).
    - Use `@SuperBuilder`, `@Getter` e `@NoArgsConstructor`; evite `@Setter` e `@Data`.
    - Não inicialize coleções no campo (deixe o Hibernate instanciar), sempre verificar `null` nas helpers.
    - Relações: `fetch = FetchType.LAZY`; carregue explicitamente nas services quando necessário.
    - Colunas monetárias: `BigDecimal` com `@Column(precision = 10, scale = 2, nullable = false)`.
    - Use métodos de conveniência para manter a consistência bidirecional (add/remove/set que atualizam ambos os lados).

- **Persistência / Adapters:**
    - Adapters implementam portas e encapsulam acesso a `JpaRepository`.
    - `NamedCrudAdapter` fornece implementação comum; sempre passe `entityName` para mensagens amigáveis (ex.: "Produto não encontrado(a)").
    - Evite lógica de negócio nas adapters — elas são adaptadores de infraestrutura.

- **Services e UseCases:**
    - `*Service` contém validações reutilizáveis e lógica leve que não exige transação distribuída.
    - Crie classes de UseCase para operações transacionais e para outras responsabilidades do domínio — não apenas para operações de "registro"/criação. UseCases servem para isolar e orquestrar fluxos de negócio e para manter cada unidade de lógica com responsabilidade única.
        - Exemplos de UseCases além de `Register*UseCase`:
            - `GetOrderUseCase` — carregamento de pedidos com associações e regras de visibilidade/escopo.
            - `CheckoutOrderUseCase` — fluxo transacional para finalizar um pedido (decremento de estoque, criação de OrderItems, mudança de status, notificações).
            - `CancelOrderUseCase`, `UpdateCartItemQuantityUseCase`, `CalculateCartTotalsUseCase`, etc.
    - Cada UseCase deve ter uma única responsabilidade: orquestrar o fluxo do caso de uso, delegar validações para `*Service` e persistência para `*Port`/Adapter, e retornar DTOs. Anote o método de entrada com `@Transactional` quando o fluxo modificar múltiplos agregados ou persistir mudanças em mais de uma dependência.
    - Testar UseCases com testes de integração que carreguem contexto mínimo do Spring (ou testes slice quando aplicável). Preferir testes que validem o fluxo completo do caso de uso.

- **Validações e Mensagens:**
    - Todas as validações em DTOs devem usar anotações Jakarta com mensagens em Português.
    - Erros de negócio lançam `BusinessRuleException` (ou exceções específicas) tratadas por `RestExceptionHandler`.

- **Boas práticas de versão e commit:**
    - Prefira PRs pequenos e focados; inclua uma breve descrição do impacto no modelo de dados.
    - Use mensagens de commit com prefixos (`feat:`, `fix:`, `chore:`).

Este padrão serve como contrato de equipe. Revisões de PR devem validar conformidade com estes pontos antes de aceitar mudanças de estrutura ou convenções.

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

**Validation**: 
- Use Jakarta validation annotations (`@NotNull`, `@NotBlank`, `@NotEmpty`, `@Min`, `@DecimalMin`, `@Size`, etc.) on all Form DTOs
- All controllers must use `@Valid` annotation on `@RequestBody` parameters
- Validation messages must be in Portuguese and user-friendly
- Validated automatically via `@Valid @RequestBody` and handled by `RestExceptionHandler`
- Required fields: Use `@NotNull` for objects/numbers, `@NotBlank` for strings, `@NotEmpty` for strings that shouldn't be whitespace-only

**Enum Organization**:
- All domain-specific enums must be placed in `model/enums/` directory within each domain
- Examples: `ReservationStatus` in `cart_item/model/enums/`, `RoleName` in `role/model/enums/`
- Use descriptive enum names and include comments for each value when needed

**Monetary Values with BigDecimal**:
- ALL monetary fields MUST use `BigDecimal` type, never `double` or `Double`
- Apply `@Column(nullable = false, precision = 10, scale = 2)` for monetary columns
- Use `BigDecimal` in entities (Product.priceBase, ServiceOffering.price, ProductVariation.variationAdditionalPrice, CartItem.unitPriceSnapshot, Order.orderTotal, OrderItem.productPrice, OrderItem.subtotal)
- Use `BigDecimal` in Form DTOs with `@DecimalMin` validation
- Use `BigDecimal` in Response DTOs for consistency
- ALL arithmetic operations MUST use BigDecimal methods: `.add()`, `.subtract()`, `.multiply()`, `.divide()`
- Use `BigDecimal.ZERO` instead of `0.0` for zero values
- For stream operations: use `.map()` and `.reduce(BigDecimal.ZERO, BigDecimal::add)` instead of `.mapToDouble().sum()`
- Example calculation: `product.getPriceBase().add(variation.getVariationAdditionalPrice())`
- Example stream sum: `items.stream().map(CartItem::calculateSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add)`

**Price Snapshot Pattern**: CartItem stores `unitPriceSnapshot` (BigDecimal) from Product at insertion time to maintain financial consistency even if product prices change later.

**Entity Constraints**:
- Always add `nullable = false` for required fields
- Add `unique = true` for business keys (productName, serviceName, etc.)
- Specify `length` for String columns to avoid database defaults
- Use `@Index` for frequently queried columns
- Use `@UniqueConstraint` to prevent business logic duplicates (e.g., same product in same cart)

**Cascade Operations**:
- Use `CascadeType.PERSIST` and `CascadeType.MERGE` for parent-child relationships
- Use `orphanRemoval = true` for strict ownership (e.g., CartItem belongs to Cart)
- Avoid `CascadeType.ALL` unless truly necessary

**Stock Management**:
- Products and ProductVariations have `stockQuantity` and `available` fields
- Always check stock before creating CartItems
- Use dedicated methods: `decreaseStock()`, `increaseStock()`, `hasStock()`
- Stock decreases on reservation, increases on cancellation

**Reservation Status Pattern**:
- CartItem has `ReservationStatus` enum: PENDING, RESERVED, COMPLETED, CANCELLED
- Items start as PENDING when added to cart
- Only PENDING items can be modified
- Use `reserve()`, `complete()`, `cancel()` methods to transition states
- Check `canModifyQuantity()` before allowing quantity changes

## Common Tasks

**Adding a New Domain Entity**:
1. Create entity extending `AuditableEntity` in `core/domain/model/`
2. Add appropriate constraints (`@Column(nullable = false)`, `unique = true`, `length`)
3. Add indexes for foreign keys and frequently queried columns
4. Initialize collections as `null` (let Hibernate manage them)
5. Add null checks in all collection manipulation methods
6. Create `DomainPort` interface extending `NamedCrudPort<Domain>`
7. Create `JpaRepository<Domain, UUID>` in `repository/`
8. Create `DomainAdapter` extending `NamedCrudAdapter<Domain, DomainRepository>` implementing `DomainPort`
9. Create `DomainService` with business logic methods using the port
10. Create DTOs (`DomainForm`, `DomainResponseDto`) in `dto/`
11. Create `DomainMapper` @Component for conversions
12. Create `Register[Domain]UseCase` for complex operations with `@Transactional`
13. Create `DomainController` with REST endpoints

**Working with Profiles**: When creating/updating profiles, remember:
- Profiles use User's UUID as their ID via `@MapsId`
- CompanyProfile entities need cnpj (unique), companyName
- IndividualProfile entities need cpf (unique), name
- Automatically assign appropriate Role (COMPANY or INDIVIDUAL) to User during profile creation
- Cart is automatically initialized for IndividualProfile in `RegisterProfileUseCase`
- See `RegisterProfileUseCase` for the pattern

**Transaction Boundaries**: Apply `@Transactional` on use case methods, not on simple CRUD operations in adapters/services.

**Role Management**: Users must have roles assigned during signup:
 - Use `RoleService.getByRoleName()` to fetch roles (COMPANY, INDIVIDUAL, ADMIN)
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
