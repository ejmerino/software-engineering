import { useState, useEffect } from 'react';
import { buscarCliente, registrarCliente, procesarVenta, listarProductos, consultarAmortizacion } from '../services/api';
import InvoiceModal from '../components/InvoiceModal';

export default function Venta() {
  const [cedula, setCedula] = useState('');
  const [cliente, setCliente] = useState(null);
  const [catalogo, setCatalogo] = useState([]); 
  const [carrito, setCarrito] = useState([]);
  const [idProducto, setIdProducto] = useState('');
  const [cantidad, setCantidad] = useState(1);
  const [formaPago, setFormaPago] = useState('Efectivo');
  const [cuotas, setCuotas] = useState(0);
  const [showModal, setShowModal] = useState(false);
  const [facturaFinal, setFacturaFinal] = useState(null);
  const [tablaAmortizacion, setTablaAmortizacion] = useState(null);

  useEffect(() => { listarProductos().then(setCatalogo).catch(console.error); }, []);

  const handleBuscarCliente = async () => {
    if(!cedula) return;
    const c = await buscarCliente(cedula);
    if (c) setCliente(c);
    else if(confirm("¿Cliente nuevo? Desea registrarlo rápido?")) {
         const nombre = prompt("Nombre:");
         const apellido = prompt("Apellido:");
         if(nombre) setCliente(await registrarCliente({ cedula, nombres: nombre, apellidos: apellido, direccion: "App", telefono: "099", email: "app@mail.com" }));
    }
  };

  const agregarCarrito = () => {
    const prod = catalogo.find(p => p.idElectrodomestico == idProducto);
    if (!prod || prod.stock < cantidad) return alert("Stock insuficiente");
    setCarrito([...carrito, { ...prod, cantidadComp: parseInt(cantidad) }]);
    setCantidad(1); setIdProducto('');
  };

  const eliminarItem = (index) => {
    const nuevo = [...carrito]; nuevo.splice(index, 1); setCarrito(nuevo);
  };

  const finalizarVenta = async () => {
    if (!cliente || carrito.length === 0) return alert("Faltan datos");
    const peticion = { cedulaCliente: cliente.cedula, formaPago, numeroCuotas: formaPago === 'Credito' ? parseInt(cuotas) : 0, items: carrito.map(i => ({ idElectrodomestico: i.idElectrodomestico, cantidad: i.cantidadComp })) };
    try {
        const resp = await procesarVenta(peticion);
        if (resp.exito) {
            setFacturaFinal(resp.facturaGenerada);
            if(resp.facturaGenerada.idCreditoBanco) setTablaAmortizacion(await consultarAmortizacion(resp.facturaGenerada.idCreditoBanco));
            else setTablaAmortizacion(null);
            setShowModal(true);
            setCarrito([]); setCliente(null); setCedula('');
        } else alert("Error: " + resp.mensaje);
    } catch (e) { alert("Error de servidor"); }
  };

  const totalEstimado = carrito.reduce((a, b) => a + (b.precioVenta * b.cantidadComp), 0);
  const totalConDesc = formaPago === 'Efectivo' ? totalEstimado * 0.67 : totalEstimado;

  return (
    <div className="animate-fade">
      <InvoiceModal isOpen={showModal} onClose={() => setShowModal(false)} factura={facturaFinal} amortizacion={tablaAmortizacion} />

      <h2 className="text-xl font-bold text-slate-400 uppercase tracking-widest mb-4 px-2">Nueva Transacción</h2>

      {/* 1. TARJETA CLIENTE (Diseño Limpio) */}
      <div className="card-neumorphic p-5 mb-5 mx-2">
         <div className="flex gap-3 mb-3">
            <div className="flex-1 relative">
                <input 
                    type="number" 
                    placeholder="Cédula Cliente" 
                    value={cedula} 
                    onChange={e => setCedula(e.target.value)}
                    className="input-dark w-full font-mono text-lg"
                />
                <button 
                    onClick={handleBuscarCliente} 
                    className="absolute right-2 top-2 bottom-2 bg-indigo-600 text-white px-4 rounded-lg font-bold shadow-lg shadow-indigo-500/30 active:scale-95 transition"
                >
                    🔍
                </button>
            </div>
         </div>
         
         {/* Feedback Cliente */}
         <div className={`p-3 rounded-xl border flex justify-between items-center transition-all ${cliente ? 'bg-emerald-500/10 border-emerald-500/30' : 'bg-slate-900 border-slate-700'}`}>
            <div className="flex flex-col">
                <span className="text-[10px] uppercase font-bold text-slate-500">Nombre Cliente</span>
                <span className={`font-medium ${cliente ? 'text-emerald-400' : 'text-slate-400'}`}>
                    {cliente ? `${cliente.nombres} ${cliente.apellidos}` : 'No seleccionado'}
                </span>
            </div>
            {cliente && <div className="w-3 h-3 rounded-full bg-emerald-500 shadow-[0_0_10px_#10b981]"></div>}
         </div>
      </div>

      {/* 2. AGREGAR PRODUCTOS */}
      <div className="card-neumorphic p-5 mb-24 mx-2">
         <span className="text-xs font-bold text-slate-500 uppercase block mb-3">Añadir al Carrito</span>
         
         <div className="flex flex-col gap-4">
             <select 
                value={idProducto} 
                onChange={e => setIdProducto(e.target.value)}
                className="input-dark w-full text-white font-medium appearance-none"
             >
                <option value="">👇 Seleccionar Producto...</option>
                {catalogo.map(p => <option key={p.idElectrodomestico} value={p.idElectrodomestico}>{p.nombre} (${p.precioVenta})</option>)}
             </select>

             <div className="flex gap-3">
                 <div className="w-1/3">
                    <input type="number" value={cantidad} onChange={e => setCantidad(e.target.value)} className="input-dark w-full text-center font-bold text-xl" />
                 </div>
                 <button 
                    onClick={agregarCarrito} 
                    className="flex-1 bg-slate-100 text-slate-900 font-bold rounded-xl hover:bg-white active:scale-95 transition shadow-lg"
                 >
                    + AÑADIR
                 </button>
             </div>
         </div>

         {/* LISTA DE ITEMS (Estilo Recibo) */}
         <div className="mt-6 space-y-3">
            {carrito.length === 0 && <p className="text-center text-slate-600 text-sm italic py-4">Carrito vacío</p>}
            
            {carrito.map((item, idx) => (
                <div key={idx} className="bg-slate-900/50 border border-slate-700/50 p-3 rounded-xl flex justify-between items-center animate-fade">
                    <div className="flex items-center gap-3">
                        <div className="bg-slate-800 p-2 rounded-lg text-slate-300 font-bold text-xs">x{item.cantidadComp}</div>
                        <div>
                            <p className="text-white font-medium text-sm">{item.nombre}</p>
                            <p className="text-slate-500 text-xs">${item.precioVenta} c/u</p>
                        </div>
                    </div>
                    <div className="text-right">
                        <p className="text-emerald-400 font-bold font-mono">${(item.cantidadComp * item.precioVenta).toFixed(2)}</p>
                        <button onClick={() => eliminarItem(idx)} className="text-rose-500 text-[10px] font-bold mt-1 uppercase">Eliminar</button>
                    </div>
                </div>
            ))}
         </div>
      </div>

      {/* 3. STICKY FOOTER (Panel de Pago Fijo) */}
      <div className="fixed bottom-20 left-0 w-full bg-[#0f172a] border-t border-slate-800 p-4 z-40 rounded-t-2xl shadow-[0_-5px_20px_rgba(0,0,0,0.5)]">
         
         {/* SELECTOR TIPO PAGO (Segmented Control) */}
         <div className="flex bg-slate-900 p-1 rounded-xl mb-4 border border-slate-800">
             <button 
                onClick={() => setFormaPago('Efectivo')}
                className={`flex-1 py-2 rounded-lg text-xs font-bold transition-all ${formaPago === 'Efectivo' ? 'bg-indigo-600 text-white shadow-lg' : 'text-slate-400 hover:text-white'}`}
             >
                💵 EFECTIVO
             </button>
             <button 
                onClick={() => setFormaPago('Credito')}
                className={`flex-1 py-2 rounded-lg text-xs font-bold transition-all ${formaPago === 'Credito' ? 'bg-indigo-600 text-white shadow-lg' : 'text-slate-400 hover:text-white'}`}
             >
                💳 CRÉDITO
             </button>
         </div>

         {formaPago === 'Credito' && (
            <div className="mb-4 flex items-center gap-3 animate-fade">
                <span className="text-xs font-bold text-slate-400">CUOTAS:</span>
                <input type="number" value={cuotas} onChange={e => setCuotas(e.target.value)} className="input-dark flex-1 py-2 text-center" placeholder="3-24" />
            </div>
         )}

         <div className="flex gap-4">
             <div className="flex-1">
                 <p className="text-[10px] text-slate-400 uppercase font-bold">Total a Pagar</p>
                 <p className="text-2xl font-black text-white tracking-tight">${(formaPago === 'Efectivo' ? totalConDesc : totalEstimado).toFixed(2)}</p>
             </div>
             <button 
                onClick={finalizarVenta}
                disabled={carrito.length === 0}
                className={`flex-1 rounded-xl font-bold text-sm uppercase shadow-lg active:scale-95 transition ${carrito.length === 0 ? 'bg-slate-800 text-slate-500' : 'bg-emerald-500 text-slate-900 hover:bg-emerald-400'}`}
             >
                {carrito.length === 0 ? 'Vacío' : 'Cobrar ➔'}
             </button>
         </div>
      </div>
    </div>
  );
}