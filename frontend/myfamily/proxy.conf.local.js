// Proxy configuration for LOCAL development
// Routes API calls to localhost:8080 (local backend)
// Use: npm start

module.exports = {
  "/api": {
    target: "http://localhost:8080",
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
