import { useEffect, useState } from "react";
import api from "../api/axiosClient";
import MapRouteModal from "../components/MapRouteModal.jsx";
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
  const [sucursales, setSucursales] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState(null);
  const [formData, setFormData] = useState(emptySucursal);
  const [routePoint, setRoutePoint] = useState(null);

  const loadData = async () => {
    try {
      setLoading(true);
      const res = await api.get("/sucursales");
      setSucursales(res.data);
    } catch (e) {
      console.error(e);
      alert("Error al cargar sucursales");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const openNewForm = () => {
    setEditing(null);
    setFormData(emptySucursal);
    setShowForm(true);
  };

  const openEditForm = (s) => {
    setEditing(s);
    setFormData(s);
    setShowForm(true);
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]:
        name === "latitud" || name === "longitud"
          ? parseFloat(value) || ""
          : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editing) {
        await api.put(`/sucursales/${editing.id}`, formData);
      } else {
        await api.post("/sucursales", formData);
      }
      setShowForm(false);
      await loadData();
    } catch (err) {
      console.error(err);
      alert("Error al guardar sucursal");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Eliminar sucursal?")) return;
    try {
      await api.delete(`/sucursales/${id}`);
      await loadData();
    } catch (err) {
      console.error(err);
      alert("Error al eliminar sucursal");
    }
  };

  return (
    <div className="page-container">
      {/* HEADER */}
      <div className="page-header">
        <h1>Gestión de Sucursales</h1>
        <button className="btn-primary" onClick={openNewForm}>
          + Agregar
        </button>
      </div>

      {loading ? (
        <p>Cargando...</p>
      ) : (
        <>
          {/* 🖥️ TABLA DESKTOP */}
          <div className="table-wrapper desktop-only">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Nombre</th>
                  <th>Ciudad</th>
                  <th>Dirección</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {sucursales.map((s) => (
                  <tr key={s.id}>
                    <td>{s.nombre}</td>
                    <td>{s.ciudad}</td>
                    <td>{s.direccion}</td>
                    <td className="table-actions">
                      <button className="btn-small" onClick={() => openEditForm(s)}>
                        Editar
                      </button>
                      <button className="btn-small danger" onClick={() => handleDelete(s.id)}>
                        Eliminar
                      </button>
                      <button className="btn-small secondary" onClick={() => setRoutePoint(s)}>
                        Llegar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* 📱 CARDS MOBILE */}
          <div className="mobile-only">
            {sucursales.map((s) => (
              <div key={s.id} className="card">
                <h3>{s.nombre}</h3>
                <p><strong>Ciudad:</strong> {s.ciudad}</p>
                <p><strong>Dirección:</strong> {s.direccion}</p>

                <div className="card-actions">
                  <button className="btn-small" onClick={() => openEditForm(s)}>Editar</button>
                  <button className="btn-small danger" onClick={() => handleDelete(s.id)}>Eliminar</button>
                  <button className="btn-small secondary" onClick={() => setRoutePoint(s)}>Llegar</button>
                </div>
              </div>
            ))}

            {sucursales.length === 0 && (
              <p>No hay sucursales registradas.</p>
            )}
          </div>
        </>
      )}

      {/* MODAL FORM */}
      {showForm && (
        <div className="modal-backdrop">
          <div className="modal-card">
            <div className="modal-header">
              <h2>{editing ? "Editar sucursal" : "Agregar sucursal"}</h2>
              <button className="close-btn" onClick={() => setShowForm(false)}>✕</button>
            </div>

            <form className="modal-body form-grid" onSubmit={handleSubmit}>
              {["nombre", "ciudad", "direccion", "latitud", "longitud"].map((field) => (
                <label key={field}>
                  {field.charAt(0).toUpperCase() + field.slice(1)}
                  <input
                    name={field}
                    type={field === "latitud" || field === "longitud" ? "number" : "text"}
                    step="0.000001"
                    value={formData[field]}
                    onChange={handleFormChange}
                    required
                  />
                </label>
              ))}

              <div className="modal-footer">
                <button type="submit" className="btn-primary">
                  {editing ? "Actualizar" : "Agregar"}
                </button>
                <button type="button" className="btn-secondary" onClick={() => setShowForm(false)}>
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <MapRouteModal
        isOpen={!!routePoint}
        onClose={() => setRoutePoint(null)}
        point={routePoint}
      />
    </div>
  );
}
