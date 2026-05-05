# Quick Start

## 1. Variables de entorno

```bash
cp .env.dev .env.local
```

Actualiza `.env.dev` segun tu entorno.

## 2. Base de datos

```bash
docker-compose up -d
```

## 3. Ejecutar en desarrollo

```bash
mvn quarkus:dev -Pdev
```

## 4. Swagger

- `http://localhost:8080/api/v1/swagger-ui`

## 5. OpenAPI

- `http://localhost:8080/api/v1/api-docs`
