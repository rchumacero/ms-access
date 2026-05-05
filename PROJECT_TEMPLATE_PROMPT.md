# Prompt para Generar Microservicio Quarkus - Plantilla Completa

## 📋 Contexto y Objetivo

Necesito crear un nuevo microservicio en **Java Quarkus** que siga exactamente la misma arquitectura, patrones y características técnicas de un proyecto de referencia. El microservicio debe estar listo para producción y seguir las mejores prácticas de desarrollo.

## 🏗️ Arquitectura Domain-Driven Design (DDD)

### Estructura de Paquetes Obligatoria

El proyecto DEBE seguir esta estructura de paquetes estricta:

```
com.{company}.{microservice-name}
├── api/                           # Capa de Presentación (REST Controllers)
│   ├── config/                    # Configuraciones (filtros, interceptores)
│   │   ├── LocaleRequestFilter.java
│   │   ├── LocaleResponseFilter.java
│   │   └── ValidationLocaleResolver.java
│   ├── dto/                       # Data Transfer Objects
│   │   ├── request/              # DTOs para peticiones (con validaciones Bean Validation)
│   │   └── response/              # DTOs para respuestas
│   ├── exception/                 # Manejadores de excepciones
│   │   └── GlobalExceptionHandler.java
│   ├── mapper/                    # Mappers entre DTOs y Entities
│   ├── resource/                  # REST Resources (JAX-RS/Jakarta REST)
│   └── service/                   # Servicios de API (i18n, etc.)
│       └── I18nService.java
│
├── domain/                        # Capa de Dominio (Lógica de Negocio)
│   ├── exception/                 # Excepciones de Dominio
│   │   ├── BusinessException.java
│   │   ├── InfrastructureException.java
│   │   ├── SystemException.java
│   │   └── I18nBusinessException.java
│   ├── model/                     # Entidades de Dominio (JPA Entities)
│   │   └── Audit.java            # Clase base con campos de auditoría
│   └── service/                   # Servicios de Negocio (Business Logic)
│
├── infrastructure/                # Capa de Infraestructura
│   └── persistence/               # Acceso a Datos
│       └── repository/            # JPA Repositories (SOLO queries y CRUD básico)
│
└── util/                          # Utilidades compartidas
    └── Constants.java
```

### Principios de Arquitectura

1. **Separación de Responsabilidades**:
   - **API Layer**: Solo manejo HTTP, validación de entrada, serialización
   - **Domain Layer**: Toda la lógica de negocio
   - **Infrastructure Layer**: Solo acceso a datos (repositories NO tienen lógica de negocio)

2. **Repositories**: 
   - SOLO métodos de acceso a datos (find, count, exists, queries)
   - NO deben tener lógica de negocio
   - Usar PanacheRepository para herencia de métodos CRUD

3. **Services**:
   - Toda la lógica de negocio va aquí
   - Anotados con `@ApplicationScoped` y `@Transactional`
   - Validaciones de negocio, transformaciones, reglas de dominio

4. **Resources**:
   - Solo manejan HTTP requests/responses
   - Inyectan Services y Mappers
   - Usan DTOs para entrada/salida
   - Anotados con OpenAPI para documentación

## 🔧 Stack Tecnológico

### Versiones y Dependencias

- **Java**: 21
- **Quarkus**: 3.26.1
- **Maven**: 3.8+
- **PostgreSQL**: 17+ (driver JDBC)
- **Hibernate ORM + Panache**: Para persistencia
- **Bean Validation**: Para validación de DTOs
- **OpenAPI/Swagger**: Para documentación de API
- **OpenTelemetry**: Para trazabilidad distribuida
- **SmallRye JWT**: Para autenticación (desactivado en desarrollo)
- **SmallRye Health**: Para health checks
- **SmallRye Metrics**: Para métricas

### Dependencias Maven Requeridas

```xml
<!-- Quarkus Core -->
- quarkus-arc
- quarkus-rest
- quarkus-rest-jackson

<!-- Database -->
- quarkus-hibernate-orm-panache
- quarkus-jdbc-postgresql
- quarkus-hibernate-orm

<!-- Security JWT (comentadas para desarrollo) -->
- quarkus-smallrye-jwt (comentada)
- quarkus-smallrye-jwt-build (comentada)

<!-- OpenAPI / Swagger -->
- quarkus-smallrye-openapi
- quarkus-swagger-ui

<!-- OpenTelemetry -->
- quarkus-opentelemetry
- quarkus-logging-json

<!-- Validation -->
- quarkus-hibernate-validator

<!-- Health & Metrics -->
- quarkus-smallrye-health
- quarkus-smallrye-metrics

<!-- Testing -->
- quarkus-junit5 (test scope)
- rest-assured (test scope)
```

