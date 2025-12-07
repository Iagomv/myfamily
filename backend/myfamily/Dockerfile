# Multi-target Dockerfile: supports `dev` and `prod` build targets

# Builder stage: compile the application using Maven
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /build

# Download dependencies early
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy sources and build
COPY src ./src
RUN mvn clean package -DskipTests


# Dev target: image suitable for iterative development (contains Maven)
FROM maven:3.9-eclipse-temurin-17-alpine AS dev
WORKDIR /app

# Copy sources so dev image can run with mvn spring-boot:run if desired
COPY pom.xml ./
COPY src ./src

EXPOSE 8080
CMD ["mvn", "spring-boot:run"]


# Prod target: multi-stage runtime image with minimal JRE
FROM eclipse-temurin:17-jre-alpine AS prod
WORKDIR /app

# Create uploads directory
RUN mkdir -p /app/uploads

# Copy the fat JAR from the builder stage
COPY --from=builder /build/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Command to run the application
CMD ["java", "-jar", "app.jar"]
