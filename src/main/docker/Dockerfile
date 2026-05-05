# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
# Cache dependencies (slow step, done separately to speed up rebuilds)
RUN mvn dependency:go-offline
COPY src ./src
# Build the application (Package into a fast-jar)
RUN mvn package -DskipTests

# Stage 2: Runtime stage - Using Alpine for small footprint (~100MB instead of ~400MB)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /deployments

# Copy Quarkus fast-jar files from the build stage
COPY --from=build /app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build /app/target/quarkus-app/*.jar /deployments/
COPY --from=build /app/target/quarkus-app/app/ /deployments/app/
COPY --from=build /app/target/quarkus-app/quarkus/ /deployments/quarkus/

# Environment defaults
ENV LANGUAGE='en_US:en'
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

# Expose the application port (as defined in application.properties)
EXPOSE 8001

# Command to run the application using the JVM
CMD ["java", "-jar", "/deployments/quarkus-run.jar"]
