// src/pages/Home.jsx
import "../styles/Home.css";

export default function Home() {
  return (
    <div className="home-content">
      <h1 className="home-title">¿Qué puedes hacer aquí?</h1>

      <p className="home-lead">
        Aplicación de <strong>EurekaBank</strong> (demo) desarrollada en <strong>React</strong> que
        consume servicios de una <strong>API REST en .NET</strong> y utiliza <strong>Google Maps</strong>
        para ubicar y obtener rutas hacia las sucursales del banco.
      </p>

      <ul className="home-list">
        <li>📌 Consultar sucursales registradas.</li>
        <li>🗺️ Visualizar sucursales en Google Maps.</li>
        <li>➡️ Generar rutas con el botón “Llegar”.</li>
        <li>🔐 Acceder mediante login local (modo demostración).</li>
      </ul>

      <p className="home-note">
        <strong>Nota:</strong> Movimientos/Depósitos/Retiros/Transferencias quedan como demostración visual.
      </p>
    </div>
  );
}
