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

This is a **Spring Boot 3.5.7 marketplace application** (uni_market) using **Java 25**, with **hybrid database architecture (PostgreSQL + MongoDB)**, and JWT authentication. The codebase follows **Hexagonal Architecture** (Ports & Adapters) with SOLID principles.

### Hybrid Database Architecture — PostgreSQL + MongoDB

The application uses a **hybrid database architecture** combining PostgreSQL and MongoDB to achieve optimal performance, flexibility, and consistency for different types of operations.

#### 🟦 PostgreSQL — Relational and Transactional Storage (ACID)

PostgreSQL remains responsible for all **critical entities** that require referential integrity, security, or transactional consistency. This data represents the business core and must obey strict rules.

**Entities maintained in PostgreSQL:**

- **Users, profiles and authentication:**
  - `users`, `roles`, `users_roles`, `individual_profiles`, `company_profiles`

- **Orders and financial operations:**
  - `orders`, `order_items`

- **Shopping cart (for now):**
  - `carts`, `cart_items`

- **Categories and fixed referential structures:**
  - `categories`, `variation_categories`, `global_variations`, `seller_variations`, `category_global_variations_mapping`

**PostgreSQL is responsible for:**
- Guaranteeing transaction consistency and atomicity
- Controlling complex relationships
- Maintaining sensitive information and business audit data
- Recording purchases, critical inventory, and accounting data

#### 🟩 MongoDB — Catalog, Variations, Services, and Logs

MongoDB is used for all structures that need **flexibility, high read volume, low schema rigidity, and rapid attribute expansion**. It stores JSON documents that can evolve independently without needing to alter the global schema.

**Entities migrated to MongoDB:**

- **products** → main catalog document
- **product_variations** → stored as arrays within the product document
- **services** → independent documents
- **logs and massive data:**
  - access logs
  - action logs
  - API audit
  - navigation tracking
  - events and notifications
  - metrics and usage history

**These data are perfect for NoSQL because they:**
- vary greatly between companies and categories
- may contain dynamic and specific attributes
- need to respond quickly in public queries (catalog)
- generate high volume (logs and tracking)

#### 🧩 How Products and Variations Work in MongoDB

Instead of several normalized tables (like `products`, `product_variations`, `variation_categories`), each product is stored as a **single document** containing all variations within an array:

```json
{
  "_id": "uuid",
  "name": "Pizza Calabresa",
  "description": "...",
  "basePrice": 35.90,
  "companyId": "uuid-company",
  "categoryId": "uuid-category",
  "stockQuantity": 10,
  "available": true,
  "variations": [
    {
      "categoryName": "Tamanho",
      "value": "Grande",
      "additionalPrice": 10,
      "stockQuantity": 5,
      "available": true
    }
  ],
  "createdAt": "2025-01-01T10:00:00Z",
  "updatedAt": "2025-01-15T14:30:00Z"
}
```

**Benefits:**
- Zero JOINs
- Extremely fast queries
- Flexible and expandable structure
- Variations are added to the document with `$push`
- Filters are done within the array (`variations.value`, `variations.categoryName`)

#### 🧾 How Logs Are Handled in MongoDB

Logs do NOT stay in PostgreSQL. MongoDB is used because it:
- allows massive writes with high throughput
- allows automatic expiration with TTL indexes
- accepts large and different documents from each other

**Example log document:**
```json
{
  "userId": "uuid",
  "action": "PRODUCT_VIEW",
  "timestamp": "2025-01-20T15:45:30Z",
  "metadata": {
    "productId": "uuid-product",
    "ip": "192.168.0.5"
  }
}
```

#### 🔄 General Application Integration

- **PostgreSQL maintains business state**
  → users, companies, orders, categories

- **MongoDB maintains dynamic state and heavy read operations**
  → products, variations, services, logs

- **Product ID in PostgreSQL is mirrored in the MongoDB document**
  → allows orders to reference only `product_id` (SQL)
  → while the catalog is read directly from Mongo

- **Application queries catalog from MongoDB**
  → shopping interface, filters, product display

- **Application writes orders to PostgreSQL**
  → because transactions need to be ACID

