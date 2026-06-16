# Project Launch & Setup Guide: MS Access Microservice

This guide contains the step-by-step instructions to check prerequisites, configure, and launch the **MS Access Microservice** (Quarkus) on your system.

---

## 1. Prerequisites Checklist & Status

We have verified that the necessary tools are installed and available on your local system:

| Prerequisite | Required Version | Installed Version | Status |
| :--- | :--- | :--- | :--- |
| **Java (JDK)** | 21 (LTS) | `21.0.3` (Oracle Corporation) | **OK** |
| **Maven** | 3.8+ | `3.9.11` | **OK** |
| **Docker** | Optional (for DB/services) | `20.10.24` | **OK** |

---

## 2. Step-by-Step Setup Instructions

Follow these steps to configure and run the service locally:

### Step 2.1: Environment Variables Configuration
The application relies on profile-specific environment variables for connecting to the database, Kafka, and OpenTelemetry.

1. Create a local environment configuration (or adjust the existing `.env.dev`):
   ```bash
   cp .env.dev .env.local
   ```
2. Verify the contents of `.env.dev`:
   * `DB_HOST`: Host of the PostgreSQL database (`localhost` for local dev).
   * `DB_PORT`: Port of the PostgreSQL database (`1100` is default for dev).
   * `DB_NAME`: Database name (`dbqk_dev`).
   * `DB_USERNAME`: Database user (`postgres`).
   * `DB_PASSWORD`: Database password (`postgres`).
   * `OTEL_ENABLED`: Set to `true` or `false` to toggle OpenTelemetry.

### Step 2.2: Launch the Database Container
If you do not have a running PostgreSQL instance on port `1100`, you can start one using the provided Docker Compose file:

1. Launch PostgreSQL:
   ```bash
   docker-compose -f docker-compose2.yml up -d
   ```
2. Verify that the container is running:
   ```bash
   docker ps | grep ms-access-postgres
   ```
   *(Note: The system already has a PostgreSQL container running on port `1100` named `postgresql-db-1`)*

### Step 2.3: Build & Compile the Application
Before running, you can verify that the project builds and all dependencies compile successfully:

```bash
mvn clean compile
```

---

## 3. Launching the Microservice

Start the Quarkus application in development mode with the `dev` profile. This will automatically load the configuration defined in `.env.dev`:

```bash
mvn quarkus:dev -Pdev
```

### Dev Mode Features:
* **Live Coding / Hot Reload**: Any changes made to Java classes or resources will be compiled and reloaded automatically upon the next HTTP request.
* **Dev UI**: You can access Quarkus Dev UI at `http://localhost:8001/q/dev/`.

---

## 4. Verification & Endpoint Access

Once the service is running, you can access these key resources to verify the setup:

* **Swagger UI / API Docs**:
  * [Swagger UI](http://localhost:8001/api/v1/swagger-ui)
  * [OpenAPI JSON Document](http://localhost:8001/api/v1/api-docs)
* **Application Health Check**:
  * [Liveness / Readiness Probe](http://localhost:8001/api/v1/health)
* **Base API Paths**:
  * Profiles: `GET http://localhost:8001/api/v1/profiles`
  * Roles: `GET http://localhost:8001/api/v1/roles`
  * Resources: `GET http://localhost:8001/api/v1/resources`

---

## 5. Advanced Integrations

For more specific setups and guidelines, refer to the following repository documentation files:

* **JWT & Security Configuration**: [JWT_SETUP.md](file:///Users/admin/DevelopmentRCM/KPLIAN/BACKEND/ms-access/JWT_SETUP.md)
* **Recommended Architecture**: [ARQUITECTURA_RECOMENDADA.md](file:///Users/admin/DevelopmentRCM/KPLIAN/BACKEND/ms-access/ARQUITECTURA_RECOMENDADA.md)
* **OpenTelemetry & Error Handling**: [OPENTELEMETRY_ERROR_HANDLING.md](file:///Users/admin/DevelopmentRCM/KPLIAN/BACKEND/ms-access/OPENTELEMETRY_ERROR_HANDLING.md)
* **Internationalization / i18n**: [I18N_USAGE.md](file:///Users/admin/DevelopmentRCM/KPLIAN/BACKEND/ms-access/I18N_USAGE.md)
* **Postman Collection**: [POSTMAN_COLLECTION.md](file:///Users/admin/DevelopmentRCM/KPLIAN/BACKEND/ms-access/POSTMAN_COLLECTION.md)
