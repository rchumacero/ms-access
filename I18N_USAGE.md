# I18N Usage

## Archivos

- `src/main/resources/i18N/message.properties`
- `src/main/resources/i18N/message_es.properties`
- `src/main/resources/ValidationMessages.properties`
- `src/main/resources/ValidationMessages_es.properties`

## Header

Enviar `Accept-Language` en las peticiones. Por defecto se usa `es-ES`.

## Ejemplo en servicios

```java
throw new I18nBusinessException(
    i18nService,
    "error.profile.not_found",
    "PROFILE_NOT_FOUND",
    profileId
);
```

## Ejemplo en DTO

```java
@NotBlank(message = "{dto.profile.code.required}")
private String code;
```
