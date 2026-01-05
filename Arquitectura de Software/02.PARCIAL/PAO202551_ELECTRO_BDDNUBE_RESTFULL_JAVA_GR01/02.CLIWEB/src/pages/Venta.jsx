import { useState, useEffect } from 'react';
import { buscarCliente, registrarCliente, procesarVenta, listarProductos, consultarAmortizacion } from '../services/api'; // Asegúrate de importar consultarAmortizacion
import InvoiceModal from '../components/InvoiceModal';

export default function Venta() {
  // Estados Datos
  const [cedula, setCedula] = useState('');
  const [cliente, setCliente] = useState(null);
  const [catalogo, setCatalogo] = useState([]); 
  
  // Estados Carrito
  const [carrito, setCarrito] = useState([]);
  const [idProducto, setIdProducto] = useState('');
  const [cantidad, setCantidad] = useState(1);
  
  // Estados Pago
  const [formaPago, setFormaPago] = useState('Efectivo');
  const [cuotas, setCuotas] = useState(0);

  // Estados Modal
  const [showModal, setShowModal] = useState(false);
  const [facturaFinal, setFacturaFinal] = useState(null);
  const [tablaAmortizacion, setTablaAmortizacion] = useState(null);

  useEffect(() => {
    listarProductos().then(setCatalogo);
  }, []);

  const handleBuscarCliente = async () => {
    if(!cedula) return;
    const c = await buscarCliente(cedula);
    if (c) {
        setCliente(c);
    } else {
        if(confirm("Cliente no encontrado. ¿Registrar rápido?")) {
            const nombre = prompt("Nombre:");
            const apellido = prompt("Apellido:");
            if(nombre && apellido) {
                const nuevo = await registrarCliente({ 
                    cedula, nombres: nombre, apellidos: apellido, 
                    direccion: "Tienda Web", telefono: "0999999999", email: "cliente@web.com" 
                });
                setCliente(nuevo);
            }
        }
    }
  };

  const agregarCarrito = () => {
    const prod = catalogo.find(p => p.idElectrodomestico == idProducto);
    if (!prod) return;
    if (prod.stock < cantidad) return alert("Stock insuficiente");
    
    setCarrito([...carrito, { ...prod, cantidadComp: parseInt(cantidad) }]);
    setCantidad(1); // Reset cantidad
  };

  const eliminarItem = (index) => {
    const nuevoCarrito = [...carrito];
    nuevoCarrito.splice(index, 1);
    setCarrito(nuevoCarrito);
  };

  const finalizarVenta = async () => {
    if (!cliente || carrito.length === 0) return alert("Faltan datos (Cliente o Productos)");

    const peticion = {
        cedulaCliente: cliente.cedula,
        formaPago,
        numeroCuotas: formaPago === 'Credito' ? parseInt(cuotas) : 0,
        items: carrito.map(i => ({ idElectrodomestico: i.idElectrodomestico, cantidad: i.cantidadComp }))
    };

    try {
        const resp = await procesarVenta(peticion);
        if (resp.exito) {
            setFacturaFinal(resp.facturaGenerada);
            
            // Si hay crédito, buscamos la tabla para mostrarla en el modal
            if(resp.facturaGenerada.idCreditoBanco && resp.facturaGenerada.idCreditoBanco > 0) {
                 const tabla = await consultarAmortizacion(resp.facturaGenerada.idCreditoBanco); // Asegúrate de tener esta función en api.js
                 setTablaAmortizacion(tabla);
            } else {
                 setTablaAmortizacion(null);
            }

            setShowModal(true); // ABRIR MODAL
            
            // Limpiar formulario de fondo
            setCarrito([]);
            setCliente(null);
            setCedula('');
        } else {
            alert("Error: " + resp.mensaje);
        }
    } catch (e) {
        console.error(e);
        alert("Error de conexión con el servidor");
    }
  };

  // Cálculos Visuales
  const totalEstimado = carrito.reduce((acc, item) => acc + (item.precioVenta * item.cantidadComp), 0);

  return (
    <div className="animate-fadeIn">
      {/* MODAL DE ÉXITO */}
      <InvoiceModal 
        isOpen={showModal} 
        onClose={() => setShowModal(false)} 
        factura={facturaFinal} 
        amortizacion={tablaAmortizacion}
      />

      <div className="flex flex-col lg:flex-row gap-6">
        
        {/* COLUMNA IZQUIERDA: CONFIGURACIÓN (2/3 ancho) */}
        <div className="lg:w-2/3 space-y-6">
          
          {/* TARJETA 1: CLIENTE */}
          <div className="bg-white p-6 rounded-2xl shadow-lg border border-slate-100">
             <h3 className="text-lg font-bold text-slate-800 mb-4 flex items-center gap-2">
                👤 Datos del Cliente
             </h3>
             <div className="flex gap-3">
                <input 
                    type="text" 
                    placeholder="Cédula" 
                    value={cedula} 
                    onChange={e => setCedula(e.target.value)}
                    className="flex-1 bg-slate-50 border p-3 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                />
                <button onClick={handleBuscarCliente} className="bg-blue-600 text-white px-6 rounded-lg font-bold hover:bg-blue-700 transition">
                    Buscar
                </button>
             </div>
             {cliente && (
                <div className="mt-4 p-3 bg-green-50 border border-green-200 rounded-lg text-green-800 font-medium flex justify-between items-center animate-fadeIn">
                    <span>✅ {cliente.nombres} {cliente.apellidos}</span>
                    <span className="text-xs bg-green-200 px-2 py-1 rounded text-green-800">{cliente.email}</span>
                </div>
             )}
          </div>

          {/* TARJETA 2: AGREGAR PRODUCTOS */}
          <div className="bg-white p-6 rounded-2xl shadow-lg border border-slate-100">
             <h3 className="text-lg font-bold text-slate-800 mb-4 flex items-center gap-2">
                📦 Agregar Productos
             </h3>
             <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                <div className="md:col-span-2">
                    <label className="text-xs font-bold text-slate-400 uppercase">Producto</label>
                    <select 
                        value={idProducto} 
                        onChange={e => setIdProducto(e.target.value)} 
                        className="w-full bg-slate-50 border p-3 rounded-lg outline-none"
                    >
                        <option value="">Seleccione...</option>
                        {catalogo.map(p => (
                            <option key={p.idElectrodomestico} value={p.idElectrodomestico}>
                                {p.nombre} - ${p.precioVenta} (Stock: {p.stock})
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label className="text-xs font-bold text-slate-400 uppercase">Cantidad</label>
                    <input 
                        type="number" 
                        min="1"
                        value={cantidad} 
                        onChange={e => setCantidad(e.target.value)} 
                        className="w-full bg-slate-50 border p-3 rounded-lg outline-none"
                    />
                </div>
                <div className="flex items-end">
                    <button onClick={agregarCarrito} className="w-full bg-slate-800 text-white p-3 rounded-lg font-bold hover:bg-slate-900 transition">
                        + Añadir
                    </button>
                </div>
             </div>
          </div>
        </div>

        {/* COLUMNA DERECHA: RESUMEN Y PAGO (1/3 ancho) */}
        <div className="lg:w-1/3 space-y-6">
            
            {/* TICKET / CARRITO */}
            <div className="bg-white rounded-2xl shadow-xl overflow-hidden flex flex-col h-full border border-slate-100">
                <div className="bg-slate-800 p-4 text-white font-bold flex justify-between">
                    <span>Carrito de Compra</span>
                    <span>{carrito.length} items</span>
                </div>
                
                <div className="flex-1 overflow-y-auto max-h-[300px] p-2 space-y-2 bg-slate-50">
                    {carrito.length === 0 ? (
                        <p className="text-center text-slate-400 py-10">Carrito vacío</p>
                    ) : (
                        carrito.map((item, idx) => (
                            <div key={idx} className="bg-white p-3 rounded-lg shadow-sm flex justify-between items-center group">
                                <div>
                                    <p className="font-bold text-slate-700 text-sm">{item.nombre}</p>
                                    <p className="text-xs text-slate-500">{item.cantidadComp} x ${item.precioVenta}</p>
                                </div>
                                <div className="flex items-center gap-3">
                                    <span className="font-bold text-slate-800">${(item.cantidadComp * item.precioVenta).toFixed(2)}</span>
                                    <button onClick={() => eliminarItem(idx)} className="text-red-400 hover:text-red-600 opacity-0 group-hover:opacity-100 transition">
                                        🗑️
                                    </button>
                                </div>
                            </div>
                        ))
                    )}
                </div>

                {/* ZONA DE PAGO */}
                <div className="p-6 bg-white border-t border-slate-200">
                    <div className="mb-4">
                        <label className="text-xs font-bold text-slate-400 uppercase">Forma de Pago</label>
                        <select 
                            value={formaPago} 
                            onChange={e => setFormaPago(e.target.value)} 
                            className="w-full border-b-2 border-slate-200 py-2 outline-none font-bold text-slate-700 focus:border-blue-500 bg-transparent"
                        >
                            <option value="Efectivo">💵 Efectivo (33% Descuento)</option>
                            <option value="Credito">💳 Crédito Diferido</option>
                        </select>
                    </div>

                    {formaPago === 'Credito' && (
                        <div className="mb-4 animate-fadeIn">
                             <label className="text-xs font-bold text-slate-400 uppercase">Cuotas (3-24)</label>
                             <input 
                                type="number" 
                                value={cuotas} 
                                onChange={e => setCuotas(e.target.value)}
                                className="w-full border-b-2 border-slate-200 py-2 outline-none font-bold text-slate-700"
                             />
                        </div>
                    )}

                    <div className="flex justify-between items-center mb-6">
                        <span className="text-slate-500">Total Estimado</span>
                        <span className="text-3xl font-extrabold text-slate-800">${totalEstimado.toFixed(2)}</span>
                    </div>

                    <button 
                        onClick={finalizarVenta} 
                        className="w-full bg-gradient-to-r from-green-500 to-emerald-600 text-white py-4 rounded-xl font-bold text-lg shadow-lg shadow-green-500/30 hover:shadow-green-500/50 transform active:scale-95 transition"
                    >
                        CONFIRMAR VENTA
                    </button>
                </div>
            </div>
        </div>
      </div>
    </div>
  );
}