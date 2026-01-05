import { Link, useLocation } from "react-router-dom";

export default function Navbar({ onLogout }) {
  const location = useLocation();

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <span className="navbar-logo">Eurekabank</span>
      </div>

      <div className="navbar-center">
        <Link className={location.pathname === "/" ? "nav-link active" : "nav-link"} to="/">
          Inicio
        </Link>

        <Link
          className={location.pathname === "/sucursales" ? "nav-link active" : "nav-link"}
          to="/sucursales"
        >
          Sucursales
        </Link>

        <Link
          className={location.pathname === "/cajeros" ? "nav-link active" : "nav-link"}
          to="/cajeros"
        >
          Cajeros
        </Link>
      </div>

      <div className="navbar-right">
        <button className="btn-outline" onClick={onLogout}>
          Cerrar sesión
        </button>
      </div>
    </nav>
  );
}