## 🌍 Internacionalización (i18n)

### Componentes Requeridos

1. **I18nService** (`api/service/I18nService.java`):
   - `@ApplicationScoped`
   - Detecta locale desde header `Accept-Language`
   - Usa ThreadLocal para almacenar locale por petición
   - Métodos: `get(String key, Object... params)`, `getLocale()`, `hasKey(String key, Locale locale)`
   - Fallback a español (es-ES) por defecto

2. **LocaleRequestFilter** (`api/config/LocaleRequestFilter.java`):
   - `@Provider` implementa `ContainerRequestFilter`
   - Parsea header `Accept-Language`
   - Establece locale en ThreadLocal

3. **LocaleResponseFilter** (`api/config/LocaleResponseFilter.java`):
   - `@Provider` implementa `ContainerResponseFilter`
   - Limpia ThreadLocal al finalizar petición

4. **Archivos de Propiedades**:
   - `resources/i18N/message.properties` (Inglés - default)
   - `resources/i18N/message_es.properties` (Español)
   - `resources/ValidationMessages.properties` (Bean Validation - Inglés)
   - `resources/ValidationMessages_es.properties` (Bean Validation - Español)

### Estructura de Claves i18n

```
# Mensajes comunes
common.success, common.error, common.not_found, common.required, common.invalid

# Validación
validation.required, validation.not_blank, validation.size.min, validation.size.max, 
validation.size.between, validation.pattern, validation.email, etc.

# Errores por entidad
error.{entity}.not_found
error.{entity}.code.required
error.{entity}.code.exists
error.{entity}.invalid
error.{entity}.{field}.required
error.{entity}.{field}.invalid

# Errores de negocio
error.business.generic, error.business.validation, error.business.page.invalid, etc.

# Errores HTTP
error.http.not_found, error.http.bad_request, error.http.internal_error, etc.

# Errores de infraestructura
error.infrastructure.database, error.infrastructure.external_service, etc.

# Errores de sistema
error.system.unexpected, error.system.configuration, etc.

# DTOs
dto.{entity}.{field}.required
dto.{entity}.{field}.size
dto.{entity}.{field}.pattern
```

### Uso en Código

**En Services:**
```java
@Inject
I18nService i18nService;

throw new I18nBusinessException(
    i18nService,
    "error.product.not_found",
    "PRODUCT_NOT_FOUND",
    productId
);
```

**En DTOs:**
```java
@NotBlank(message = "{dto.product.code.required}")
@Size(min = 3, max = 50, message = "{dto.product.code.size}")
private String code;
```

### Translation table

CREATE TABLE tlocal_translation (
	entity varchar(50) NOT NULL,
	entity_id int8 NOT NULL,
	field varchar(50) NOT NULL,
	language_code varchar(15) NOT NULL,
	"text" text NULL,
	"domain" varchar(50) NOT NULL,
	CONSTRAINT tlocal_translation_pkey PRIMARY KEY (domain, entity, entity_id, field, language_code)
);

- En los listados deberia aplicarse consulta con join a esta tabla para obtener el texto traducido:
            SELECT p.id, COALESCE(lt.text, p.name) AS name
            FROM public.tparameter p
            LEFT JOIN public.tlocal_translation lt
                   ON lt.domain = :domain
                  AND lt.entity = 'parameter'
                  AND lt.entity_id = p.id
                  AND lt.field = 'name'
                  AND lt.language_code = :langCode
            WHERE p.id IN (:ids)

## 🚨 Manejo de Errores con OpenTelemetry

### Niveles de Error

1. **BusinessException**:
   - Errores de negocio esperados
   - NO se registra en OpenTelemetry
   - Solo se registra en logs
   - Ejemplos: validaciones, reglas de negocio, entidades no encontradas

2. **InfrastructureException**:
   - Errores de infraestructura (BD, servicios externos, red)
   - SÍ se registra en OpenTelemetry con `error.type=infrastructure`
   - Crea span con status ERROR y atributos detallados

3. **SystemException**:
   - Errores críticos del sistema
   - SÍ se registra en OpenTelemetry con `error.type=system`
   - Crea span con status ERROR y atributos detallados

### GlobalExceptionHandler

- Implementa `ExceptionMapper<Exception>`
- Inyecta `I18nService` y `Tracer` (OpenTelemetry)
- Traduce mensajes automáticamente según locale
- Registra errores en OpenTelemetry solo para InfrastructureException y SystemException
- Atributos en span: `error`, `error.type`, `error.code`, `error.message`, `error.class`, `error.stack_trace`

