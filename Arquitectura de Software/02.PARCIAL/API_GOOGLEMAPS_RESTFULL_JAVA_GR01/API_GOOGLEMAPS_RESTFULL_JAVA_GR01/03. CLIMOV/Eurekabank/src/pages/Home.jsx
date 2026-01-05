// src/pages/Home.jsx
import "../styles/Home.css";

export default function Home() {
  return (
    <div className="home-hero-card">
      <h1>Bienvenido al panel administrativo</h1>
      <p>
        Desde este panel podrás gestionar la información de las sucursales y
        cajeros de Eurekabank, así como visualizar su ubicación en Google Maps.
        Las demás opciones se encuentran deshabilitadas por tratarse de una
        versión académica del sistema.
      </p>
    </div>
  );
}
