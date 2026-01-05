import { useEffect, useState } from "react";
import api from "../api/axiosClient";
import MapRouteModal from "../components/MapRouteModal.jsx";

const emptyAtm = {
  id: null,
  nombre: "",
  direccion: "",
  latitud: "",
  longitud: "",
  estado: "",
};

export default function Cajeros() {
  const [atms, setAtms] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState(null);
  const [formData, setFormData] = useState(emptyAtm);
  const [routePoint, setRoutePoint] = useState(null);

  const loadData = async () => {
    try {
      setLoading(true);
      const res = await api.get("/atms");
      setAtms(res.data);
    } catch (e) {
      console.error(e);
      alert("Error al cargar cajeros");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const openNewForm = () => {
    setEditing(null);
    setFormData(emptyAtm);
    setShowForm(true);
  };

  const openEditForm = (atm) => {
    setEditing(atm);
    setFormData(atm);
    setShowForm(true);
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]:
        name === "latitud" || name === "longitud" ? parseFloat(value) || "" : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (editing) {
        await api.put(`/atms/${editing.id}`, formData);
      } else {
        await api.post("/atms", formData);
      }
      setShowForm(false);
      await loadData();
    } catch (err) {
      console.error(err);
      alert("Error al guardar cajero");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Eliminar cajero?")) return;
    try {
      await api.delete(`/atms/${id}`);
      await loadData();
    } catch (err) {
      console.error(err);
      alert("Error al eliminar cajero");
    }
  };

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>Gestión de Cajeros</h1>
        <button className="btn-primary" onClick={openNewForm}>
          Agregar cajero
        </button>
      </div>

      {loading ? (
        <p>Cargando...</p>
      ) : (
        <div className="table-wrapper">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Dirección</th>
                <th>Latitud</th>
                <th>Longitud</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {atms.map((a) => (
                <tr key={a.id}>
                  <td>{a.id}</td>
                  <td>{a.nombre}</td>
                  <td>{a.direccion}</td>
                  <td>{a.latitud}</td>
                  <td>{a.longitud}</td>
                  <td>{a.estado}</td>
                  <td className="table-actions">
                    <button className="btn-small" onClick={() => openEditForm(a)}>
                      Editar
                    </button>
                    <button
                      className="btn-small danger"
                      onClick={() => handleDelete(a.id)}
                    >
                      Eliminar
                    </button>
                    <button
                      className="btn-small secondary"
                      onClick={() => setRoutePoint(a)}
                    >
                      Llegar
                    </button>
                  </td>
                </tr>
              ))}
              {atms.length === 0 && (
                <tr>
                  <td colSpan="7">No hay cajeros registrados.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {/* Form modal */}
      {showForm && (
        <div className="modal-backdrop">
          <div className="modal-card">
            <div className="modal-header">
              <h2>{editing ? "Editar cajero" : "Agregar cajero"}</h2>
              <button className="close-btn" onClick={() => setShowForm(false)}>
                ✕
              </button>
            </div>
            <form className="modal-body form-grid" onSubmit={handleSubmit}>
              <label>
                Nombre
                <input
                  name="nombre"
                  value={formData.nombre}
                  onChange={handleFormChange}
                  required
                />
              </label>
              <label>
                Dirección
                <input
                  name="direccion"
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
              <label>
                Estado
                <input
                  name="estado"
                  value={formData.estado}
                  onChange={handleFormChange}
                  required
                />
              </label>

              <div className="modal-footer">
                <button type="submit" className="btn-primary">
                  {editing ? "Actualizar" : "Agregar"}
                </button>
                <button
                  type="button"
                  className="btn-secondary"
                  onClick={() => setShowForm(false)}
                >
                  Cancelar
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Modal de ruta */}
      <MapRouteModal
        isOpen={!!routePoint}
        onClose={() => setRoutePoint(null)}
        point={routePoint}
      />
    </div>
  );
}
