// Proxy configuration with response rewrite to keep redirects on the dev server origin
// This rewrites absolute Location headers coming from the backend (e.g. http://localhost:8080/...)
// to relative paths so the browser follows redirects on localhost:8100 and avoids CORS issues.

// Use backend service name when in Docker, localhost when running locally
const backendHost = process.env.BACKEND_HOST || "localhost";
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
        // strip protocol+host so the Location becomes relative
        proxyRes.headers["location"] = location.replace(
          /^https?:\/\/[^/]+/,
          ""
        );
      }
    },
  },
};
