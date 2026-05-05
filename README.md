# MS Access Microservice

Microservicio Quarkus para la gestion de perfiles, roles, recursos y asignaciones segun el modelo entidad-relacion.

## Requisitos

- Java 21
- Maven 3.8+
- Docker (opcional para PostgreSQL)

## Configuracion rapida

1. Copiar y ajustar `.env.dev`, `.env.test`, `.env.prod` segun el entorno.
2. Levantar PostgreSQL con `docker-compose up -d`.
3. Iniciar el servicio:

```bash
mvn quarkus:dev -Pdev
```

## Endpoints base

El root path es `/api/v1`. Ejemplos:

- `GET /api/v1/profiles`
- `GET /api/v1/roles`
- `GET /api/v1/resources`
- `GET /api/v1/interins`
- `GET /api/v1/user-profiles`
- `GET /api/v1/profile-roles`
- `GET /api/v1/role-resources`

## Documentacion

Ver los archivos:

- `QUICK_START.md`
- `ARQUITECTURA_RECOMENDADA.md`
- `I18N_USAGE.md`
- `OPENTELEMETRY_ERROR_HANDLING.md`
- `JWT_SETUP.md`
- `POSTMAN_COLLECTION.md`
