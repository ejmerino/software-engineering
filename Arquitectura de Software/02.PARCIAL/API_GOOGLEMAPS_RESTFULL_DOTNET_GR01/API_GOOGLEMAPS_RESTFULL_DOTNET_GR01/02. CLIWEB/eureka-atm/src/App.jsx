// src/App.jsx
import { useEffect, useState } from "react";
import { Routes, Route, Navigate } from "react-router-dom";

import Login from "./pages/Login.jsx";
import Home from "./pages/Home.jsx";
import Sucursales from "./pages/Sucursales.jsx";

import ProtectedRoute from "./components/ProtectedRoute.jsx";
import DashboardLayout from "./components/DashboardLayout.jsx";

import "./App.css";

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // ✅ Mantener sesión al recargar
  useEffect(() => {
    const v = localStorage.getItem("auth_ok");
    setIsAuthenticated(v === "1");
  }, []);

  const handleLoginSuccess = () => {
    localStorage.setItem("auth_ok", "1");
    setIsAuthenticated(true);
  };

  const handleLogout = () => {
    localStorage.removeItem("auth_ok");
    setIsAuthenticated(false);
  };

  return (
    <Routes>
      {/* ✅ al iniciar: si no hay auth -> login */}
      <Route
        path="/"
        element={<Navigate to={isAuthenticated ? "/home" : "/login"} replace />}
      />

      {/* ✅ Login SIEMPRE sin layout */}
      <Route path="/login" element={<Login onLoginSuccess={handleLoginSuccess} />} />

      {/* ✅ Home dentro de layout */}
      <Route
        path="/home"
        element={
          <ProtectedRoute isAuthenticated={isAuthenticated}>
            <DashboardLayout onLogout={handleLogout}>
              <Home />
            </DashboardLayout>
          </ProtectedRoute>
        }
      />

      {/* ✅ Sucursales dentro de layout */}
      <Route
        path="/sucursales"
        element={
          <ProtectedRoute isAuthenticated={isAuthenticated}>
            <DashboardLayout onLogout={handleLogout}>
              <Sucursales />
            </DashboardLayout>
          </ProtectedRoute>
        }
      />

      {/* fallback */}
      <Route
        path="*"
        element={<Navigate to={isAuthenticated ? "/home" : "/login"} replace />}
      />
    </Routes>
  );
}
