import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true, // <--- ESTO ES LO IMPORTANTE: Permite conexiones externas
    port: 5174, // Fijamos el puerto 5174 para que no cambie
  }
})