#### ⭐ Architecture Summary

- **PostgreSQL** = critical core + business rules + security + transactions
- **MongoDB** = catalog, variations, services, flexible data, logs, and heavy queries

**Hybrid architecture guarantees:**
- ✅ Speed in catalog
- ✅ Consistency in orders
- ✅ Flexibility for new product types
- ✅ Horizontal scalability
- ✅ Efficient log storage

### Core Architectural Patterns

> 🔷 **Padrão oficial**: todo o projeto segue Arquitetura Hexagonal (Ports & Adapters). Quando criar ou alterar componentes, mantenha a separação Controller → UseCase → Service → Port → Adapter intacta para garantir baixo acoplamento e facilidade na troca de tecnologias (como PostgreSQL e MongoDB).

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
# Copilot Instructions - UniMarket Solid Classes

## 1. Project Context & Business Overview

**UniMarket** is a hyper-local e-commerce platform designed for a university campus. It connects students (buyers) with autonomous vendors and student-entrepreneurs (sellers).

**Key Constraints:**
- **No Delivery System:** All transactions result in a physical pickup at the vendor's location on campus.
- **Transactions:** Payment is processed off-platform (or via simple integration), but the system manages the *reservation* and *order lifecycle*.
- **Strict Validation:** No "joke" reservations allowed; strict data types and validation required.
- **Marketplace Model:** A single cart may contain items from multiple different sellers (Split Order logic required).

---

## 2. Technical Stack & Architecture

- **Language:** Java 25
- **Framework:** Spring Boot 3.5.7
- **Architecture:** Hexagonal Architecture (Ports & Adapters)
- **Database:** Hybrid Architecture (PostgreSQL + MongoDB)
- **Authentication:** JWT (Stateless)
- **Documentation:** SpringDoc OpenAPI v2 (Swagger)

---

## 3. Hybrid Database Strategy (Strict Separation)

The application uses two databases to optimize for transactional integrity (SQL) and catalog flexibility (NoSQL). **You must respect this separation.**

### 🟦 PostgreSQL (Transactional Core)
**Responsibility:** Data integrity, relations, financial history, user access.
**Persistence:** `JpaRepository`.
**Entities (JPA):**
- **Auth:** `User`, `Role`
- **Profiles:** `IndividualProfile`, `CompanyProfile`, `ProfileEntity` (@MappedSuperclass)
- **Financial:** `Order`, `OrderItem` (Snapshots)
- **State:** `Cart`, `CartItem`
- **Taxonomy:** `Category`, `VariationCategoryEntity` (Global/Seller inheritance)

### 🟩 MongoDB (Catalog & Discovery)
**Responsibility:** High-read volume, flexible schema, product catalog, logs.
**Persistence:** `MongoRepository`.
**Documents:**
- **Catalog:** `Product` (Document), `ProductVariation` (Embedded Array inside Product)
- **Services:** `ServiceOffering`
- **Audit:** `Logs`, `AccessHistory`

> 🚨 **CRITICAL ARCHITECTURE RULE:**
> 1. Do **NOT** create JPA entities for `Product` or `ProductVariation`.
> 2. PostgreSQL entities (like `CartItem` or `OrderItem`) reference Products only by their UUID string.
> 3. Complex catalog queries (filters, text search) happen in MongoDB.
> 4. Transactional operations (Checkout, Stock Reservation) coordinate both DBs via UseCases.

---

## 4. Layer Structure (Hexagonal)

Follow this directory structure for every new domain (`src/main/java/com/example/solid_classes/core/<domain>/`):

1.  **`controller/`** (Adapter In):
    - REST Endpoints.
    - Uses `@Valid` on DTOs.
    - Returns `ResponseEntity<ResponseDto>`.
    - **Rule:** Never allows Entities to leak to the API surface.

2.  **`dto/`** (Data Transfer Objects):
    - `[Domain]Form`: Input data with Jakarta Validation (`@NotNull`, `@DecimalMin`).
    - `[Domain]ResponseDto`: Output data.

