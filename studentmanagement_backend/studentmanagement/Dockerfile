# syntax=docker/dockerfile:1

### Build stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven wrapper + pom first for better caching
COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml

# Pre-fetch dependencies (will be cached unless pom changes)
RUN ./mvnw -q -DskipTests dependency:go-offline

# Copy sources
COPY src src

# Build fat jar
RUN ./mvnw -q -DskipTests package

### Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

# Spring Boot listens on 8080 by default
EXPOSE 8080

# Copy built jar
COPY --from=build /app/target/*.jar app.jar

# You should pass DB/JWT settings via env vars at runtime.
# Example:
#   -e SPRING_DATASOURCE_URL=...
#   -e SPRING_DATASOURCE_USERNAME=...
#   -e SPRING_DATASOURCE_PASSWORD=...
#   -e SECURITY_JWT_SECRET=...

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
