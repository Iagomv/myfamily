# This Dockerfile handles the monorepo structure for Railway
# Railway will set RAILWAY_SERVICE_NAME environment variable

ARG RAILWAY_SERVICE_NAME=backend

# Backend service
FROM maven:3.9-eclipse-temurin-17-alpine AS backend-builder
WORKDIR /build
COPY backend/myfamily/pom.xml .
RUN mvn dependency:go-offline
COPY backend/myfamily/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine AS backend-runtime
WORKDIR /app
RUN mkdir -p /app/uploads
COPY --from=backend-builder /build/target/demo-0.0.1-SNAPSHOT.jar app.jar
RUN addgroup -g 1000 app && adduser -D -u 1000 -G app app
USER app
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]

# Frontend service
FROM node:20-alpine AS frontend-builder
WORKDIR /build
COPY frontend/myfamily/package*.json ./
RUN npm ci
COPY frontend/myfamily ./ 
RUN npm run build

FROM nginx:alpine AS frontend-runtime
RUN rm -rf /etc/nginx/conf.d/*
COPY frontend/myfamily/nginx.conf /etc/nginx/nginx.conf
COPY frontend/myfamily/default.conf /etc/nginx/conf.d/default.conf
COPY --from=frontend-builder /build/dist/myfamily /usr/share/nginx/html
EXPOSE 80
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1
CMD ["nginx", "-g", "daemon off;"]

# Final stage - select based on service
FROM backend-runtime AS backend
FROM frontend-runtime AS frontend
FROM ${RAILWAY_SERVICE_NAME:-backend}
