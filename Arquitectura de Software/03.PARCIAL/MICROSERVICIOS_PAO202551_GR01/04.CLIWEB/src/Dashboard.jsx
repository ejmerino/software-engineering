import React, { useState } from 'react';
import axios from 'axios';
import { 
  Search, DollarSign, History, RefreshCw, Send, LogOut, 
  ArrowRightLeft, Wallet, TrendingUp, AlertCircle, CheckCircle2 
} from 'lucide-react';

// Configura tu IP local aquí si es necesario
const api = axios.create({ baseURL: 'http://10.40.29.134:8080/api' });

export default function Dashboard({ onLogout }) {
  // ESTADOS
  const [busqueda, setBusqueda] = useState('');
  const [cuentaData, setCuentaData] = useState(null);
  const [historial, setHistorial] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  
  // Operaciones
  const [activeTab, setActiveTab] = useState('resumen');
  const [monto, setMonto] = useState('');
  const [destino, setDestino] = useState('');
  const [procesando, setProcesando] = useState(false);
  const [mensaje, setMensaje] = useState(null);

  // --- LÓGICA ROBUSTA ---
  
  const handleBuscarCuenta = async (e) => {
    e.preventDefault(); // Prevenir recarga
    if (!busqueda.trim()) return;

    setLoading(true);
    setError('');
    setCuentaData(null);
    setMensaje(null);

    try {
      const res = await api.get(`/cuentas/${busqueda}`);
      setCuentaData(res.data);
      cargarHistorial(busqueda);
    } catch (err) {
      console.error(err);
      setError('Cuenta no encontrada o error de conexión con el servidor.');
    } finally {
      setLoading(false);
    }
  };

  const cargarHistorial = async (idCuenta) => {
    try {
      const res = await api.get(`/historial/cuenta/${idCuenta}`);
      setHistorial(res.data);
    } catch (err) {
      setHistorial([]);
    }
  };

  const recargarDatos = () => {
    if (cuentaData) {
        api.get(`/cuentas/${cuentaData.codigo}`).then(res => setCuentaData(res.data));
        cargarHistorial(cuentaData.codigo);
    }
  };

  const handleTransaccion = async (e) => {
    e.preventDefault(); // ¡VITAL! Evita la pantalla blanca
    if (!monto) return;
    
    setProcesando(true);
    setMensaje(null);

    try {
      const endpoint = activeTab === 'retiro' ? '/transacciones/retiro' : '/transacciones/transferencia';
      const body = {
        importe: parseFloat(monto),
        empleado: "0001"
      };

      if (activeTab === 'retiro') {
        body.cuenta = cuentaData.codigo;
      } else {
        body.cuentaOrigen = cuentaData.codigo;
        body.cuentaDestino = destino;
      }

      await api.post(endpoint, body);
      
      setMensaje({ type: 'success', text: 'Transacción realizada exitosamente.' });
      setMonto('');
      setDestino('');
      recargarDatos();
    } catch (err) {
      const msg = err.response?.data?.mensaje || 'Error al procesar la solicitud.';
      setMensaje({ type: 'error', text: msg });
    } finally {
      setProcesando(false);
    }
  };

  // --- COMPONENTES UI ---

  // VISTA: BUSCADOR (Landing del Dashboard)
  if (!cuentaData) {
    return (
      <div className="min-h-screen bg-monster-bg flex flex-col items-center justify-center p-6 relative overflow-hidden">
        {/* Decoración de fondo */}
        <div className="absolute top-0 right-0 w-[800px] h-[800px] bg-monster-primary/10 rounded-full blur-[100px] -translate-y-1/2 translate-x-1/2"></div>
        
        <div className="glass-panel w-full max-w-2xl p-10 rounded-3xl relative z-10 text-center animate-slide-up">
            <div className="w-20 h-20 bg-gradient-to-tr from-monster-success to-teal-400 rounded-full flex items-center justify-center mx-auto mb-8 shadow-lg shadow-monster-success/30">
                <Search className="text-white w-10 h-10" />
            </div>
            
            <h2 className="text-3xl font-bold text-white mb-2">Búsqueda de Cliente</h2>
            <p className="text-monster-muted mb-8 text-lg">Ingrese el número de cuenta para iniciar operaciones.</p>
            
            <form onSubmit={handleBuscarCuenta} className="relative max-w-md mx-auto">
                <input 
                    type="text" 
                    className="w-full pl-6 pr-14 py-4 bg-slate-900/80 border border-slate-700 rounded-2xl text-white text-lg placeholder-slate-500 focus:border-monster-success focus:ring-1 focus:ring-monster-success outline-none transition-all"
                    placeholder="Ej: 00100001"
                    value={busqueda}
                    onChange={(e) => setBusqueda(e.target.value)}
                    autoFocus
                />
                <button 
                    type="submit"
                    disabled={loading}
                    className="absolute right-2 top-2 bottom-2 aspect-square bg-monster-success hover:bg-emerald-400 text-white rounded-xl transition-all flex items-center justify-center"
                >
                    {loading ? <RefreshCw className="animate-spin" /> : <Search />}
                </button>
            </form>

            {error && (
                <div className="mt-8 p-4 bg-red-500/10 border border-red-500/20 rounded-xl text-red-200 flex items-center justify-center gap-2">
                    <AlertCircle size={20} /> {error}
                </div>
            )}
            
            <button onClick={onLogout} className="mt-8 text-monster-muted hover:text-white text-sm flex items-center gap-2 mx-auto transition-colors">
                <LogOut size={16} /> Cerrar Sesión
            </button>
        </div>
      </div>
    );
  }

  // VISTA: DASHBOARD COMPLETO
  return (
    <div className="min-h-screen bg-monster-bg text-monster-text flex flex-col md:flex-row">
      
      {/* SIDEBAR */}
      <aside className="w-full md:w-72 glass-panel border-r-0 border-b md:border-b-0 md:border-r flex flex-col z-20">
        <div className="p-8">
            <div className="flex items-center gap-3 mb-1">
                <div className="w-10 h-10 bg-gradient-to-br from-monster-primary to-monster-accent rounded-lg flex items-center justify-center font-bold text-xl text-white">E</div>
                <span className="font-bold text-xl tracking-tight">EUREKABANK</span>
            </div>
            <p className="text-xs text-monster-muted uppercase tracking-widest pl-1">Monsters Inc.</p>
        </div>

        <nav className="flex-1 px-4 space-y-2">
            <button 
                onClick={() => setActiveTab('resumen')} 
                className={`w-full flex items-center gap-3 px-4 py-4 rounded-xl transition-all ${activeTab === 'resumen' ? 'bg-monster-primary text-white shadow-lg shadow-monster-primary/20' : 'text-monster-muted hover:bg-white/5 hover:text-white'}`}
            >
                <History size={20} /> <span className="font-medium">Historial</span>
            </button>
            <button 
                onClick={() => setActiveTab('retiro')} 
                className={`w-full flex items-center gap-3 px-4 py-4 rounded-xl transition-all ${activeTab === 'retiro' ? 'bg-monster-primary text-white shadow-lg shadow-monster-primary/20' : 'text-monster-muted hover:bg-white/5 hover:text-white'}`}
            >
                <DollarSign size={20} /> <span className="font-medium">Retiro</span>
            </button>
            <button 
                onClick={() => setActiveTab('transferencia')} 
                className={`w-full flex items-center gap-3 px-4 py-4 rounded-xl transition-all ${activeTab === 'transferencia' ? 'bg-monster-primary text-white shadow-lg shadow-monster-primary/20' : 'text-monster-muted hover:bg-white/5 hover:text-white'}`}
            >
                <ArrowRightLeft size={20} /> <span className="font-medium">Transferencia</span>
            </button>
        </nav>

        <div className="p-4 border-t border-slate-700/50">
            <button onClick={() => { setCuentaData(null); setBusqueda(''); }} className="w-full py-3 text-sm text-monster-muted hover:text-white transition-colors flex justify-center items-center gap-2 mb-2">
                <Search size={16} /> Cambiar Cuenta
            </button>
            <button onClick={onLogout} className="w-full py-3 bg-red-500/10 hover:bg-red-500/20 text-red-200 rounded-xl transition-colors flex justify-center items-center gap-2">
                <LogOut size={16} /> Salir
            </button>
        </div>
      </aside>

      {/* MAIN CONTENT */}
      <main className="flex-1 overflow-y-auto bg-slate-900/50 p-4 md:p-8 relative">
        {/* Fondo decorativo */}
        <div className="absolute top-0 left-0 w-full h-[300px] bg-gradient-to-b from-monster-primary/5 to-transparent pointer-events-none"></div>

        <div className="max-w-5xl mx-auto space-y-8 relative z-10">
            
            {/* Header Cuenta */}
            <div className="flex flex-col md:flex-row justify-between items-end gap-6 animate-slide-up">
                <div>
                    <h2 className="text-3xl font-bold text-white mb-2">Hola, Cliente {cuentaData.codigo}</h2>
                    <div className="flex items-center gap-3">
                        <span className={`px-3 py-1 rounded-full text-xs font-bold uppercase ${cuentaData.estado === 'ACTIVO' ? 'bg-emerald-500/20 text-emerald-400' : 'bg-red-500/20 text-red-400'}`}>
                            {cuentaData.estado}
                        </span>
                        <span className="text-monster-muted text-sm">Sucursal: Matriz (Quito)</span>
                    </div>
                </div>
                
                <div className="glass-panel px-8 py-6 rounded-2xl flex items-center gap-6 min-w-[300px]">
                    <div className="bg-monster-success/20 p-3 rounded-full text-monster-success">
                        <Wallet size={32} />
                    </div>
                    <div>
                        <p className="text-monster-muted text-xs uppercase font-bold tracking-wider mb-1">Saldo Disponible</p>
                        <p className="text-4xl font-bold text-white">${cuentaData.saldo.toFixed(2)}</p>
                    </div>
                </div>
            </div>

            {/* Alertas / Mensajes */}
            {mensaje && (
                <div className={`p-4 rounded-xl flex items-center gap-3 animate-slide-up ${mensaje.type === 'success' ? 'bg-emerald-500/10 border border-emerald-500/20 text-emerald-400' : 'bg-red-500/10 border border-red-500/20 text-red-400'}`}>
                    {mensaje.type === 'success' ? <CheckCircle2 /> : <AlertCircle />}
                    <span className="font-medium">{mensaje.text}</span>
                </div>
            )}

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                
                {/* PANEL PRINCIPAL (Historial o Formulario) */}
                <div className="lg:col-span-3 glass-panel rounded-3xl p-1 overflow-hidden min-h-[500px]">
                    
                    {/* VISTA: FORMULARIOS DE TRANSACCIÓN */}
                    {activeTab !== 'resumen' && (
                        <div className="p-8 max-w-xl mx-auto py-20 animate-slide-up">
                            <h3 className="text-2xl font-bold text-white mb-2 flex items-center gap-3">
                                {activeTab === 'retiro' ? <DollarSign className="text-monster-accent" /> : <ArrowRightLeft className="text-monster-accent" />}
                                {activeTab === 'retiro' ? 'Retirar Fondos' : 'Transferir Fondos'}
                            </h3>
                            <p className="text-monster-muted mb-8">Complete los datos para procesar la transacción.</p>
                            
                            <form onSubmit={handleTransaccion} className="space-y-6">
                                {activeTab === 'transferencia' && (
                                    <div>
                                        <label className="block text-sm font-medium text-monster-muted mb-2">Cuenta Destino</label>
                                        <input 
                                            type="text" required 
                                            className="w-full p-4 bg-slate-900/50 border border-slate-700 rounded-xl text-white focus:border-monster-primary focus:ring-1 focus:ring-monster-primary outline-none transition-all"
                                            placeholder="00100002"
                                            value={destino} onChange={e => setDestino(e.target.value)}
                                        />
                                    </div>
                                )}
                                
                                <div>
                                    <label className="block text-sm font-medium text-monster-muted mb-2">Monto ($)</label>
                                    <input 
                                        type="number" step="0.01" min="0.01" required 
                                        className="w-full p-4 bg-slate-900/50 border border-slate-700 rounded-xl text-white text-2xl font-bold placeholder-slate-600 focus:border-monster-primary focus:ring-1 focus:ring-monster-primary outline-none transition-all"
                                        placeholder="0.00"
                                        value={monto} onChange={e => setMonto(e.target.value)}
                                    />
                                </div>

                                <button 
                                    type="submit" 
                                    disabled={procesando}
                                    className="w-full py-4 mt-4 bg-monster-primary hover:bg-indigo-500 text-white font-bold rounded-xl shadow-lg shadow-indigo-500/20 transition-all active:scale-[0.98] flex justify-center items-center gap-2"
                                >
                                    {procesando ? <RefreshCw className="animate-spin" /> : <Send size={20} />}
                                    Confirmar {activeTab === 'retiro' ? 'Retiro' : 'Transferencia'}
                                </button>
                            </form>
                        </div>
                    )}

                    {/* VISTA: HISTORIAL */}
                    {activeTab === 'resumen' && (
                        <div className="h-full flex flex-col">
                            <div className="p-6 border-b border-white/5 flex justify-between items-center bg-slate-800/30">
                                <h3 className="font-bold text-white text-lg">Últimos Movimientos</h3>
                                <button onClick={recargarDatos} className="p-2 hover:bg-white/10 rounded-lg text-monster-muted hover:text-white transition-colors">
                                    <RefreshCw size={18} />
                                </button>
                            </div>
                            <div className="p-4">
                                <table className="w-full">
                                    <thead>
                                        <tr className="text-left text-xs uppercase text-monster-muted font-semibold">
                                            <th className="px-4 py-3">Fecha</th>
                                            <th className="px-4 py-3">Tipo</th>
                                            <th className="px-4 py-3 text-right">Monto</th>
                                        </tr>
                                    </thead>
                                    <tbody className="divide-y divide-white/5">
                                        {historial.length > 0 ? (
                                            historial.map((m, i) => (
                                                <tr key={i} className="hover:bg-white/5 transition-colors">
                                                    <td className="px-4 py-4 text-sm text-slate-300">
                                                        {new Date(m.fecha).toLocaleDateString()}
                                                    </td>
                                                    <td className="px-4 py-4">
                                                        <div className="flex items-center gap-3">
                                                            <div className={`p-2 rounded-lg ${['008','003'].includes(m.tipo) ? 'bg-emerald-500/10 text-emerald-400' : 'bg-red-500/10 text-red-400'}`}>
                                                                {['008','003'].includes(m.tipo) ? <TrendingUp size={16}/> : <DollarSign size={16}/>}
                                                            </div>
                                                            <span className="text-sm font-medium text-white">
                                                                {m.tipo === '004' && 'Retiro'}
                                                                {m.tipo === '009' && 'Transferencia Enviada'}
                                                                {m.tipo === '008' && 'Transferencia Recibida'}
                                                                {!['004','008','009'].includes(m.tipo) && 'Operación Varios'}
                                                            </span>
                                                        </div>
                                                    </td>
                                                    <td className={`px-4 py-4 text-right font-bold ${['008','003'].includes(m.tipo) ? 'text-emerald-400' : 'text-white'}`}>
                                                        {['008','003'].includes(m.tipo) ? '+' : '-'}${m.importe.toFixed(2)}
                                                    </td>
                                                </tr>
                                            ))
                                        ) : (
                                            <tr>
                                                <td colSpan="3" className="text-center py-20 text-monster-muted">No hay movimientos recientes.</td>
                                            </tr>
                                        )}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
      </main>
    </div>
  );
}