3.  **`mapper/`** (Pure Functions):
    - Converts `Form` → `Entity` and `Entity` → `ResponseDto`.
    - **Rule:** Must be `@Component`.
    - **Rule:** **SIDE-EFFECT FREE**. Never inject Services/Repositories into Mappers. Pass all dependencies (e.g., `CategoryName`) as method arguments.

4.  **`model/`** (Domain Entities):
    - JPA Entities or Mongo Documents.
    - Uses `@SuperBuilder`, `@Getter`.
    - **Rule:** Extends `AuditableEntity`.

5.  **`ports/`** (Output Port):
    - Interfaces extending `NamedCrudPort<T>`.

6.  **`repository/`** (DB Access):
    - Interfaces extending `JpaRepository` or `MongoRepository`.

7.  **`service/`** (Domain Logic):
    - `[Domain]Adapter`: Implements Port, uses Repository.
    - `[Domain]Service`: Lightweight validation, pure domain logic.
    - `Register[Domain]UseCase`: **@Transactional** orchestrator. Handles the flow between multiple services.

---

## 5. Coding Standards & Corrections (High Priority)

### 🔹 Mappers: Pure & Null-Safe
**Correction:** Do not inject Services into Mappers to fetch data. Fetch data in the UseCase and pass it to the mapper.
* *Bad:* `mapper.toDto(product)` (where mapper calls DB to get category name).
* *Good:* `mapper.toDto(product, category.getName(), company.getName())`.

**Correction:** Always check for nulls in lazy relationships before accessing properties.
* *Pattern:* `String name = (entity.getRelation() != null) ? entity.getRelation().getName() : null;`

### 🔹 Pagination
**Correction:** Never return `List<T>` for search/listing endpoints.
* *Pattern:* Use `Pageable` in Controller and Repository. Return `Page<DTO>`.

### 🔹 Exception Handling
**Correction:** Avoid `throws` in method signatures. Use unchecked exceptions.
* *Pattern:* Throw `BusinessRuleException("Message in Portuguese")` for logic failures.
* *Pattern:* Throw `EntityNotFoundException` (via `NamedCrudAdapter`) when resources are missing.

### 🔹 Monetary Values
**Correction:** ALWAYS use `BigDecimal`.
* *Annotation:* `@Column(nullable = false, precision = 10, scale = 2)`
* *Math:* Use `.add()`, `.multiply()`. Never use operators `+` `*`.

---

## 6. Critical Business Logic Patterns

### 🛒 Split Order (Checkout)
The system is a Marketplace. One `Cart` can contain items from multiple Sellers.
**On Checkout:**
1.  Group `CartItems` by `CompanyProfile`.
2.  Create one `Order` entity per Company.
3.  Generate a unique **Pickup Code** (5 chars, e.g., `#A3K9`) per Order.
4.  Save `OrderItem` snapshots (see below).
5.  Clear the Cart.

### 📸 Order Snapshots
**Rule:** `Order` and `OrderItem` must represent the **past**.
**Implementation:**
- `OrderItem` must store a copy of: `productName`, `unitPrice` (at time of purchase), and `quantity`.
- Do **NOT** rely on the `Product` link to get the price, as the vendor may change it later.

### 📦 Inventory Management
- **Source of Truth:** MongoDB (`Product.stockQuantity`).
- **Reservation:** When an `Order` is created, decrement stock in MongoDB.
- **Validation:** `RegisterCartItemUseCase` must check Mongo stock availability before adding to Postgres Cart.

---

## 7. AI Agent Checklist

Before generating code, verify:

1.  [ ] **Database:** Am I trying to create a JPA relationship to a Product? -> **STOP**. Use UUID reference.
2.  [ ] **Mapper:** Am I `@Autowired`-ing a Service in a Mapper? -> **STOP**. Pass data as args.
3.  [ ] **Search:** Is this a list endpoint? -> **USE PAGEABLE**.
4.  [ ] **Language:** Are validation messages/Exceptions in Portuguese (PT-BR)? -> **YES**.
5.  [ ] **Safety:** Did I initialize Lists as `null` in the Entity to let Hibernate handle it? -> **YES**.
6.  [ ] **Logic:** Did I use `BigDecimal` for money? -> **YES**.
- `auth/service/JwtService.java` - JWT token generation/validation

