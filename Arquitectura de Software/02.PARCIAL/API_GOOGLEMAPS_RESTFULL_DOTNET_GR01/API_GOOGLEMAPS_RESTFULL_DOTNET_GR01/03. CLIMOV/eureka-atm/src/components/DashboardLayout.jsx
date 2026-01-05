import { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "../styles/Home.css";

export default function DashboardLayout({ children, onLogout }) {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const navigate = useNavigate();
  const location = useLocation();

  const isMobile = () => window.matchMedia("(max-width: 768px)").matches;

  useEffect(() => {
    // En móvil, por defecto cerrado
    if (isMobile()) setSidebarOpen(false);
  }, []);

  const goTo = (path) => {
    navigate(path);
    if (isMobile()) setSidebarOpen(false); // cerrar al navegar en móvil
  };

  const handleLogout = () => {
    if (onLogout) onLogout();
    else navigate("/login", { replace: true });
    if (isMobile()) setSidebarOpen(false);
  };

  return (
    <div className={`home-layout ${sidebarOpen ? "" : "sidebar-collapsed"}`}>
      <button
        className="sidebar-toggle"
        onClick={() => setSidebarOpen((p) => !p)}
        aria-label="Abrir menú"
      >
        ☰
      </button>

      {/* Overlay solo en móvil */}
      {sidebarOpen && (
        <div
          className="sidebar-overlay"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      <aside className={`home-sidebar ${sidebarOpen ? "open" : "collapsed"}`}>
        <div className="home-sidebar-header">
          <h2 className="home-brand">EUREKABANK</h2>
          <p className="home-system-text">
            App demo: React + .NET API + Google Maps
          </p>
        </div>

        <nav className="home-menu">
          <button
            title="Inicio"
            className={"menu-item " + (location.pathname === "/home" ? "active-menu" : "")}
            onClick={() => goTo("/home")}
          >
            <span className="mi-ico">🏠</span>
            <span className="mi-text">Inicio</span>
          </button>

          <button title="Consultar Movimiento" className="menu-item disabled">
            <span className="mi-ico">🔎</span>
            <span className="mi-text">Consultar Movimiento</span>
          </button>

          <button title="Realizar Depósito" className="menu-item disabled">
            <span className="mi-ico">➕</span>
            <span className="mi-text">Realizar Depósito</span>
          </button>

          <button title="Realizar Retiro" className="menu-item disabled">
            <span className="mi-ico">➖</span>
            <span className="mi-text">Realizar Retiro</span>
          </button>

          <button title="Realizar Transferencia" className="menu-item disabled">
            <span className="mi-ico">🔁</span>
            <span className="mi-text">Realizar Transferencia</span>
          </button>

          <button
            title="Buscar Sucursales"
            className={"menu-item " + (location.pathname === "/sucursales" ? "active-menu" : "")}
            onClick={() => goTo("/sucursales")}
          >
            <span className="mi-ico">📍</span>
            <span className="mi-text">Buscar Sucursales</span>
          </button>
        </nav>

        <div className="home-sidebar-footer">
          <button title="Cerrar sesión" className="menu-item danger" onClick={handleLogout}>
            <span className="mi-ico">🚪</span>
            <span className="mi-text">Cerrar sesión</span>
          </button>
        </div>
      </aside>

      <section className="home-main">{children}</section>
    </div>
  );
}
