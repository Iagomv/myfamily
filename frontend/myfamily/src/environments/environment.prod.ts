// At build time or runtime, use the NG_APP_API_URL env var; fallback to a default.
// Angular's build process replaces this: ng build --configuration production uses environment.prod.ts
// The actual API URL is injected via environment variable NG_APP_API_URL (set in .env and docker-compose.yml)
export const environment = {
  production: true,
  apiUrl: 'https://myfamily-backend-latest.onrender.com/api',
};
