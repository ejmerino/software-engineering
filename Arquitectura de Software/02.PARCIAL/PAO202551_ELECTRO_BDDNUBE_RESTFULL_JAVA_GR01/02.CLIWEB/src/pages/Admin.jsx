import { useState, useEffect } from 'react';
import { crearProducto, actualizarProducto, eliminarProducto, listarProductos } from '../services/api';

export default function Admin() {
  const [productos, setProductos] = useState([]);
  const [modoEdicion, setModoEdicion] = useState(false);
  
  // Estado del formulario
  const [form, setForm] = useState({
    idElectrodomestico: null, // Importante para editar
    nombre: '',
    descripcion: '',
    precioVenta: '',
    stock: '',
    rutaImagen: ''
  });

  // Cargar productos al iniciar
  useEffect(() => {
    cargarInventario();
  }, []);

  const cargarInventario = () => {
    listarProductos().then(setProductos);
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // Cargar datos en el formulario para editar
  const handleEditar = (p) => {
    setForm({
        idElectrodomestico: p.idElectrodomestico,
        nombre: p.nombre,
        descripcion: p.descripcion,
        precioVenta: p.precioVenta,
        stock: p.stock,
        rutaImagen: p.rutaImagen || ''
    });
    setModoEdicion(true);
    // Scroll suave hacia arriba
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleCancelar = () => {
    setForm({ idElectrodomestico: null, nombre: '', descripcion: '', precioVenta: '', stock: '', rutaImagen: '' });
    setModoEdicion(false);
  };

  const handleEliminar = async (id) => {
    if(confirm("¿Seguro que quieres eliminar este producto?")) {
        await eliminarProducto(id);
        cargarInventario(); // Recargar lista
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.nombre || !form.precioVenta) return alert("Datos incompletos");

    const productoEnviar = {
        ...form,
        precioVenta: parseFloat(form.precioVenta),
        stock: parseInt(form.stock) || 0,
        rutaImagen: form.rutaImagen || "https://placehold.co/400x400?text=Sin+Foto"
    };

    let resultado;
    
    if (modoEdicion) {
        // ACTUALIZAR (PUT)
        resultado = await actualizarProducto(form.idElectrodomestico, productoEnviar);
        if(resultado) alert("✅ Producto actualizado!");
    } else {
        // CREAR (POST)
        resultado = await crearProducto(productoEnviar);
        if(resultado) alert("✅ Producto creado!");
    }

    if (resultado) {
        handleCancelar(); // Limpiar form
        cargarInventario(); // Recargar tabla
    }
  };

  return (
    <div className="p-8 max-w-6xl mx-auto animate-fadeIn">
      <h2 className="text-3xl font-bold text-slate-800 mb-6 border-l-8 border-indigo-600 pl-4">
        {modoEdicion ? 'Editar Producto' : 'Nuevo Producto'}
      </h2>

      {/* --- FORMULARIO --- */}
      <div className="flex flex-col md:flex-row gap-8 mb-12">
        <div className="flex-1 bg-white p-6 rounded-2xl shadow-xl border border-slate-100">
            <form onSubmit={handleSubmit} className="space-y-4">
                {/* Inputs del Formulario (Iguales al anterior) */}
                <div>
                    <label className="block text-sm font-bold text-slate-700">Nombre</label>
                    <input name="nombre" value={form.nombre} onChange={handleChange} className="w-full border p-3 rounded-lg bg-slate-50 outline-none focus:border-indigo-500" />
                </div>
                <div className="flex gap-4">
                    <div className="flex-1">
                        <label className="block text-sm font-bold text-slate-700">Precio</label>
                        <input type="number" step="0.01" name="precioVenta" value={form.precioVenta} onChange={handleChange} className="w-full border p-3 rounded-lg bg-slate-50" />
                    </div>
                    <div className="w-1/3">
                        <label className="block text-sm font-bold text-slate-700">Stock</label>
                        <input type="number" name="stock" value={form.stock} onChange={handleChange} className="w-full border p-3 rounded-lg bg-slate-50" />
                    </div>
                </div>
                <div>
                    <label className="block text-sm font-bold text-slate-700">URL Imagen</label>
                    <input name="rutaImagen" value={form.rutaImagen} onChange={handleChange} className="w-full border p-3 rounded-lg bg-slate-50 text-sm" placeholder="https://..." />
                </div>
                <div>
                    <label className="block text-sm font-bold text-slate-700">Descripción</label>
                    <textarea name="descripcion" value={form.descripcion} onChange={handleChange} className="w-full border p-3 rounded-lg bg-slate-50 h-20"></textarea>
                </div>

                <div className="flex gap-3">
                    {modoEdicion && (
                        <button type="button" onClick={handleCancelar} className="flex-1 bg-slate-500 text-white font-bold py-3 rounded-xl hover:bg-slate-600 transition">
                            CANCELAR
                        </button>
                    )}
                    <button type="submit" className={`flex-1 text-white font-bold py-3 rounded-xl shadow-lg transition ${modoEdicion ? 'bg-orange-500 hover:bg-orange-600' : 'bg-indigo-600 hover:bg-indigo-700'}`}>
                        {modoEdicion ? 'GUARDAR CAMBIOS' : 'CREAR PRODUCTO'}
                    </button>
                </div>
            </form>
        </div>

        {/* PREVIEW (Igual al anterior) */}
        <div className="w-full md:w-1/3">
            <div className="bg-white p-4 rounded-2xl shadow-lg border border-slate-100">
                <div className="aspect-square bg-slate-100 rounded-xl mb-4 overflow-hidden relative flex items-center justify-center">
                    {form.rutaImagen ? <img src={form.rutaImagen} className="object-contain w-full h-full" onError={e => e.target.src='/images/monstersinc2.png'} /> : <span className="text-4xl opacity-20">📷</span>}
                </div>
                <h4 className="font-bold text-slate-800">{form.nombre || "Producto"}</h4>
                <p className="text-green-600 font-black text-xl">${form.precioVenta || 0}</p>
            </div>
        </div>
      </div>

      {/* --- TABLA DE INVENTARIO (NUEVO) --- */}
      <h3 className="text-xl font-bold text-slate-700 mb-4">Inventario Actual ({productos.length})</h3>
      <div className="bg-white rounded-2xl shadow-lg overflow-hidden border border-slate-100">
         <table className="w-full text-sm text-left">
            <thead className="bg-slate-900 text-white uppercase font-bold">
                <tr>
                    <th className="p-4">ID</th>
                    <th className="p-4">Producto</th>
                    <th className="p-4">Precio</th>
                    <th className="p-4">Stock</th>
                    <th className="p-4 text-center">Acciones</th>
                </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
                {productos.map(p => (
                    <tr key={p.idElectrodomestico} className="hover:bg-slate-50">
                        <td className="p-4 font-mono text-slate-400">#{p.idElectrodomestico}</td>
                        <td className="p-4 font-bold text-slate-700">{p.nombre}</td>
                        <td className="p-4 text-green-600 font-bold">${p.precioVenta}</td>
                        <td className="p-4">
                            <span className={`px-2 py-1 rounded text-xs font-bold ${p.stock < 5 ? 'bg-red-100 text-red-600' : 'bg-green-100 text-green-600'}`}>
                                {p.stock} unid.
                            </span>
                        </td>
                        <td className="p-4 flex justify-center gap-2">
                            <button onClick={() => handleEditar(p)} className="bg-blue-100 text-blue-600 px-3 py-1 rounded hover:bg-blue-200 font-bold transition">
                                ✏️ Editar
                            </button>
                            <button onClick={() => handleEliminar(p.idElectrodomestico)} className="bg-red-100 text-red-600 px-3 py-1 rounded hover:bg-red-200 font-bold transition">
                                🗑️
                            </button>
                        </td>
                    </tr>
                ))}
            </tbody>
         </table>
      </div>
    </div>
  );
}