### Excepciones Requeridas

```java
// domain/exception/BusinessException.java
// domain/exception/InfrastructureException.java  
// domain/exception/SystemException.java
// domain/exception/I18nBusinessException.java
```

## 📊 OpenTelemetry

### Configuración Requerida

```properties
quarkus.opentelemetry.enabled=${OTEL_ENABLED:true}
quarkus.opentelemetry.tracer.exporter.otlp.endpoint=${OTEL_EXPORTER_OTLP_ENDPOINT:http://localhost:4317}
quarkus.opentelemetry.tracer.exporter.otlp.protocol=${OTEL_EXPORTER_OTLP_PROTOCOL:grpc}
quarkus.opentelemetry.service-name=${OTEL_SERVICE_NAME:ms-name}
quarkus.opentelemetry.resource-attributes=service.name=${OTEL_SERVICE_NAME:ms-name},service.version=${quarkus.application.version:1.0.0}
```

### Integración en GlobalExceptionHandler

- Inyectar `Tracer` de OpenTelemetry
- Crear/actualizar spans solo para errores de infraestructura y sistema
- Agregar atributos de error al span
- Marcar span con `StatusCode.ERROR`
- Registrar evento de excepción

## 🔐 Seguridad JWT

### Configuración

- **Desarrollo**: Dependencias JWT comentadas en `pom.xml`
- **Producción**: Descomentar dependencias y configurar:
  ```properties
  quarkus.smallrye-jwt.enabled=true
  mp.jwt.verify.publickey.location=classpath:/META-INF/resources/publicKey.pem
  mp.jwt.verify.issuer=https://kplian.com
  ```

### Comportamiento

- En desarrollo: Endpoints públicos (sin autenticación)
- En producción: Usar `@RolesAllowed` en Resources para proteger endpoints

## ⚙️ Perfiles Maven y Variables de Entorno

### Estructura de Perfiles

El proyecto DEBE incluir perfiles Maven para diferentes entornos:

```xml
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <env.file>.env.dev</env.file>
        </properties>
    </profile>
    <profile>
        <id>test</id>
        <properties>
            <env.file>.env.test</env.file>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <env.file>.env.prod</env.file>
        </properties>
    </profile>
</profiles>
```

### Archivos .env Requeridos

Crear en la raíz del proyecto:

**`.env.dev`**:
```properties
DB_HOST=localhost
DB_PORT=1100
DB_NAME=dbqk_dev
DB_USERNAME=postgres
DB_PASSWORD=postgres
OTEL_ENABLED=true
OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317
OTEL_SERVICE_NAME=ms-name-dev
```

**`.env.test`**:
```properties
DB_HOST=localhost
DB_PORT=1100
DB_NAME=dbqk_test
DB_USERNAME=postgres
DB_PASSWORD=postgres
OTEL_ENABLED=true
OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4317
OTEL_SERVICE_NAME=ms-name-test
```

**`.env.prod`**:
```properties
DB_HOST=prod-db-host
DB_PORT=5432
DB_NAME=dbqk_prod
DB_USERNAME=${DB_USERNAME}
DB_PASSWORD=${DB_PASSWORD}
OTEL_ENABLED=true
OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector:4317
OTEL_SERVICE_NAME=ms-name-prod
```

### Carga de Variables de Entorno

**Opción 1: Plugin dotenv-maven-plugin** (Recomendado)

Agregar al `pom.xml` en la sección `<build><plugins>`:

```xml
<plugin>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-maven-plugin</artifactId>
    <version>1.0.0</version>
    <executions>
        <execution>
            <id>load-env</id>
            <phase>initialize</phase>
            <goals>
                <goal>dotenv</goal>
            </goals>
            <configuration>
                <dotenvFile>${env.file}</dotenvFile>
            </configuration>
        </execution>
    </executions>
</plugin>
```

**Opción 2: Usar variables de entorno del sistema**

Si no se usa el plugin, las variables deben estar en el sistema y Quarkus las leerá automáticamente desde `application.properties` usando `${VARIABLE_NAME:default_value}`.

### Configuración de Perfiles en pom.xml

Agregar después de `<properties>` y antes de `<dependencyManagement>`:

```xml
<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <env.file>.env.dev</env.file>
        </properties>
    </profile>
    <profile>
        <id>test</id>
        <properties>
            <env.file>.env.test</env.file>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <env.file>.env.prod</env.file>
        </properties>
    </profile>
</profiles>
```

### Uso de Perfiles

```bash
# Desarrollo (por defecto)
mvn quarkus:dev

# Desarrollo explícito
mvn quarkus:dev -Pdev

# Test
mvn quarkus:dev -Ptest

# Producción
mvn quarkus:dev -Pprod
```

