import { useEffect, useState } from "react";
import {
  GoogleMap,
  Marker,
  DirectionsRenderer,
  useJsApiLoader,
} from "@react-google-maps/api";

const containerStyle = {
  width: "100%",
  height: "300px",
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
  const [geoMessage, setGeoMessage] = useState("");
  const [routeSteps, setRouteSteps] = useState([]);

  const apiKey = import.meta.env.VITE_GOOGLE_MAPS_API_KEY;

const { isLoaded } = useJsApiLoader({
  googleMapsApiKey: apiKey,
  language: "es",
  region: "ES", // opcional
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

  // 1) Intentar usar la ubicación real; si falla, usar ESPE
  useEffect(() => {
    if (!navigator.geolocation) {
      setOrigin(ORIGIN_ESPE);
      setGeoMessage("No hay geolocalización. Se usa ESPE Sangolquí.");
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
        setGeoMessage("");
      }
    );
  }, []);

  // 2) Calcular ruta con Directions API
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
        destination: { lat: point.latitud, lng: point.longitud },
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

          // 🔴 AQUÍ ESTABA EL PROBLEMA: antes usábamos s.html_instructions
          const steps = rawSteps.map((s) => {
            const instrHtml = s.instructions || ""; // <-- propiedad correcta en la JS API
            const instrText = instrHtml.replace(/<[^>]*>/g, ""); // quitamos etiquetas <b>, <div>, etc.
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
    generateRoute();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [origin, point, travelMode, isLoaded]);

  if (!isOpen || !point) return null;

  const hasSteps = routeSteps.length > 0;

  const fallbackDescription =
    !hasSteps && routeInfo
      ? `Sigue la ruta indicada en el mapa (${routeInfo.distance}, ${routeInfo.duration}) desde tu ubicación actual hasta ${point.nombre}.`
      : "";

  return (
    <div className="modal-overlay">
      <div className="modal">
        {/* Encabezado */}
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <h2>Cómo llegar</h2>
          <button className="close-btn" onClick={onClose}>
            ✕
          </button>
        </div>

        {/* Información básica del lugar */}
        <p>
          <strong>Nombre:</strong> {point.nombre}
        </p>
        {point.ciudad && (
          <p>
            <strong>Ciudad:</strong> {point.ciudad}
          </p>
        )}
        {point.direccion && (
          <p>
            <strong>Dirección:</strong> {point.direccion}
          </p>
        )}

        {geoMessage && (
          <p style={{ marginTop: "8px", fontSize: "0.85rem", opacity: 0.8 }}>
            {geoMessage}
          </p>
        )}

        {/* 1) Descripción del camino = paso a paso */}
        <div style={{ marginTop: "14px" }}>
          <h3 style={{ marginBottom: "6px" }}>Descripción del camino</h3>

          {hasSteps ? (
            <ol
              style={{
                paddingLeft: "1.2rem",
                maxHeight: "190px",
                overflowY: "auto",
                fontSize: "0.9rem",
              }}
            >
              {routeSteps.map((step, index) => (
                <li key={index} style={{ marginBottom: "6px" }}>
                  {step.instruction}{" "}
                  <span style={{ opacity: 0.7 }}>
                    ({step.distance} · {step.duration})
                  </span>
                </li>
              ))}
            </ol>
          ) : routeError ? (
            <p style={{ fontSize: "0.9rem", opacity: 0.85 }}>
              No se pudo obtener una descripción detallada de la ruta.
            </p>
          ) : fallbackDescription ? (
            <p style={{ fontSize: "0.9rem" }}>{fallbackDescription}</p>
          ) : (
            <p style={{ fontSize: "0.9rem", opacity: 0.85 }}>
              Calculando ruta...
            </p>
          )}
        </div>

        {routeError && (
          <div
            style={{
              marginTop: "10px",
              fontSize: "0.85rem",
              color: "#fecaca",
              background: "rgba(220,38,38,0.15)",
              borderRadius: "0.5rem",
              padding: "0.5rem 0.75rem",
            }}
          >
            {routeError}
          </div>
        )}

        {/* 2) Botones de modo */}
        <div
          style={{
            marginTop: "14px",
            display: "flex",
            gap: "0.5rem",
            flexWrap: "wrap",
          }}
        >
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
            🚴 Bicicleta
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

        {/* 3) Info del modo elegido */}
        {routeInfo && !routeError && (
          <div style={{ marginTop: "12px", fontSize: "0.9rem" }}>
            <p>
              <strong>Modo actual:</strong> {getModeLabel(travelMode)}
            </p>
            <p>
              <strong>Distancia:</strong> {routeInfo.distance}
            </p>
            <p>
              <strong>Tiempo estimado:</strong> {routeInfo.duration}
            </p>
          </div>
        )}

        {/* 4) Mapa */}
        <div style={{ marginTop: "15px" }}>
          {isLoaded && (
            <GoogleMap
              mapContainerStyle={containerStyle}
              center={{ lat: point.latitud, lng: point.longitud }}
              zoom={13}
            >
              <Marker position={{ lat: point.latitud, lng: point.longitud }} />
              {directions && <DirectionsRenderer directions={directions} />}
            </GoogleMap>
          )}
        </div>

        <div style={{ marginTop: "15px", textAlign: "right" }}>
          <button className="btn-secondary" onClick={onClose}>
            Cerrar
          </button>
        </div>
      </div>
    </div>
  );
}
