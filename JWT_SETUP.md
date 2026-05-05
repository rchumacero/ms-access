# JWT Setup

## Dependencias

Las dependencias JWT estan comentadas en `pom.xml` para desarrollo.

Para habilitar:

1. Descomentar en `pom.xml`:
   - `quarkus-smallrye-jwt`
   - `quarkus-smallrye-jwt-build`
2. Agregar configuracion en `application.properties`:

```properties
quarkus.smallrye-jwt.enabled=true
mp.jwt.verify.publickey.location=classpath:/META-INF/resources/publicKey.pem
mp.jwt.verify.issuer=https://kplian.com
```

3. Proteger endpoints con `@RolesAllowed`.
