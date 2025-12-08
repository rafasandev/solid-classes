  # Copilot Instructions - UniMarket

  > ⚠️ **Governança obrigatória:** sempre que qualquer alteração estrutural atingir entidades, modelos de dados, integrações com PostgreSQL/MongoDB ou contratos entre camadas Hexagonais, atualize imediatamente este arquivo antes de concluir a tarefa/PR.

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
  - **Users, profiles and authentication (PostgreSQL / JPA):**
    - `users` — `User` (JPA) (email, password, active, roles, contacts)
    - `roles` — `Role` (JPA) (enum name, users)
    - `individual_profiles` — `IndividualProfile` (JPA) (linked `User`, cart)
    - `company_profiles` — `CompanyProfile` (JPA) (companyName, cnpj, availability, variationCategories, orders, paymentMethods)

  - **Orders and financial operations (PostgreSQL / JPA):**
    - `orders` — `Order` (JPA) (pickUpcode, status, orderTotal, customer, company, orderItems)
    - `order_items` — `OrderItem` (JPA) (product snapshot fields, quantity, subtotal, order)

  - **Shopping cart (PostgreSQL / JPA):**
    - `carts` — `Cart` (JPA) (profile, items)
    - `cart_items` — `CartItem` (JPA) (productId, productVariationId, productName, unitPriceSnapshot, itemQuantity, status, cart)

  - **Taxonomy and variation categories (PostgreSQL / JPA):**
    - `categories` — `Category` (JPA) (categoryName, businessSector, variationCategories)
    - `variation_categories` — `VariationCategoryEntity` (JPA, inheritance JOINED) with concrete `VariationCategoryGlobal` and `VariationCategorySeller`

  - **Contact & payment helpers (PostgreSQL / JPA):**
    - `contact_methods` — `ContactType` (JPA) (channel, baseUrl, validationRegex, iconUrl)
    - `contact_infos` — `ContactInfo` (JPA) (value, contactType, user(profile))
    - `payment_methods` — `PaymentMethod` (JPA) (name, iconUrl, companyProfiles)

  - **Other supporting entities (PostgreSQL / JPA):**
    - `roles`, `payment_methods`, and any audit/lookup tables used by business logic

  **PostgreSQL is responsible for:**
  - Guaranteeing transaction consistency and atomicity
  - Controlling complex relationships
  - Maintaining sensitive information and business audit data
  - Recording purchases, critical inventory, and accounting data

  #### 🟩 MongoDB — Catalog, Variations, Services, and Logs

  MongoDB is used for all structures that need **flexibility, high read volume, low schema rigidity, and rapid attribute expansion**. It stores JSON documents that can evolve independently without needing to alter the global schema.

  **Entities migrated to MongoDB:**

  - **products** — `Product` (MongoDB document) (productName, description, basePrice, companyId, categoryId, totalStockCache, variations refs, locationReference)
  - **product_variations** — `ProductVariation` (MongoDB document) (productId, variationCategoryId, value, variationAdditionalPrice, stockQuantity, available)
  - **services** — `ServiceOffering` (MongoDB document) (serviceName, description, price, durationMinutes, available, companyId)
  - **logs and massive data:**
    - audit and access logs (`AuditLog` / Mongo collection)
    - action logs, API audit, navigation tracking, events and notifications, metrics and usage history

  **These data are perfect for NoSQL because they:**
  - vary greatly between companies and categories
  - may contain dynamic and specific attributes
  - need to respond quickly in public queries (catalog)
  - generate high volume (logs and tracking)

  #### 🧩 How Products and Variations Work in MongoDB

  **Important:** `ProductVariation` is stored as a **separate MongoDB document** (not embedded in Product). Each variation has its own `_id` and references the parent `Product` via `productId`.

  Product Structure:
  ```json
  {
    "_id": "uuid",
    "productName": "Pizza Calabresa",
    "description": "...",
    "basePrice": 35.90,
    "companyId": "uuid-company",
    "categoryId": "uuid-category",
    "stockQuantity": 10,
    "variations": [],
    "createdAt": "2025-01-01T10:00:00Z",
    "updatedAt": "2025-01-15T14:30:00Z"
  }
  ```

  ProductVariation Structure (separate document):
  ```json
  {
    "_id": "uuid",
    "productId": "uuid-product",
    "categoryName": "Tamanho",
    "categoryType": "GLOBAL",
    "value": "Grande",
    "valueType": "TEXT",
    "additionalPrice": 10,
    "stockQuantity": 5,
    "available": true,
    "createdAt": "2025-01-01T10:00:00Z",
    "updatedAt": "2025-01-15T14:30:00Z"
  }
  ```

  **Benefits:**
  - Separate lifecycle for Product and Variations
  - Easy to query variations independently
  - Product maintains a list of variation references for convenience
  - Flexible and expandable structure

  **Note:** ProductVariation is a first-class Mongo document and may be DBRef-linked from `Product` documents; do not create JPA entities for these.

  #### 📞 Contact Channels & Accepted Payments (PostgreSQL)

  - **ContactType** define the available communication channels (WhatsApp, SMS, Call, etc.) with `ContactChannel`, `baseUrl`, `validationRegex` and `iconUrl`. They live in the `contact_type` domain and are seeded/managed centrally.
  - **ContactInfo** is a JPA entity that links a `User` to a `ContactType` plus the end-user value (phone number, profile link, etc.). All `ProfileMapper` conversions rely on the `User.contacts` list already populated; never fetch repositories inside the mapper.
  - **PaymentMethod** holds marketplace-level payment options (Pix, Cartão, Dinheiro) with `name` and `iconUrl`. Companies reference them through a `ManyToMany` to express which methods they accept. Responses expose lightweight DTOs (`PaymentMethodResponseDto`) derived exclusively from the entity data provided to the mapper.
  - **Configuração pós-criação**: empresas autenticadas usam `PUT /profile/company/{id}/contacts` (caso de uso `ConfigureCompanyContactsUseCase`) para substituir integralmente seus canais. O payload `CompanyContactConfigurationForm` recebe uma lista de `ContactInfoForm` e cada valor é sanitizado/validado com o `validationRegex` do respectivo `ContactType`.
  - **Pagamentos aceitos**: `PUT /profile/company/{id}/payment-methods` dispara `ConfigureCompanyPaymentMethodsUseCase`, que exige `CompanyPaymentMethodsForm` com todos os IDs de `PaymentMethod`. O caso de uso garante que todos os IDs existam e sincroniza ambos os lados do relacionamento ManyToMany.

  #### 🕒 Company Availability Windows

  - `CompanyProfile.weekDaysAvailable` é persistido via `@ElementCollection` na tabela `company_available_week_days`, permitindo saber rapidamente quais dias possuem atendimento.
  - `CompanyProfile.dailyAvailableTimeRanges` guarda uma lista de `CompanyDailyAvailability` (dia + intervalo) em `company_daily_availability`. O `ProfileMapper` agrupa esses registros e expõe um `Map<Integer, List<TimeRange>>` no DTO.
  - O endpoint `PUT /profile/company/{id}/availability` (caso de uso `ConfigureCompanyAvailabilityUseCase`) consome `CompanyAvailabilityForm`, validando sobreposições de horários, limites 0-6 para dias da semana e exigindo intervalos para todos os dias selecionados.
  - Sempre valide a propriedade do perfil com `UserService.getLoggedInUser()` antes de aplicar qualquer mudança na disponibilidade.

  #### 🔁 Post-Creation Company Configuration Flow

  - **Contatos:** `PUT /profile/company/{id}/contacts` substitui todos os registros usando `CompanyContactConfigurationForm`. O `ContactInfoService` sanitiza os valores, valida com o regex do `ContactType` e regrava a coleção via `ContactInfoPort.deleteByProfileId` + `saveAll`.
  - **Pagamentos:** `PUT /profile/company/{id}/payment-methods` aceita `CompanyPaymentMethodsForm` e usa `PaymentMethodService.getAllByIds` para garantir que 100% dos IDs existam antes de sincronizar o ManyToMany.
  - **Disponibilidade:** `PUT /profile/company/{id}/availability` aplica `CompanyAvailabilityForm`, normaliza dias (0-6), impede sobreposições (`CompanyDailyAvailability`) e persiste via `@ElementCollection`.
  - Todos os casos de uso validam propriedade com `UserService.getLoggedInUser()` e retornam `CompanyProfileResponseDto` atualizado através do `ProfileMapper`.

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

  **Domain Module Structure**: Each domain (product, cart, user, profile, etc.) in `src/main/java/com/example/market_api/core/` follows this consistent layout:
  ```
  domain/
  ├── controller/      # REST endpoints with @Valid annotations
  ├── dto/            # Request/Response DTOs with validation
  ├── mapper/         # Entity ↔ DTO conversion (Spring @Component)
  ├── model/          # JPA entities or Mongo Documents
  │   └── enums/      # Domain-specific enums
  ├── ports/          # Interface extending NamedCrudPort<T>
  ├── repository/     # Database access layer (separated by type)
  │   ├── jpa/        # JpaRepository interfaces (PostgreSQL)
  │   └── mongo/      # MongoRepository interfaces (MongoDB)
  └── service/
      ├── DomainAdapter.java        # Implements port, extends NamedCrudAdapter or NamedMongoAdapter
      ├── DomainService.java        # Business logic orchestration
      └── Register[Domain]UseCase.java  # @Transactional use cases
  ```

  **Repository Directory Structure Rules:**
  - **`repository/jpa/`**: Reserved for repositories that extend `JpaRepository<T, UUID>` and connect to **PostgreSQL**. Used for transactional entities like `User`, `Order`, `Cart`, `Category`, etc.
  - **`repository/mongo/`**: Reserved for repositories that extend `MongoRepository<T, UUID>` and connect to **MongoDB**. Used for catalog entities like `Product`, `ProductVariation`, `ServiceOffering`, and log documents.
  - The adapter must use the appropriate base class: `NamedCrudAdapter` for JPA repositories or `NamedMongoAdapter` for Mongo repositories.

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
      - `core/<domain>/model` — Entidades JPA (estendem `AuditableEntity` ou `ProfileEntity`) ou Documentos Mongo (estendem `AuditableMongoEntity`).
      - `core/<domain>/model/enums/` — Enums específicos do domínio.
      - `core/<domain>/ports` — interfaces `*Port` que estendem `NamedCrudPort<T>` (contrato da camada de domínio).
      - `core/<domain>/repository` — Diretório de persistência separado por tipo de banco:
          - `core/<domain>/repository/jpa/` — interfaces `JpaRepository<T, UUID>` para entidades PostgreSQL.
          - `core/<domain>/repository/mongo/` — interfaces `MongoRepository<T, UUID>` para documentos MongoDB.
      - `core/<domain>/service` — `*Service` para validações leves e orquestração local (delegam persistência ao Port).
      - `core/<domain>/service/Register*UseCase.java` — caso de uso transacional para operações complexas envolvendo múltiplos serviços/adapters.
      - `core/<domain>/service/Get*UseCase.java` — caso de uso para operações de leitura com transformação para DTOs.

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
      - Adapters implementam portas e encapsulam acesso a repositórios (`JpaRepository` ou `MongoRepository`).
      - Use `NamedCrudAdapter` para repositórios JPA (PostgreSQL) e `NamedMongoAdapter` para repositórios Mongo (MongoDB).
      - Sempre passe `entityName` para mensagens amigáveis (ex.: "Produto não encontrado(a)").
      - Evite lógica de negócio nas adapters — elas são adaptadores de infraestrutura.
      - **Regra crítica:** Escolha o adapter correto baseado no tipo de repositório:
          - Entidade JPA → `repository/jpa/` → `NamedCrudAdapter` → estende `AuditableEntity`
          - Documento Mongo → `repository/mongo/` → `NamedMongoAdapter` → estende `AuditableMongoEntity`
      - **NamedCrudAdapter**: Para PostgreSQL (JPA), disponibiliza todos os métodos CRUD padrão.
      - **NamedMongoAdapter**: Para MongoDB, adiciona lógica de geração de UUID antes do save (`generateId()`).
      - Adapters podem adicionar métodos customizados delegando ao repositório (ex.: `findByCompanyId`, `searchByName`).

  - **Services e UseCases:**
      - `*Service` contém validações reutilizáveis e lógica leve que não exige transação distribuída.
      - Crie classes de UseCase para operações transacionais e para outras responsabilidades do domínio — não apenas para operações de "registro"/criação. UseCases servem para isolar e orquestrar fluxos de negócio e para manter cada unidade de lógica com responsabilidade única.
          - Exemplos de UseCases além de `Register*UseCase`:
              - `Get*UseCase` — carregamento de entidades com associações e transformação para DTOs (ex.: `GetProductUseCase`, `GetCategoryUseCase`).
              - `CheckoutOrderUseCase` — fluxo transacional para finalizar um pedido (decremento de estoque, criação de OrderItems, mudança de status, notificações).
              - `CancelOrderUseCase`, `UpdateCartItemQuantityUseCase`, `CalculateCartTotalsUseCase`, etc.
      - Cada UseCase deve ter uma única responsabilidade: orquestrar o fluxo do caso de uso, delegar validações para `*Service` e persistência para `*Port`/Adapter, e retornar DTOs. 
      - **Anotação @Transactional:**
          - Use `@Transactional` quando o fluxo modificar múltiplos agregados ou persistir mudanças (write operations).
          - Use `@Transactional(readOnly = true)` para operações de leitura que precisam garantir consistência de snapshot.
      - Testar UseCases com testes de integração que carreguem contexto mínimo do Spring (ou testes slice quando aplicável). Preferir testes que validem o fluxo completo do caso de uso.
  # Copilot Instructions - UniMarket

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
  - **Build Tool:** Maven
  - **Key Dependencies:** Spring Data JPA, Spring Data MongoDB, Spring Security, JJWT, Lombok

  **Configuration Classes:**
  - `JpaConfiguration` — configura auditoria JPA e PostgreSQL
  - `MongoConfiguration` — configura auditoria MongoDB
  - `SecurityConfiguration` — configura autenticação JWT e hierarquia de roles
  - `ApplicationConfiguration` — beans gerais (PasswordEncoder, AuthenticationManager)
  - `OpenApiConfig` — configuração do Swagger/OpenAPI
  - `RestExceptionHandler` — tratamento global de exceções

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
  - **Catalog:** `Product` (Document que referencia variações via `@DBRef(lazy = true)` e mantém `totalStockCache`), `ProductVariation` (coleção independente com `productId` como chave de vínculo)
  - **Services:** `ServiceOffering`
  - **Audit:** `Logs`, `AccessHistory`

  > 🚨 **CRITICAL ARCHITECTURE RULE:**
  > 1. Do **NOT** create JPA entities for `Product` or `ProductVariation`.
  > 2. PostgreSQL entities (like `CartItem` or `OrderItem`) reference Products only by their UUID string.
  > 3. Complex catalog queries (filters, text search) happen in MongoDB.
  > 4. Transactional operations (Checkout, Stock Reservation) coordinate both DBs via UseCases.

  ---

  ## 4. Layer Structure (Hexagonal)

  Follow this directory structure for every new domain (`src/main/java/com/example/market_api/core/<domain>/`):

  1.  **`controller/`** (Adapter In):
      - REST Endpoints.
      - Uses `@Valid` on DTOs.
      - Returns `ResponseEntity<ResponseDto>`.
    - **Rule:** Never allows Entities to leak to the API surface.
    - **Rule:** Controllers depend **only** on UseCases (application services). Do **not** inject domain Services, Ports, Repositories or Mappers directly in controllers.

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

  6.  **`repository/`** (DB Access - Separated by Type):
      - **`repository/jpa/`**: Interfaces extending `JpaRepository<T, UUID>` for PostgreSQL entities.
      - **`repository/mongo/`**: Interfaces extending `MongoRepository<T, UUID>` for MongoDB documents.
      - **Rule:** Always place repositories in the correct subdirectory based on database type.

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
  * *Pattern:* Throw `EntityNotFoundException` (via `NamedCrudAdapter.throwEntityNotFound()`) when resources are missing.
  * *Handling:* `RestExceptionHandler` centraliza o tratamento de exceções globalmente:
      - `BusinessRuleException` → 400 Bad Request
      - `EntityNotFoundException` → 404 Not Found
      - `MethodArgumentNotValidException` → 400 com detalhes de validação
      - `DataIntegrityViolationException` → 409 Conflict
      - `UserRuleException` → 400 Bad Request

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
  - `OrderItem` must store a copy of: `productName`, `productPrice`, `variationAdditionalPriceSnapshot`, `finalUnitPriceSnapshot`, and `orderQuantity`.
  - Do **NOT** rely on the `Product` link to get the price, as the vendor may change it later.
  - Each `OrderItem` calculates its own `subtotal` via `calculateSubtotal()` method.

  ### 🛒 Cart Item Structure
  **CartItem (PostgreSQL):**
  - References products by UUID only (`productId`, `productVariationId`)
  - Stores snapshot data: `productName`, `unitPriceSnapshot`
  - Contains `itemQuantity` and `status` (ReservationStatus enum)
  - Has unique constraint per cart + product variation combination
  - Uses indexed columns for performance (`cart_id`, `product_variation_id`)

  ### 📦 Inventory Management
  - **Source of Truth:** MongoDB (`Product.stockQuantity`).
  - **Reservation:** When an `Order` is created, decrement stock in MongoDB.
  - **Validation:** `RegisterCartItemUseCase` must check Mongo stock availability before adding to Postgres Cart.

  ### 🧬 Profile Inheritance & Entity Relationships

  **ProfileEntity Inheritance Pattern:**
  - `ProfileEntity` is a `@MappedSuperclass` (not an entity itself).
  - Two concrete implementations stored in separate tables:
      - `IndividualProfile` → `individual_profiles` table
      - `CompanyProfile` → `company_profiles` table
  - Both use `@MapsId` to share the same ID with their linked `User` entity (one-to-one).
  - `ProfileEntity` extends the base auditing structure but does NOT extend `AuditableEntity`.

  **VariationCategory Inheritance Pattern:**
  - `VariationCategoryEntity` is a JPA entity with `@Inheritance(strategy = InheritanceType.JOINED)`.
  - Two concrete implementations:
      - `VariationCategoryGlobal` → `variation_categories_global` table (platform-wide)
      - `VariationCategorySeller` → `variation_categories_seller` table (company-specific)
  - Adapters: `VariationCategoryGlobalAdapter` e `VariationCategorySellerAdapter` são separados.

  **Bidirectional Relationship Helpers:**
  - Entities com relacionamentos bidirecionais implementam métodos helper para manter consistência:
      - `Product.setCategory(Category)` → atualiza ambos os lados da relação.
      - `OrderItem.setOrder(Order)` → adiciona/remove o item na lista do Order.
      - `CartItem.setCart(Cart)` → similar.
  - **SEMPRE** use esses métodos helper ao invés de modificar diretamente as coleções.

  ---

  ## 7. Security & Authentication

  **JWT-based Authentication:**
  - `JwtService` gerencia geração e validação de tokens JWT.
  - `JwtAuthFilter` intercepta requisições e valida tokens.
  - `SecurityConfiguration` define:
      - Endpoints públicos: `/auth/**`, `/user/**`, `/swagger-ui/**`, `/v3/api-docs/**`
      - Todos os outros endpoints requerem autenticação.
      - Stateless session management (JWT).
      - CORS habilitado para todos os origins em desenvolvimento.

  **Role Hierarchy:**
  - `ROLE_ADMIN > ROLE_COMPANY`
  - `ROLE_ADMIN > ROLE_INDIVIDUAL`
  - Controllers usam `@PreAuthorize("hasRole('ROLE_NAME')")` para controle de acesso.

  **User Context:**
  - `UserService.getLoggedInUser()` recupera o usuário autenticado do contexto Spring Security.
  - UseCases devem validar ownership (ex.: um Company só pode criar produtos para si mesmo).

  ---

  ## 8. AI Agent Checklist

  Before generating code, verify:

  1.  [ ] **Database:** Am I trying to create a JPA relationship to a Product? -> **STOP**. Use UUID reference.
  2.  [ ] **Mapper:** Am I `@Autowired`-ing a Service in a Mapper? -> **STOP**. Pass data as args.
  3.  [ ] **Search:** Is this a list endpoint? -> **USE PAGEABLE**.
  4.  [ ] **Language:** Are validation messages/Exceptions in Portuguese (PT-BR)? -> **YES**.
  5.  [ ] **Safety:** Did I initialize Lists as `null` in the Entity to let Hibernate handle it? -> **YES**.
  6.  [ ] **Logic:** Did I use `BigDecimal` for money? -> **YES**.
  7.  [ ] **Adapter:** Did I extend the correct base adapter (`NamedCrudAdapter` for JPA, `NamedMongoAdapter` for Mongo)? -> **YES**.
  8.  [ ] **UseCase:** Did I annotate with `@Transactional` or `@Transactional(readOnly = true)` appropriately? -> **YES**.
  9.  [ ] **Security:** Did I validate user ownership in UseCases that modify user-specific resources? -> **YES**.
  10. [ ] **Repository Location:** Is the repository in the correct subdirectory (`jpa/` or `mongo/`)? -> **YES**.

  ---

  ## 9. Key Service Classes Reference

  - `auth/service/JwtService.java` - JWT token generation/validation
  - `auth/service/JwtAuthFilter.java` - JWT authentication filter
  - `user/service/UserService.java` - User management and logged user context
  - `role/service/RoleSeeder.java` - Initial role setup on application startup
  - `order/service/PickupCodeGenerator.java` - Unique pickup code generation for orders
  - `auth/service/JwtService.java` - JWT token generation/validation


  ## 10. MVP — Regras de Negócio Essenciais

  Estas regras representam o conjunto mínimo de comportamentos que um MVP válido da UniMarket deve satisfazer. Elas derivam do arquivo `mvp_basics.txt` e devem ser seguidas por qualquer implementação mínima do produto.

  - **Registro de Vendedor:**
    - Criação de `User` e `CompanyProfile` ao cadastrar um vendedor.
    - `company` inicia com saldo `0` (flag de desenvolvimento/contabilidade) e `status` geral `ATIVO` mas `statusDeVenda` como `FECHADO` até o vendedor habilitar vendas (implementação pendente onde aplicável).

  - **Configuração de Disponibilidade:**
    - Vendedor define dias da semana e intervalos horários de funcionamento.
    - Disponibilidade gravada no perfil da empresa e validada (sem sobreposição de intervalos).

  - **Criação de Produto (Catálogo - MongoDB):**
    - Produto criado com dados básicos (`name`, `description`, `basePrice`, `categoryId`) e referência `companyId`.
    - Novo produto é inserido no Mongo com `status = INATIVO` e sem variações até o vendedor ativar/editar.

  - **Criação de Variação (MongoDB):**
    - Variações associadas ao `productId` são documentos independentes (`product_variations`).
    - Cada variação inclui `value`, `additionalPrice`, `stockQuantity` e `available`.

  - **Adicionar Item no Carrinho (PostgreSQL):**
    - Usuário seleciona `productVariationId` e quantidade.
    - `RegisterCartItemUseCase` valida estoque consultando o catálogo em Mongo antes de persistir o `CartItem` em Postgres.
    - Ao adicionar, é criado um snapshot do nome e preço unitário atual no `CartItem`.
    - Se não houver estoque, a variação não deve estar disponível para seleção (UX) e a tentativa de adicionar deve falhar com mensagem apropriada.

  - **Visualizar Carrinho:**
    - Cálculo do total em tempo real a partir dos snapshots e quantidades.
    - Produtos inativos devem ser automaticamente removidos do carrinho (ou sinalizados), com notificação ao usuário.

  - **Finalização do Pedido (Checkout / Split Order):**
    - Agrupar itens do carrinho por `CompanyProfile` e criar um `Order` por vendedor.
    - Criar `OrderItem` como snapshot (nome, preço unitário, variação, preço adicional, quantidade e subtotal calculado).
    - Ajustar estoque no Mongo (decremento) durante o fluxo transacional e garantir consistência.
    - Persistir orders em PostgreSQL (estado transacional) e limpar itens do carrinho.

  - **Fluxo de Retirada / Mudança de Status do Pedido:**
    - Gerar um `Pickup Code` único por pedido (ex.: 5 caracteres) para validação na retirada.
    - Estados mínimos do pedido: `PENDENTE`, `PAGO`, `PRONTO_RETIRADA`, `COMPLETADO`, `CANCELADO`, `EXPIRADO`, `AGUARDANDO_CONFIRMACAO`, `SEM_RETIRADA`.
    - Regras adicionais:
      - Se o cliente não retirar em 24h, marcar `EXPIRADO`.
      - `SEM_RETIRADA` é um estado administrativo para contabilização de faltas e possíveis punições.

  ---

  Notas de integração e prioridades para o MVP:

  - Priorizar: cadastro de vendedor, criação de produto/variação no catálogo (Mongo), adicionar ao carrinho, checkout com split order e estados básicos de pedido.
  - Opcional inicialmente: processamento de saldo do vendedor (contabilidade), notificações avançadas, e regras de punição (podem ser introduzidas após MVP).
  - Segurança: validar propriedade (ex.: `UserService.getLoggedInUser()`) em endpoints que alteram perfis/recursos de empresas.

  Estas regras devem ser incluídas na documentação do projeto e servem como contrato para PRs que toquem o fluxo de catálogo, carrinho ou checkout.

  ## Entity Reference — Basic Structure

  This section lists the current Java domain entities (JPA and Mongo documents), their persistence type (SQL/NoSQL), core properties and main relationships. Use this as a structural reference when adding or changing domains.

  **PostgreSQL (JPA entities)**
  - `User` (JPA / SQL)
    - Base: extends `AuditableEntity` (UUID id, createdAt, updatedAt)
    - Key fields: `email`, `password`, `active`
    - Relations: `contacts` (OneToMany -> `ContactInfo`), `roles` (ManyToMany -> `Role`)

  - `Role` (JPA / SQL)
    - Fields: `name` (enum `RoleName`)
    - Relations: `users` (ManyToMany -> `User`)

  - `ContactType` (JPA / SQL)
    - Fields: `channel` (enum), `baseUrl`, `validationRegex`, `iconUrl`
    - Relations: `contactInfos` (OneToMany -> `ContactInfo`)

  - `ContactInfo` (JPA / SQL)
    - Fields: `value`
    - Relations: `contactType` (ManyToOne -> `ContactType`), `profile`/`user` (ManyToOne -> `User`)

  - `CompanyProfile` (JPA / SQL)
    - Extends `ProfileEntity` (maps to `company_profiles`)
    - Fields: `companyName`, `cnpj`, `businessSector`, `weekDaysAvailable`, `dailyAvailableTimeRanges`
    - Relations: `variationCategories` (OneToMany -> `VariationCategorySeller`), `orders` (OneToMany -> `Order`), `paymentMethods` (ManyToMany -> `PaymentMethod`)

  - `IndividualProfile` (JPA / SQL)
    - Extends `ProfileEntity` (maps to `individual_profiles`)
    - Fields: `name`, `cpf`
    - Relations: `cart` (OneToOne -> `Cart`), `orders` (OneToMany -> `Order`)

  - `Category` (JPA / SQL)
    - Fields: `categoryName`, `businessSector`
    - Relations: `variationCategories` (ManyToMany -> `VariationCategoryGlobal`)

  - `VariationCategoryEntity` (JPA / SQL, JOINED inheritance)
    - Abstract base for `VariationCategoryGlobal` and `VariationCategorySeller`
    - Fields: `name`, `type`, `measureUnit`, `description`, `active`

  - `PaymentMethod` (JPA / SQL)
    - Fields: `name`, `iconUrl`
    - Relations: `companyProfiles` (ManyToMany -> `CompanyProfile`)

  - `Cart` (JPA / SQL)
    - Fields: none scalar aside from audit
    - Relations: `profile` (OneToOne -> `IndividualProfile`), `items` (OneToMany -> `CartItem`)

  - `CartItem` (JPA / SQL)
    - Fields (snapshots): `productId`, `productVariationId`, `productName`, `itemQuantity`, `unitPriceSnapshot`, `status` (enum `ReservationStatus`)
    - Relations: `cart` (ManyToOne -> `Cart`)

  - `Order` (JPA / SQL)
    - Fields: `pickUpcode`, `status` (enum `OrderStatus`), `orderTotal`
    - Relations: `orderItems` (OneToMany -> `OrderItem`), `customer` (ManyToOne -> `IndividualProfile`), `company` (ManyToOne -> `CompanyProfile`)

  - `OrderItem` (JPA / SQL)
    - Snapshot fields: `productId`, `productVariationId`, `productName`, `productVariationValue`, `productPrice`, `variationAdditionalPriceSnapshot`, `finalUnitPriceSnapshot`, `orderQuantity`, `subtotal`
    - Relations: `order` (ManyToOne -> `Order`)

  - `Appointment` (JPA / SQL)
    - Fields: `type` (enum), `status` (enum), `scheduledDate`, `notes`, `orderId` (UUID ref), `serviceId` (UUID ref)
    - Relations: `customer` (ManyToOne -> `IndividualProfile`), `company` (ManyToOne -> `CompanyProfile`)


  **MongoDB (Documents / NoSQL)**
  - `Product` (Mongo document)
    - Fields: `productName`, `description`, `basePrice`, `totalStockCache`, `companyId`, `categoryId`, `variations` (DBRef list to `ProductVariation`), `locationReference`

  - `ProductVariation` (Mongo document)
    - Fields: `productId`, `variationCategoryId`, `variationCategoryType`, `valueType`, `variationValue`, `variationAdditionalPrice`, `stockQuantity`, `available`

  - `ServiceOffering` (Mongo document)
    - Fields: `serviceName`, `description`, `price`, `durationMinutes`, `available`, `model`, `companyId`, `categoryId`, `locationReference`

  - `AuditLog` / other logs (Mongo document)
    - Flexible log documents used for audit, metrics and trace data

  **Notes and guidelines**
  - Keep PostgreSQL (JPA) for transactional, referential data (users, profiles, carts, orders, taxonomy).
  - Keep MongoDB for flexible, high-read catalog & logs (products, variations, services, audit logs).
  - Always reference catalog items from JPA entities by UUID (never create JPA relations to Mongo documents).
  - When adding a new domain, follow the established module layout: `controller`, `dto`, `mapper`, `model`, `ports`, `repository`, `service` and choose the correct persistence type according to the rules above.


