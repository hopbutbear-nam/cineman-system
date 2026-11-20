import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Dev: moi request /api/** duoc chuyen tiep sang backend Spring Boot (cong 8080) => khong vuong CORS.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': { target: 'http://localhost:8080', changeOrigin: true },
    },
  },
});
