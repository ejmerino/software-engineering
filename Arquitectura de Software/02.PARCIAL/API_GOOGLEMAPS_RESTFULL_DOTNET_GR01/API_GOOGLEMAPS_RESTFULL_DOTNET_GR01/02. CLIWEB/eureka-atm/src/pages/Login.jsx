// src/pages/Login.jsx
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/Login.css";
import fondo1 from "../styles/fondo1.jpg";

export default function Login({ onLoginSuccess }) {
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();

    if (usuario === "MONSTER" && password === "MONSTER9") {
      onLoginSuccess?.();
      navigate("/home", { replace: true });
    } else {
      alert("Credenciales incorrectas");
    }
  };

  return (
    <div className="login-page" style={{ backgroundImage: `url(${fondo1})` }}>
      <form className="login-card" onSubmit={handleSubmit}>
        <h1 className="bank-title">EurekaBank</h1>
        <h2 className="login-title">Login</h2>

        <label>Usuario</label>
        <input
          type="text"
          value={usuario}
          onChange={(e) => setUsuario(e.target.value)}
          placeholder="Usuario"
          autoComplete="off"
          required
        />

        <label>Contraseña</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="Contraseña"
          required
        />

        <button type="submit">Ingresar</button>
      </form>
    </div>
  );
}