## 📝 Configuración application.properties

### Estructura Requerida

```properties
# Application Configuration
quarkus.application.name={microservice-name}
quarkus.application.version=1.0.0

# HTTP Configuration
quarkus.http.port=8080
quarkus.http.root-path=/api/v1
quarkus.http.cors.origins=*
quarkus.http.cors.headers=accept,authorization,content-type,x-requested-with

# Database Configuration (usar variables de entorno)
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=${DB_USERNAME:postgres}
quarkus.datasource.password=${DB_PASSWORD:postgres}
quarkus.datasource.jdbc.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:1100}/${DB_NAME:dbqk}
quarkus.datasource.jdbc.max-size=10
quarkus.datasource.jdbc.min-size=2

# Hibernate Configuration
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.log.sql=false
quarkus.hibernate-orm.log.bind-parameters=false

# Security Configuration (JWT comentado para desarrollo)
# Instrucciones para producción incluidas como comentarios

# OpenAPI / Swagger Configuration
quarkus.smallrye-openapi.info-title={Microservice Name} API
quarkus.smallrye-openapi.info-version=1.0.0
quarkus.smallrye-openapi.path=/api-docs
quarkus.swagger-ui.path=/swagger-ui
quarkus.swagger-ui.always-include=true

# OpenTelemetry Configuration
quarkus.opentelemetry.enabled=${OTEL_ENABLED:true}
quarkus.opentelemetry.tracer.exporter.otlp.endpoint=${OTEL_EXPORTER_OTLP_ENDPOINT:http://localhost:4317}
quarkus.opentelemetry.tracer.exporter.otlp.protocol=${OTEL_EXPORTER_OTLP_PROTOCOL:grpc}
quarkus.opentelemetry.service-name=${OTEL_SERVICE_NAME:ms-name}
quarkus.opentelemetry.resource-attributes=service.name=${OTEL_SERVICE_NAME:ms-name},service.version=${quarkus.application.version:1.0.0}

# Logging Configuration
quarkus.log.level=INFO
quarkus.log.category."com.{company}".level=DEBUG
quarkus.log.category."org.hibernate".level=WARN
quarkus.log.category."io.quarkus".level=INFO
quarkus.log.console.json=false
quarkus.log.console.format=%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p [%c{2.}] (%t) %s%e%n

# Health & Metrics
quarkus.smallrye-health.root-path=/health
quarkus.smallrye-metrics.enabled=true
quarkus.smallrye-metrics.path=/metrics

# i18n Configuration
quarkus.locales=en, es
quarkus.default-locale=es-ES
```

## 📝 Sistema de Auditoría

### Clase Base Audit

**TODAS las entidades DEBEN heredar de `Audit`** en lugar de `PanacheEntityBase`.

La clase `Audit` (`domain/model/Audit.java`) es una clase abstracta con `@MappedSuperclass` que incluye:

**Campos de Auditoría:**
- `createdAt` (LocalDateTime, nullable = false, updatable = false) - Fecha de creación
- `createdBy` (String, length = 100, updatable = false) - Usuario que creó
- `updatedAt` (LocalDateTime) - Fecha de última actualización
- `updatedBy` (String, length = 100) - Usuario que actualizó
- `deletedAt` (LocalDateTime) - Fecha de eliminación lógica (soft delete)
- `deletedBy` (String, length = 100) - Usuario que eliminó
- `status` (String, length = 50) - Estado del registro (ACTIVE, DELETED, INACTIVE, etc.)

**Métodos Helper:**
- `setAuditForCreate(String createdBy)`: Establece createdAt, createdBy, status=ACTIVE
- `setAuditForUpdate(String updatedBy)`: Establece updatedAt, updatedBy
- `setAuditForDelete(String deletedBy)`: Establece deletedAt, deletedBy, status=DELETED (soft delete)
- `isDeleted()`: Verifica si está eliminado lógicamente
- `restore()`: Restaura un registro eliminado (limpia deletedAt, deletedBy, status=ACTIVE)

### Implementación de Audit

```java
@MappedSuperclass
public abstract class Audit extends PanacheEntityBase {

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 100, updatable = false)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", length = 100)
    private String deletedBy;

    @Column(name = "status", length = 50)
    private String status;

    // Getters, Setters y métodos helper...
}
```

### Uso en Entidades

```java
@Entity
@Table(name = "tproduct") //El nombre de la tabla debe ser en minusculas y debe empezar con t
public class Product extends Audit {  // Hereda de Audit, NO de PanacheEntityBase
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    // Campos específicos de la entidad...
    // Los campos de auditoría se heredan automáticamente
}
```

