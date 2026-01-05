// src/pages/Login.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Login.css";

const USER = "MONSTER";
const PASS = "MONSTER9";

export default function Login({ onLoginSuccess }) {
  const [usuario, setUsuario] = useState("");
  const [clave, setClave] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();

    if (usuario === USER && clave === PASS) {
      setError("");
      onLoginSuccess();
      navigate("/");
    } else {
      setError("Credenciales incorrectas.");
    }
  };

  return (
    <div className="login-wrapper">
      <div className="login-box">
        <h1 className="login-title-main">EUREKABANK</h1>
        <h2 className="login-title">Login</h2>

        <form onSubmit={handleSubmit} className="login-form">
          <label>
            Usuario
            <input
              type="text"
              value={usuario}
              onChange={(e) => setUsuario(e.target.value)}
              placeholder="Usuario"
              autoFocus
            />
          </label>

          <label>
            Contraseña
            <input
              type="password"
              value={clave}
              onChange={(e) => setClave(e.target.value)}
              placeholder="Contraseña"
            />
          </label>

          {error && <div className="error-box">{error}</div>}

          <button type="submit" className="btn-primary full-width">
            Ingresar
          </button>
        </form>
      </div>
    </div>
  );
}
