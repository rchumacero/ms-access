# OpenTelemetry Error Handling

## Niveles de error

- BusinessException: no se reporta a OpenTelemetry.
- InfrastructureException: se reporta con `error.type=infrastructure`.
- SystemException: se reporta con `error.type=system`.

## Implementacion

`GlobalExceptionHandler` crea un span de error con atributos:

- `error`
- `error.type`
- `error.class`
- `error.message`

## Configuracion

Ver `src/main/resources/application.properties`.