### Uso en Services

**Crear:**
```java
public Product create(Product product) {
    // Validaciones...
    
    // Establecer campos de auditoría
    product.setAuditForCreate(getCurrentUser());
    
    repository.persist(product);
    return product;
}
```

**Actualizar:**
```java
public Product update(UUID id, Product product) {
    Product existing = findById(id);
    
    // Actualizar campos...
    
    // Establecer campos de auditoría
    existing.setAuditForUpdate(getCurrentUser());
    
    return existing;
}
```

**Eliminar (Soft Delete):**
```java
public void delete(UUID id) {
    Product product = findById(id);
    
    // Soft delete: no elimina físicamente
    product.setAuditForDelete(getCurrentUser());
}
```

**Restaurar:**
```java
public void restore(UUID id) {
    Product product = findByIdIncludingDeleted(id);
    product.restore();
    product.setAuditForUpdate(getCurrentUser());
}
```

### Queries con Soft Delete

**TODAS las queries deben excluir registros eliminados:**

```java
// Repository
public Optional<Product> findByIdOptional(UUID id) {
    return find("id = ?1 and deletedAt is null", id).firstResultOptional();
}

public Optional<Product> findByCode(String code) {
    return find("code = ?1 and deletedAt is null", code).firstResultOptional();
}

public List<Product> findAllWithFilters(...) {
    StringBuilder query = new StringBuilder("deletedAt is null");
    // ... agregar otros filtros
}
```

```java
// Service
public List<Product> findAll() {
    return repository.find("deletedAt is null").list();
}

public Product findById(UUID id) {
    return repository.find("id = ?1 and deletedAt is null", id)
        .firstResultOptional()
        .orElseThrow(...);
}
```

### Obtener Usuario Actual

```java
private String getCurrentUser() {
    // En desarrollo: retornar "system"
    // En producción: obtener del JWT o contexto de seguridad
    // Ejemplo con JWT:
    // SecurityIdentity identity = SecurityIdentity.current();
    // return identity.getPrincipal().getName();
    return "system";
}
```

## 🗄️ Entidades JPA - Reglas Importantes

### Clase Base Audit

**TODAS las entidades DEBEN heredar de `Audit`** en lugar de `PanacheEntityBase`.

La clase `Audit` (`domain/model/Audit.java`) incluye campos de auditoría:
- `createdAt` (LocalDateTime, nullable = false, updatable = false)
- `createdBy` (String, length = 100, updatable = false)
- `updatedAt` (LocalDateTime)
- `updatedBy` (String, length = 100)
- `deletedAt` (LocalDateTime) - Para soft delete
- `deletedBy` (String, length = 100)
- `status` (String, length = 50) - Estado del registro (ACTIVE, DELETED, etc.)

**Métodos helper en Audit:**
- `setAuditForCreate(String createdBy)`: Establece createdAt, createdBy, status=ACTIVE
- `setAuditForUpdate(String updatedBy)`: Establece updatedAt, updatedBy
- `setAuditForDelete(String deletedBy)`: Establece deletedAt, deletedBy, status=DELETED (soft delete)
- `isDeleted()`: Verifica si está eliminado lógicamente
- `restore()`: Restaura un registro eliminado

### Generación de IDs

- **UUID**: Usar `@GeneratedValue(strategy = GenerationType.UUID)`
- **NO usar** `GenerationType.IDENTITY` con UUID (PostgreSQL no lo soporta)

### Tipos de Datos

- **Double/Float**: NO usar `precision` y `scale` en `@Column` (PostgreSQL no lo soporta)
- Si necesitas precisión decimal exacta, usar `BigDecimal` con `columnDefinition = "NUMERIC(10,2)"`

### Nombres de Columnas

- Evitar palabras reservadas de SQL (ej: `order` → usar `order_task`)
- Usar snake_case para nombres de columnas

### Ejemplo de Entidad

```java
@Entity
@Table(name = "tentity")
public class Entity extends Audit {  // Hereda de Audit, NO de PanacheEntityBase
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "code", nullable = false, length = 50, unique = true)
    private String code;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    // NO usar precision/scale con Double
    @Column(name = "amount")
    private Double amount;
    
    // Los campos de auditoría (createdAt, createdBy, updatedAt, updatedBy, 
    // deletedAt, deletedBy, status) se heredan automáticamente de Audit
    // Getters y Setters solo para campos específicos de la entidad
}
```

## 📦 Estructura de Capas - Detalles

### 1. API Layer (`api/`)

