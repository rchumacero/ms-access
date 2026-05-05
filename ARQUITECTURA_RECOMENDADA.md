# Arquitectura Recomendada

El microservicio sigue DDD con separacion estricta de responsabilidades:

```
com.kplian.msaccess
├── api/
│   ├── config/
│   ├── dto/
│   ├── exception/
│   ├── mapper/
│   ├── resource/
│   └── service/
├── domain/
│   ├── exception/
│   ├── model/
│   └── service/
├── infrastructure/
│   └── persistence/
│       └── repository/
└── util/
```

## Principios

- API: solo HTTP, validaciones y DTOs.
- Domain: reglas de negocio y servicios.
- Infrastructure: acceso a datos (solo CRUD y queries).

## Soft delete

Todas las entidades heredan `Audit` e implementan borrado logico. Todas las consultas filtran `deletedAt is null`.
