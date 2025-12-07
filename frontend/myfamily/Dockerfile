# Multi-target Dockerfile for frontend (dev + prod)

# Builder stage: install and build the Angular app
FROM node:20-alpine AS builder
WORKDIR /build

# Copy package files and install dependencies
COPY package*.json ./
RUN npm ci --silent

# Copy source and build
COPY . .
RUN npm run build --silent


# Dev target: lightweight image for local development using ng serve
FROM node:20-alpine AS dev
WORKDIR /app
COPY package*.json ./
RUN npm ci --silent
COPY . .
EXPOSE 4200
CMD ["npx", "ng", "serve", "--host", "0.0.0.0", "--poll", "2000"]


# Prod target: serve built app with nginx
FROM nginx:alpine AS prod
WORKDIR /usr/share/nginx/html

# Remove default config and add custom nginx config if present
RUN rm -rf /etc/nginx/conf.d/*
COPY nginx.conf /etc/nginx/nginx.conf
COPY default.conf /etc/nginx/conf.d/default.conf

# Copy built app from builder (angular builds to www/)
COPY --from=builder /build/www ./

EXPOSE 80
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

CMD ["nginx", "-g", "daemon off;"]