**Resources**:
- `@Path("/api/v1/{resource}")`
- `@ApplicationScoped`
- Inyectan Services y Mappers
- Usan DTOs para entrada/salida
- Anotaciones OpenAPI para documentación
- Manejan validación con `@Valid`

**DTOs**:
- Request DTOs: Validaciones Bean Validation con mensajes i18n
- Response DTOs: Solo datos necesarios para el cliente
- NO incluir lógica de negocio

**Mappers**:
- `@ApplicationScoped`
- Métodos: `toEntity(DTO)`, `toResponseDTO(Entity)`, `toResponseDTOs(List<Entity>)`

### 2. Domain Layer (`domain/`)

**Services**:
- `@ApplicationScoped`
- `@Transactional`
- Toda la lógica de negocio
- Validaciones de negocio
- Usan I18nService para mensajes
- Lanzan I18nBusinessException para errores de negocio
- Lanzan InfrastructureException para errores de BD/infraestructura
- Lanzan SystemException para errores inesperados

**Models**:
- Extienden `PanacheEntityBase`
- Anotaciones JPA estándar
- UUID como ID con `GenerationType.UUID`

### 3. Infrastructure Layer (`infrastructure/`)

**Repositories**:
- Implementan `PanacheRepository<Entity>`
- `@ApplicationScoped`
- SOLO métodos de acceso a datos
- Métodos comunes: `findByIdOptional(UUID id)`, `findByCode(String code)`, `existsByCode(String code)`
- NO lógica de negocio

## 📚 Archivos de Documentación Requeridos

Crear los siguientes archivos de documentación:

1. **README.md**: Descripción general, setup, uso básico
2. **QUICK_START.md**: Guía rápida de inicio
3. **ARQUITECTURA_RECOMENDADA.md**: Documentación de arquitectura
4. **I18N_USAGE.md**: Guía de uso de i18n
5. **OPENTELEMETRY_ERROR_HANDLING.md**: Guía de manejo de errores con OpenTelemetry
6. **JWT_SETUP.md**: Configuración de JWT
7. **POSTMAN_COLLECTION.md**: Ejemplos de peticiones Postman
8. **docker-compose.yml**: Configuración de PostgreSQL

## ✅ Checklist de Implementación

### Configuración Base
- [ ] `pom.xml` con todas las dependencias
- [ ] Perfiles Maven (dev, test, prod)
- [ ] Plugin dotenv-maven-plugin
- [ ] Archivos `.env.dev`, `.env.test`, `.env.prod`
- [ ] `application.properties` completo
- [ ] `docker-compose.yml` para PostgreSQL

### Arquitectura
- [ ] Estructura de paquetes correcta (api, domain, infrastructure, util)
- [ ] Clase base `Audit` creada con todos los campos de auditoría
- [ ] TODAS las entidades heredan de `Audit` (NO de `PanacheEntityBase`)
- [ ] Repositories sin lógica de negocio
- [ ] Services con toda la lógica de negocio
- [ ] Services establecen campos de auditoría (setAuditForCreate, setAuditForUpdate, setAuditForDelete)
- [ ] Services implementan soft delete (no eliminan físicamente)
- [ ] Queries excluyen registros eliminados (deletedAt is null)
- [ ] Resources solo manejan HTTP

### i18n
- [ ] I18nService implementado
- [ ] LocaleRequestFilter y LocaleResponseFilter
- [ ] Archivos de propiedades (message.properties, message_es.properties)
- [ ] Archivos ValidationMessages (inglés y español)
- [ ] DTOs usando claves de traducción
- [ ] Services usando I18nBusinessException

### Manejo de Errores
- [ ] BusinessException, InfrastructureException, SystemException
- [ ] I18nBusinessException
- [ ] GlobalExceptionHandler con integración OpenTelemetry
- [ ] Registro de errores solo para Infrastructure y System

### OpenTelemetry
- [ ] Dependencia quarkus-opentelemetry
- [ ] Configuración en application.properties
- [ ] Integración en GlobalExceptionHandler
- [ ] Tracer inyectado correctamente

### JWT
- [ ] Dependencias comentadas en pom.xml
- [ ] Comentarios explicando cómo habilitar en producción
- [ ] Documentación JWT_SETUP.md

### Entidades
- [ ] UUID con GenerationType.UUID (NO IDENTITY)
- [ ] Sin precision/scale en campos Double
- [ ] Nombres de columnas sin palabras reservadas

### Documentación
- [ ] README.md completo
- [ ] QUICK_START.md
- [ ] Documentación de arquitectura
- [ ] Guías de uso (i18n, OpenTelemetry, JWT)
- [ ] Ejemplos Postman

## 🎯 Ejemplo de Entidad Completa

