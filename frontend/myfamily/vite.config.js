import { defineConfig } from "vite";

// Vite dev server config allowing Cloudflare-hosted hostnames
export default defineConfig({
  server: {
    host: "www.imtriboo.com",
    port: 4200,
    strictPort: false,
    disableHostCheck: true,
    // Allow these hostnames (addresses Cloudflare gives for the tunnel)
    allowedHosts: ["www.imtriboo.com", "imtriboo.com", "localhost"],
    hmr: {
      protocol: "wss",
      host: "www.imtriboo.com",
    },
    middlewareMode: true,
    configureServer: (server) => {
      server.middlewares.use((req, res, next) => {
        console.log(`Incoming request: ${req.url}`);
        next();
      });
    },
    proxy: {
      "/api": {
        target: "http://backend:8080",
        changeOrigin: true,
        rewrite: (path) => path,
      },
    },
    cors: true,
    origin: "*",
  },
});
