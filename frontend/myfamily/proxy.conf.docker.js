// Proxy configuration for DOCKER development
// Routes API calls to backend:8080 (Docker service)
// Used by: npm run start:docker (in docker-compose)
// Uses environment variables when in Docker container

const backendHost = process.env.BACKEND_HOST || "backend";
const backendPort = process.env.BACKEND_PORT || "8080";
const backendUrl = `http://${backendHost}:${backendPort}`;

console.log(`[Proxy] Routing /api to ${backendUrl}`);

module.exports = {
  "/api": {
    target: backendUrl,
    secure: false,
    changeOrigin: true,
    logLevel: "debug",
    onProxyRes: (proxyRes) => {
      const location = proxyRes.headers && proxyRes.headers["location"];
      if (location && typeof location === "string") {
        proxyRes.headers["location"] = location.replace(
          /^https?:\/\/[^/]+/,
          ""
        );
      }
    },
  },
};