Para cada entidad, crear TODAS las capas:

1. **Model** (`domain/model/Entity.java`)
   - Extiende `Audit` (NO `PanacheEntityBase`)
   - UUID con `GenerationType.UUID`
   - Anotaciones JPA correctas
   - Hereda campos de auditoría automáticamente

2. **Repository** (`infrastructure/persistence/repository/EntityRepository.java`)
   - Implementa `PanacheRepository<Entity>`
   - Métodos: `findByIdOptional(UUID id)`, `findByCode(String code)`, `existsByCode(String code)`
   - SOLO acceso a datos

3. **Service** (`domain/service/EntityService.java`)
   - `@ApplicationScoped` y `@Transactional`
   - Inyecta Repository e I18nService
   - Métodos CRUD: `findAll()`, `findById(UUID id)`, `create(Entity)`, `update(UUID id, Entity)`, `delete(UUID id)`
   - Validaciones de negocio
   - Usa I18nBusinessException para errores de negocio
   - Establece campos de auditoría: `entity.setAuditForCreate(user)`, `entity.setAuditForUpdate(user)`, `entity.setAuditForDelete(user)`
   - `findAll()` debe excluir eliminados: `repository.find("deletedAt is null").list()`
   - Método `getCurrentUser()` para obtener usuario del contexto (JWT en producción)

4. **Request DTO** (`api/dto/request/EntityRequestDTO.java`)
   - Validaciones Bean Validation
   - Mensajes usando claves i18n: `message = "{dto.entity.field.required}"`

5. **Response DTO** (`api/dto/response/EntityResponseDTO.java`)
   - Solo campos necesarios para el cliente
   - Incluye ID (UUID)

6. **Mapper** (`api/mapper/EntityMapper.java`)
   - `@ApplicationScoped`
   - Métodos: `toEntity(RequestDTO)`, `toResponseDTO(Entity)`, `toResponseDTOs(List<Entity>)`

7. **Resource** (`api/resource/EntityResource.java`)
   - `@Path("/api/v1/{resource}")`
   - `@ApplicationScoped`
   - Inyecta Service y Mapper
   - Endpoints: GET (list, byId), POST, PUT, DELETE
   - Anotaciones OpenAPI para documentación
   - Usa `@Valid` en métodos POST/PUT

8. **Claves i18n** en archivos de propiedades
   - `error.entity.not_found`
   - `error.entity.code.required`
   - `error.entity.code.exists`
   - `error.entity.invalid`
   - `dto.entity.field.required`
   - `dto.entity.field.size`

## 🐳 Docker Compose

Incluir `docker-compose.yml` en la raíz del proyecto para PostgreSQL:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    container_name: {microservice-name}-postgres
    environment:
      POSTGRES_DB: ${DB_NAME:dbqk}
      POSTGRES_USER: ${DB_USERNAME:postgres}
      POSTGRES_PASSWORD: ${DB_PASSWORD:postgres}
    ports:
      - "${DB_PORT:1100}:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  postgres_data:
```

## 📋 Instrucciones Finales

1. Generar el proyecto completo con TODAS las capas en funcion de entidad relacion a adjuntar. Considerar que en todas las consultas de datos por GET o por otros, deben incluirse los datos de auditoria.
2. Incluir TODOS los componentes de infraestructura (i18n, OpenTelemetry, manejo de errores)
3. Configurar perfiles Maven y archivos .env
4. Asegurar que compile sin errores
5. Incluir documentación completa
6. Seguir exactamente la estructura y patrones descritos
7. Incluir docker-compose.yml para PostgreSQL

## 📝 Patrones de Código Específicos

### Resource (REST Controller)

```java
@Path("/api/v1/products")
@ApplicationScoped
@Tag(name = "Products", description = "Product management API")
public class ProductResource {

    @Inject
    ProductService productService;

