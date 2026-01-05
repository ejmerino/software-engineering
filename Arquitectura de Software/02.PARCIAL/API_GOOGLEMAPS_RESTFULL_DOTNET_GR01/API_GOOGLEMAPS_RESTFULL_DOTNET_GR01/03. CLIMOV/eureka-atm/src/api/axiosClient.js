import axios from "axios";

const baseURL = import.meta.env.VITE_API_URL || "http://localhost:5127";

const api = axios.create({
  baseURL,
  headers: { "Content-Type": "application/json" },
});

export default api;
