# syntax=docker/dockerfile:1

### =========================
### Build stage
### =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Install curl (needed sometimes by mvnw)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy Maven wrapper + pom first (better layer caching)
COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml

# 🔥 Fix: give execute permission to mvnw
RUN chmod +x mvnw

# Pre-fetch dependencies
RUN ./mvnw -q -DskipTests dependency:go-offline

# Copy source code
COPY src src

# Build fat jar
RUN ./mvnw -q -DskipTests package


### =========================
### Runtime stage
### =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Spring Boot default port
EXPOSE 8080

# Copy the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Run app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