    @Inject
    ProductMapper productMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all products")
    public Response getAll() {
        List<Product> products = productService.findAll();
        List<ProductResponseDTO> dtos = productMapper.toResponseDTOs(products);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get product by ID")
    public Response getById(
        @Parameter(description = "Product ID", required = true)
        @PathParam("id") UUID id
    ) {
        Product product = productService.findById(id);
        ProductResponseDTO dto = productMapper.toResponseDTO(product);
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new product")
    public Response create(@Valid ProductRequestDTO requestDTO) {
        Product product = productMapper.toEntity(requestDTO);
        Product created = productService.create(product);
        ProductResponseDTO responseDTO = productMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a product")
    public Response update(
        @Parameter(description = "Product ID", required = true)
        @PathParam("id") UUID id,
        @Valid ProductRequestDTO requestDTO
    ) {
        Product product = productMapper.toEntity(requestDTO);
        Product updated = productService.update(id, product);
        ProductResponseDTO responseDTO = productMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a product")
    public Response delete(
        @Parameter(description = "Product ID", required = true)
        @PathParam("id") UUID id
    ) {
        productService.delete(id);
        return Response.noContent().build();
    }
}
```

### Service

```java
@ApplicationScoped
@Transactional
public class ProductService {

    @Inject
    ProductRepository productRepository;

    @Inject
    I18nService i18nService;

    public List<Product> findAll() {
        // Excluir registros eliminados (soft delete)
        return productRepository.find("deletedAt is null").list();
    }

    public Product findById(UUID id) {
        return productRepository.find("id = ?1 and deletedAt is null", id)
            .firstResultOptional()
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.product.not_found",
                "PRODUCT_NOT_FOUND",
                id
            ));
    }

    public Product create(Product product) {
        validateProductForCreate(product);
        
        if (productRepository.existsByCode(product.getCode())) {
            throw new I18nBusinessException(
                i18nService,
                "error.product.code.exists",
                "PRODUCT_CODE_ALREADY_EXISTS",
                product.getCode()
            );
        }
        
        // Establecer campos de auditoría para creación
        product.setAuditForCreate(getCurrentUser());
        
        productRepository.persist(product);
        return product;
    }

    public Product update(UUID id, Product product) {
        Product existing = findById(id);
        
        // Actualizar campos...
        existing.setName(product.getName());
        
        // Establecer campos de auditoría para actualización
        existing.setAuditForUpdate(getCurrentUser());
        
        return existing;
    }

    public void delete(UUID id) {
        Product product = findById(id);
        
        // Soft delete: marcar como eliminado
        product.setAuditForDelete(getCurrentUser());
    }

    private void validateProductForCreate(Product product) {
        if (product == null) {
            throw new I18nBusinessException(
                i18nService,
                "error.product.invalid",
                "INVALID_PRODUCT"
            );
        }
        // Más validaciones...
    }

    /**
     * Obtiene el usuario actual del contexto de seguridad.
     * En desarrollo retorna "system", en producción debe obtenerse del JWT.
     */
    private String getCurrentUser() {
        // TODO: Implementar obtención del usuario desde JWT o contexto de seguridad
        return "system";
    }
}
```

### Repository

```java
@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public Optional<Product> findByIdOptional(UUID id) {
        // Incluir condición para excluir eliminados
        return find("id = ?1 and deletedAt is null", id).firstResultOptional();
    }

    public Optional<Product> findByCode(String code) {
        // Incluir condición para excluir eliminados
        return find("code = ?1 and deletedAt is null", code).firstResultOptional();
    }

    public boolean existsByCode(String code) {
        // Verificar existencia excluyendo eliminados
        return count("code = ?1 and deletedAt is null", code) > 0;
    }

    public List<Product> findAllWithFilters(...) {
        // Siempre incluir "deletedAt is null" en las queries
        StringBuilder query = new StringBuilder("deletedAt is null");
        // ... resto de filtros
    }
}
```

## 🔍 Validaciones Finales

El proyecto generado debe:
- ✅ Compilar sin errores
- ✅ Tener estructura DDD correcta
- ✅ i18n funcionando (español por defecto, inglés como alternativa)
- ✅ OpenTelemetry configurado (aunque el collector no esté corriendo)
- ✅ Manejo de errores diferenciado (Business vs Infrastructure vs System)
- ✅ JWT desactivado en desarrollo
- ✅ Perfiles Maven funcionando
- ✅ Variables de entorno cargándose desde .env
- ✅ Documentación completa
- ✅ Listo para consumir desde Postman
- ✅ Docker Compose incluido
- ✅ UUID con GenerationType.UUID (NO IDENTITY)
- ✅ Sin precision/scale en campos Double
- ✅ Clase base Audit implementada
- ✅ Todas las entidades heredan de Audit
- ✅ Campos de auditoría establecidos automáticamente en Services
- ✅ Soft delete implementado

## 🚀 Comandos de Uso

```bash
# Desarrollo (carga .env.dev)
mvn quarkus:dev -Pdev

# Test (carga .env.test)
mvn quarkus:dev -Ptest

# Producción (carga .env.prod)
mvn quarkus:dev -Pprod

# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Build para producción
mvn clean package -Pprod
```

---

**IMPORTANTE**: Este prompt debe generar un proyecto COMPLETO y FUNCIONAL que siga exactamente estos patrones y características. No dejar componentes incompletos o pendientes. El proyecto debe estar listo para usar inmediatamente después de la generación.
