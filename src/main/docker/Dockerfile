# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiar configuración de dependencias para aprovechar la caché de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y empaquetar
COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /deployments

#Port definition
ARG APP_INTERNAL_PORT=8001

# Crear usuario sin privilegios para producción
RUN addgroup -S quarkus && adduser -S quarkus -G quarkus

# Copiar archivos del Quarkus fast-jar asignando propiedad al usuario no-root
COPY --chown=quarkus:quarkus --from=build /app/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=quarkus:quarkus --from=build /app/target/quarkus-app/*.jar /deployments/
COPY --chown=quarkus:quarkus --from=build /app/target/quarkus-app/app/ /deployments/app/
COPY --chown=quarkus:quarkus --from=build /app/target/quarkus-app/quarkus/ /deployments/quarkus/

# Cambiar al usuario sin privilegios
USER quarkus

# Variables de entorno por defecto
ENV LANGUAGE='en_US:en' \
    JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager" \
    JAVA_APP_JAR="/deployments/quarkus-run.jar"

EXPOSE ${APP_INTERNAL_PORT}

CMD ["java", "-jar", "/deployments/quarkus-run.jar"]