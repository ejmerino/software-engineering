import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite' // Asegúrate de tener esto si usas v4

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    host: '0.0.0.0', // Esto obliga a Vite a escuchar en toda tu red local
    port: 5173,
    strictPort: true, // Si el puerto está ocupado, no cambia a otro
  },
})