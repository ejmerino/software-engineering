// src/components/RoutePanel.jsx
import { useEffect, useState } from "react";
import {
  GoogleMap,
  Marker,
  DirectionsRenderer,
  useJsApiLoader,
} from "@react-google-maps/api";

// Origen fallback: ESPE Sangolquí
const ORIGIN_ESPE = { lat: -0.314022, lng: -78.443382 };

export default function RoutePanel({ point, onClose }) {
  const [origin, setOrigin] = useState(null);
  const [directions, setDirections] = useState(null);
  const [travelMode, setTravelMode] = useState("DRIVING");
  const [routeInfo, setRouteInfo] = useState(null);
  const [routeSteps, setRouteSteps] = useState([]);
  const [routeError, setRouteError] = useState("");
  const [geoMessage, setGeoMessage] = useState("");

  const apiKey = import.meta.env.VITE_GOOGLE_MAPS_API_KEY;

  const { isLoaded } = useJsApiLoader({
    googleMapsApiKey: apiKey,
    language: "es",
    region: "EC",
  });

  const getModeLabel = (mode) => {
    switch (mode) {
      case "WALKING":
        return "A pie";
      case "BICYCLING":
        return "Bici";
      case "TRANSIT":
        return "Bus";
      default:
        return "Carro";
    }
  };

  // Ubicación actual (o fallback)
  useEffect(() => {
    if (!point) return;

    if (!navigator.geolocation) {
      setOrigin(ORIGIN_ESPE);
      setGeoMessage("No hay geolocalización. Se usa ESPE Sangolquí como origen.");
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (pos) => {
        setOrigin({ lat: pos.coords.latitude, lng: pos.coords.longitude });
        setGeoMessage("");
      },
      () => {
        setOrigin(ORIGIN_ESPE);
        setGeoMessage("No se pudo obtener tu ubicación. Se usa ESPE Sangolquí como origen.");
      }
    );
  }, [point]);

  // Calcular ruta
  useEffect(() => {
    if (!origin || !point || !isLoaded) return;

    setDirections(null);
    setRouteInfo(null);
    setRouteSteps([]);
    setRouteError("");

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

          const steps = (leg.steps || []).map((s) => {
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
          setRouteError("No hay ruta disponible para este modo.");
        }
      }
    );
  }, [origin, point, travelMode, isLoaded]);

  if (!point) return null;

  return (
    <div className="route-panel">
      {/* HEADER */}
      <div className="route-panel-header">
        <div>
          <h2>Cómo llegar</h2>
          
        </div>

        <button className="btn-small" type="button" onClick={onClose}>
          Cerrar
        </button>
      </div>

      {/* INFO SUCURSAL */}
      <div className="route-info">
        <div className="route-info-row">
          <div className="k">Sucursal:</div>
          <div className="v">{point.nombre}</div>
        </div>

        <div className="route-info-row">
          <div className="k">Ciudad:</div>
          <div className="v">{point.ciudad}</div>
        </div>

        <div className="route-info-row">
          <div className="k">Dirección:</div>
          <div className="v">{point.direccion}</div>
        </div>
      </div>

      {/* MODOS + MÉTRICAS */}
      <div className="route-modes">
        <div className="route-modes-left">
          <button
            type="button"
            className={`mode-btn ${travelMode === "DRIVING" ? "active" : ""}`}
            onClick={() => setTravelMode("DRIVING")}
          >
            🚗 Carro
          </button>

          <button
            type="button"
            className={`mode-btn ${travelMode === "WALKING" ? "active" : ""}`}
            onClick={() => setTravelMode("WALKING")}
          >
            🚶 A pie
          </button>

          <button
            type="button"
            className={`mode-btn ${travelMode === "BICYCLING" ? "active" : ""}`}
            onClick={() => setTravelMode("BICYCLING")}
          >
            🚴 Bici
          </button>

          <button
            type="button"
            className={`mode-btn ${travelMode === "TRANSIT" ? "active" : ""}`}
            onClick={() => setTravelMode("TRANSIT")}
          >
            🚌 Bus
          </button>
        </div>
    </div>
       <div className="route-metrics">
        <div>
            <strong>Distancia:</strong> {routeInfo?.distance || "--"}
        </div>
        <div>
            <strong>Tiempo:</strong> {routeInfo?.duration || "--"}
        </div>
    </div>


      {/* PASOS */}
      <div className="route-steps">
        <h3>Pasos para llegar</h3>

        {routeError ? (
          <p className="route-error">{routeError}</p>
        ) : routeSteps.length > 0 ? (
          <div className="route-steps-box">
            <ol>
              {routeSteps.map((s, idx) => (
                <li key={idx}>
                  {s.instruction}{" "}
                  <span style={{ opacity: 0.75 }}>
                    ({s.distance} · {s.duration})
                  </span>
                </li>
              ))}
            </ol>
          </div>
        ) : (
          <p className="route-muted">Calculando ruta...</p>
        )}
      </div>

      {/* MAPA */}
      <div className="route-map">
        {isLoaded ? (
          <GoogleMap
            mapContainerStyle={{ width: "100%", height: "100%" }}
            center={{ lat: Number(point.latitud), lng: Number(point.longitud) }}
            zoom={13}
          >
            <Marker position={{ lat: Number(point.latitud), lng: Number(point.longitud) }} />
            {directions && <DirectionsRenderer directions={directions} />}
          </GoogleMap>
        ) : (
          <p className="route-muted" style={{ padding: 12 }}>
            Cargando Google Maps...
          </p>
        )}
      </div>
    </div>
  );
}
