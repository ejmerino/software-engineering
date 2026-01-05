import { useEffect, useMemo, useState } from "react";
import api from "../api/axiosClient";
import RoutePanel from "../components/RoutePanel.jsx";
import "../styles/Sucursales.css";

const emptySucursal = {
  id: null,
  nombre: "",
  ciudad: "",
  direccion: "",
  latitud: "",
  longitud: "",
};

export default function Sucursales() {
  const PRIMARY_PATH = useMemo(() => "/sucursales", []);
  const FALLBACK_PATH = useMemo(() => "/api/sucursales", []);

  const [apiPath, setApiPath] = useState(PRIMARY_PATH);

  const [sucursales, setSucursales] = useState([]);
  const [loading, setLoading] = useState(false);

  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState(null);
  const [formData, setFormData] = useState(emptySucursal);

  const [routePoint, setRoutePoint] = useState(null);

  const toNumberOrEmpty = (v) => {
    if (v === "" || v === null || v === undefined) return "";
    const n = Number(v);
    return Number.isFinite(n) ? n : "";
  };

  const normalizeSucursal = (s) => ({
    id: s.id ?? null,
    nombre: s.nombre ?? "",
    ciudad: s.ciudad ?? "",
    direccion: s.direccion ?? "",
    latitud: toNumberOrEmpty(s.latitud),
    longitud: toNumberOrEmpty(s.longitud),
  });

  const fetchWithPath = async (path) => {
    const res = await api.get(path);
    const list = Array.isArray(res.data) ? res.data : [];
    setSucursales(list.map(normalizeSucursal));
  };

  const loadData = async () => {
    try {
      setLoading(true);
      await fetchWithPath(apiPath);
    } catch (e1) {
      const status = e1?.response?.status;
      if (status === 404) {
        try {
          const nextPath = apiPath === PRIMARY_PATH ? FALLBACK_PATH : PRIMARY_PATH;
          await fetchWithPath(nextPath);
          setApiPath(nextPath);
        } catch (e2) {
          console.error(e2);
          alert("Error al cargar sucursales. Revisa el endpoint en .NET.");
        }
      } else {
        console.error(e1);
        alert("Error al cargar sucursales. Revisa API + CORS.");
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const openNewForm = () => {
    setEditing(null);
    setFormData(emptySucursal);
    setShowForm(true);
  };

  const openEditForm = (s) => {
    setEditing(s);
    setFormData({
      ...s,
      latitud: toNumberOrEmpty(s.latitud),
      longitud: toNumberOrEmpty(s.longitud),
    });
    setShowForm(true);
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;

    if (name === "latitud" || name === "longitud") {
      setFormData((prev) => ({ ...prev, [name]: value === "" ? "" : Number(value) }));
      return;
    }

    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const validateForm = () => {
    if (!formData.nombre.trim()) return "Nombre es obligatorio.";
    if (!formData.ciudad.trim()) return "Ciudad es obligatoria.";
    if (!formData.direccion.trim()) return "Dirección es obligatoria.";

    const lat = Number(formData.latitud);
    const lng = Number(formData.longitud);
    if (!Number.isFinite(lat)) return "Latitud debe ser un número.";
    if (!Number.isFinite(lng)) return "Longitud debe ser un número.";
    if (lat < -90 || lat > 90) return "Latitud debe estar entre -90 y 90.";
    if (lng < -180 || lng > 180) return "Longitud debe estar entre -180 y 180.";

    return "";
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const msg = validateForm();
    if (msg) return alert(msg);

    try {
      const payload = {
        id: formData.id ?? 0,
        nombre: formData.nombre.trim(),
        ciudad: formData.ciudad.trim(),
        direccion: formData.direccion.trim(),
        latitud: Number(formData.latitud),
        longitud: Number(formData.longitud),
      };

      if (editing?.id) await api.put(`${apiPath}/${editing.id}`, payload);
      else await api.post(apiPath, payload);

      setShowForm(false);
      setEditing(null);
      setFormData(emptySucursal);
      await loadData();
    } catch (err) {
      console.error(err);
      alert("Error al guardar sucursal.");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Eliminar sucursal?")) return;
    try {
      await api.delete(`${apiPath}/${id}`);
      await loadData();
      if (routePoint?.id === id) setRoutePoint(null);
    } catch (err) {
      console.error(err);
      alert("Error al eliminar sucursal.");
    }
  };

  const canRoute = (s) =>
    Number.isFinite(Number(s.latitud)) && Number.isFinite(Number(s.longitud));

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>Gestión de Sucursales</h1>

        <button className="btn-primary" onClick={openNewForm}>
          + Agregar
        </button>
      </div>

      {loading ? (
        <p className="muted">Cargando…</p>
      ) : (
        <>
          {/* DESKTOP: TABLA */}
          <div className="table-wrapper desktop-only">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Nombre</th>
                  <th>Ciudad</th>
                  <th>Dirección</th>
                  <th style={{ textAlign: "right" }}>Acciones</th>
                </tr>
              </thead>

              <tbody>
                {sucursales.map((s) => (
                  <tr key={s.id}>
                    <td>{s.nombre}</td>
                    <td>{s.ciudad}</td>
                    <td>{s.direccion}</td>

                    <td className="table-actions" style={{ justifyContent: "flex-end" }}>
                      <button className="btn-small" onClick={() => openEditForm(s)}>
                        Editar
                      </button>
                      <button className="btn-small danger" onClick={() => handleDelete(s.id)}>
                        Eliminar
                      </button>
                      <button
                        className="btn-small secondary"
                        onClick={() => setRoutePoint(s)}
                        disabled={!canRoute(s)}
                        title={!canRoute(s) ? "Latitud/Longitud inválidas" : "Ver ruta"}
                      >
                        Llegar
                      </button>
                    </td>
                  </tr>
                ))}

                {sucursales.length === 0 && (
                  <tr>
                    <td colSpan={4} style={{ opacity: 0.85 }}>
                      No hay sucursales registradas.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {/* MOBILE: CARDS */}
          <div className="mobile-only cards-list">
            {sucursales.map((s) => (
              <div key={s.id} className="sucursal-card">
                <div className="sucursal-card-top">
                  <h3 className="sucursal-name">{s.nombre}</h3>
                  <span className="sucursal-city">{s.ciudad}</span>
                </div>

                <p className="sucursal-dir">{s.direccion}</p>

                <div className="sucursal-actions">
                  <button className="btn-small" onClick={() => openEditForm(s)}>
                    Editar
                  </button>
                  <button className="btn-small danger" onClick={() => handleDelete(s.id)}>
                    Eliminar
                  </button>
                  <button
                    className="btn-small secondary"
                    onClick={() => setRoutePoint(s)}
                    disabled={!canRoute(s)}
                  >
                    Llegar
                  </button>
                </div>
              </div>
            ))}

            {sucursales.length === 0 && (
              <p className="muted">No hay sucursales registradas.</p>
            )}
          </div>
        </>
      )}

      {/* PANEL "CÓMO LLEGAR" DEBAJO */}
      {routePoint && (
        <RoutePanel
          point={routePoint}
          onClose={() => setRoutePoint(null)}
        />
      )}

      {/* MODAL FORM */}
      {showForm && (
        <div className="modal-backdrop">
          <div className="modal-card">
            <div className="modal-header">
              <h2>{editing ? "Editar sucursal" : "Agregar sucursal"}</h2>
              <button
                className="close-btn"
                onClick={() => {
                  setShowForm(false);
                  setEditing(null);
                  setFormData(emptySucursal);
                }}
              >
                ✕
              </button>
            </div>

            <form className="modal-body form-grid" onSubmit={handleSubmit}>
              <label>
                Nombre
                <input
                  name="nombre"
                  type="text"
                  value={formData.nombre}
                  onChange={handleFormChange}
                  required
                />
              </label>

              <label>
                Ciudad
                <input
                  name="ciudad"
                  type="text"
                  value={formData.ciudad}
                  onChange={handleFormChange}
                  required
                />
              </label>

              <label style={{ gridColumn: "1 / -1" }}>
                Dirección
                <input
                  name="direccion"
                  type="text"
                  value={formData.direccion}
                  onChange={handleFormChange}
                  required
                />
              </label>

              <label>
                Latitud
                <input
                  name="latitud"
                  type="number"
                  step="0.000001"
                  value={formData.latitud}
                  onChange={handleFormChange}
                  required
                />
              </label>

              <label>
                Longitud
                <input
                  name="longitud"
                  type="number"
                  step="0.000001"
                  value={formData.longitud}
                  onChange={handleFormChange}
                  required
                />
              </label>

              <div className="modal-footer" style={{ gridColumn: "1 / -1" }}>
                <button type="submit" className="btn-primary">
                  {editing ? "Actualizar" : "Agregar"}
                </button>

                <button
                  type="button"
                  className="btn-secondary"
                  onClick={() => {
                    setShowForm(false);
                    setEditing(null);
                    setFormData(emptySucursal);
                  }}
                >
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
