import { useEffect, useState } from "react";
import {
  GoogleMap,
  Marker,
  DirectionsRenderer,
  useJsApiLoader,
} from "@react-google-maps/api";

const containerStyle = {
  width: "100%",
  height: "360px",
};

// Origen de respaldo: ESPE Sangolquí
const ORIGIN_ESPE = {
  lat: -0.314022,
  lng: -78.443382,
};

export default function MapRouteModal({ isOpen, onClose, point }) {
  const [origin, setOrigin] = useState(null);
  const [directions, setDirections] = useState(null);
  const [travelMode, setTravelMode] = useState("DRIVING");
  const [routeInfo, setRouteInfo] = useState(null);
  const [routeError, setRouteError] = useState("");
  const [routeSteps, setRouteSteps] = useState([]);

  const apiKey = import.meta.env.VITE_GOOGLE_MAPS_API_KEY;

  const { isLoaded } = useJsApiLoader({
    googleMapsApiKey: apiKey,
    language: "es",
    region: "EC",
  });

  const getModeLabel = (mode) => {
    switch (mode) {
      case "WALKING":
        return "a pie";
      case "BICYCLING":
        return "en bicicleta";
      case "TRANSIT":
        return "en bus o transporte público";
      case "DRIVING":
      default:
        return "en carro";
    }
  };

  // 1) Origen: geolocalización o fallback
  useEffect(() => {
    if (!isOpen || !point) return;

    setOrigin(null);
    setDirections(null);
    setRouteInfo(null);
    setRouteError("");
    setRouteSteps([]);

    if (!navigator.geolocation) {
      setOrigin(ORIGIN_ESPE);
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (pos) => {
        setOrigin({
          lat: pos.coords.latitude,
          lng: pos.coords.longitude,
        });
      },
      () => {
        setOrigin(ORIGIN_ESPE);
      },
      { enableHighAccuracy: true, timeout: 8000 }
    );
  }, [isOpen, point]);

  // 2) Calcular ruta
  const generateRoute = () => {
    if (!origin || !point || !isLoaded) return;

    setDirections(null);
    setRouteInfo(null);
    setRouteError("");
    setRouteSteps([]);

    const directionsService = new google.maps.DirectionsService();

    directionsService.route(
      {
        origin,
        destination: { lat: Number(point.latitud), lng: Number(point.longitud) },
        travelMode,
      },
      (result, status) => {
        if (status === "OK") {
          const route = result.routes?.[0];
          const leg = route?.legs?.[0];

          if (!leg) {
            setRouteError("No se pudo interpretar la ruta.");
            return;
          }

          setDirections(result);

          setRouteInfo({
            distance: leg.distance?.text || "",
            duration: leg.duration?.text || "",
          });

          const rawSteps = leg.steps || [];
          const steps = rawSteps.map((s) => {
            const instrHtml = s.instructions || "";
            const instrText = instrHtml.replace(/<[^>]*>/g, "");
            return {
              instruction: instrText,
              distance: s.distance?.text || "",
              duration: s.duration?.text || "",
            };
          });

          setRouteSteps(steps);
        } else {
          console.error("Error generando ruta:", status);
          setRouteError("No hay ruta disponible para este modo.");
        }
      }
    );
  };

  useEffect(() => {
    if (!isOpen || !point) return;
    generateRoute();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [origin, point, travelMode, isLoaded]);

  if (!isOpen || !point) return null;

  const hasSteps = routeSteps.length > 0;

  return (
    // ✅ YA NO ES MODAL/OVERLAY: ES PANEL EMBEBIDO
    <div className="route-panel">
      {/* Encabezado */}
      <div className="route-panel-header">
        <h2>Cómo llegar</h2>
        <button className="btn-small danger" onClick={onClose}>
          Cerrar
        </button>
      </div>

      {/* 1) Datos de sucursal (primero como pediste) */}
      <div className="route-meta">
        <p>
          <strong>Sucursal:</strong> {point.nombre}
        </p>
        <p>
          <strong>Ciudad:</strong> {point.ciudad}
        </p>
        <p>
          <strong>Dirección:</strong> {point.direccion}
        </p>
      </div>

      {/* 2) Pasos */}
      <div style={{ marginTop: "12px" }}>
        <h3 style={{ marginBottom: "8px" }}>Pasos para llegar</h3>

        {hasSteps ? (
          <ol className="route-steps-list">
            {routeSteps.map((step, index) => (
              <li key={index}>
                {step.instruction}{" "}
                <span className="muted">
                  ({step.distance} · {step.duration})
                </span>
              </li>
            ))}
          </ol>
        ) : routeError ? (
          <div className="route-error">{routeError}</div>
        ) : (
          <p className="muted">Calculando ruta...</p>
        )}
      </div>

      {/* 3) Botones modo */}
      <div className="route-buttons">
        <button
          className={`btn-small ${
            travelMode === "DRIVING" ? "btn-primary" : "btn-secondary"
          }`}
          onClick={() => setTravelMode("DRIVING")}
        >
          🚗 Carro
        </button>
        <button
          className={`btn-small ${
            travelMode === "WALKING" ? "btn-primary" : "btn-secondary"
          }`}
          onClick={() => setTravelMode("WALKING")}
        >
          🚶 A pie
        </button>
        <button
          className={`btn-small ${
            travelMode === "BICYCLING" ? "btn-primary" : "btn-secondary"
          }`}
          onClick={() => setTravelMode("BICYCLING")}
        >
          🚴 Bici
        </button>
        <button
          className={`btn-small ${
            travelMode === "TRANSIT" ? "btn-primary" : "btn-secondary"
          }`}
          onClick={() => setTravelMode("TRANSIT")}
        >
          🚌 Bus
        </button>
      </div>

      {/* 4) Distancia/Tiempo */}
      {routeInfo && !routeError && (
        <div className="route-info">
          <p>
            <strong>Modo actual:</strong> {getModeLabel(travelMode)}
          </p>
          <p>
            <strong>Distancia:</strong> {routeInfo.distance}
          </p>
          <p>
            <strong>Tiempo:</strong> {routeInfo.duration}
          </p>
        </div>
      )}

      {/* 5) Mapa */}
      <div style={{ marginTop: "12px" }}>
        {!apiKey ? (
          <div className="route-error">
            Falta <strong>VITE_GOOGLE_MAPS_API_KEY</strong> en tu .env
          </div>
        ) : !isLoaded ? (
          <p className="muted">Cargando Google Maps...</p>
        ) : (
          <GoogleMap
            mapContainerStyle={containerStyle}
            center={{ lat: Number(point.latitud), lng: Number(point.longitud) }}
            zoom={13}
          >
            <Marker
              position={{ lat: Number(point.latitud), lng: Number(point.longitud) }}
            />
            {directions && <DirectionsRenderer directions={directions} />}
          </GoogleMap>
        )}
      </div>
    </div>
  );
}
