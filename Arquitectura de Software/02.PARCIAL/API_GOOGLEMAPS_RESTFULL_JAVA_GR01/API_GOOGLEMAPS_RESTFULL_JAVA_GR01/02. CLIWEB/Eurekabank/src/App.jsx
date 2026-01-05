// src/App.jsx
import { useState } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import "./App.css";

import Login from "./pages/Login.jsx";
import Home from "./pages/Home.jsx";
import Sucursales from "./pages/Sucursales.jsx";
import Cajeros from "./pages/Cajeros.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import DashboardLayout from "./components/DashboardLayout.jsx";

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const handleLoginSuccess = () => setIsAuthenticated(true);
  const handleLogout = () => setIsAuthenticated(false);

  return (
    <div className="app-root">
      <Routes>
        {/* Login sin layout */}
        <Route
          path="/login"
          element={<Login onLoginSuccess={handleLoginSuccess} />}
        />

        {/* Home con layout + menú lateral */}
        <Route
          path="/"
          element={
            <ProtectedRoute isAuthenticated={isAuthenticated}>
              <DashboardLayout onLogout={handleLogout}>
                <Home />
              </DashboardLayout>
            </ProtectedRoute>
          }
        />

        {/* Sucursales con el mismo menú lateral */}
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

        {/* Cajeros con el mismo menú lateral */}
        <Route
          path="/cajeros"
          element={
            <ProtectedRoute isAuthenticated={isAuthenticated}>
              <DashboardLayout onLogout={handleLogout}>
                <Cajeros />
              </DashboardLayout>
            </ProtectedRoute>
          }
        />

        {/* Fallback */}
        <Route
          path="*"
          element={<Navigate to={isAuthenticated ? "/" : "/login"} replace />}
        />
      </Routes>
    </div>
  );
}
