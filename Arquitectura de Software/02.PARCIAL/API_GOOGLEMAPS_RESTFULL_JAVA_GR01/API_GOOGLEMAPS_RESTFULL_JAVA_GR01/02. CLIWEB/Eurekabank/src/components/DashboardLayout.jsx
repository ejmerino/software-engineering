// src/components/DashboardLayout.jsx
import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "../styles/Home.css"; // reutilizamos los estilos del layout

export default function DashboardLayout({ children, onLogout }) {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const navigate = useNavigate();
  const location = useLocation();

  const goTo = (path) => {
    navigate(path);
  };

  const handleLogout = () => {
    if (onLogout) onLogout();
    navigate("/login");
  };

  return (
    <div className={`home-layout ${sidebarOpen ? "" : "sidebar-collapsed"}`}>
      {/* Botón hamburguesa */}
      <button
        className="sidebar-toggle"
        onClick={() => setSidebarOpen((prev) => !prev)}
      >
        ☰
      </button>

      {/* Menú lateral */}
      <aside className={`home-sidebar ${sidebarOpen ? "open" : "collapsed"}`}>
        <div className="home-sidebar-header">
          <h2 className="home-brand">EUREKABANK</h2>
          <h3 className="home-system-title">Sistema Eurekabank</h3>
          <p className="home-system-text">
            Panel de administración de movimientos, sucursales y cajeros.
          </p>
        </div>

        <nav className="home-menu">
          <button className="menu-item disabled">Buscar Movimientos</button>
          <button className="menu-item disabled">Registrar Depósito</button>
          <button className="menu-item disabled">Realizar Retiro</button>
          <button className="menu-item disabled">Realizar Transferencia</button>

          <button
            className={
              "menu-item " +
              (location.pathname === "/sucursales" ? "active-menu" : "")
            }
            onClick={() => goTo("/sucursales")}
          >
            Gestión de Sucursales
          </button>

          <button
            className={
              "menu-item " +
              (location.pathname === "/cajeros" ? "active-menu" : "")
            }
            onClick={() => goTo("/cajeros")}
          >
            Gestión de Cajeros
          </button>
        </nav>

        <div className="home-sidebar-footer">
          <button className="menu-item danger" onClick={handleLogout}>
            Cerrar sesión
          </button>
          <span className="home-hint">
           
          </span>
        </div>
      </aside>

      {/* Contenido de cada página */}
      <section className="home-main">{children}</section>
    </div>
  );
}
