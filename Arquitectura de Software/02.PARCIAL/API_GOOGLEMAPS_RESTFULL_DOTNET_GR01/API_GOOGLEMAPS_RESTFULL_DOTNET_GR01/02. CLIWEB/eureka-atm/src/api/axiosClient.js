import axios from "axios";

const baseURL = import.meta.env.VITE_API_URL || "http://10.40.31.204:5127";

const api = axios.create({
  baseURL,
  headers: { "Content-Type": "application/json" },
});

export default api;
