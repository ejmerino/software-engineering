import { useState } from 'react';
import { buscarFacturaPorId, consultarAmortizacion } from '../services/api'; // Importa consultarAmortizacion desde api.js

export default function Consultas() {
  const [activeTab, setActiveTab] = useState('factura'); // 'factura' o 'credito'
  
  // Estados Búsqueda
  const [idBusqueda, setIdBusqueda] = useState('');
  const [resultadoFactura, setResultadoFactura] = useState(null);
  const [resultadoAmortizacion, setResultadoAmortizacion] = useState(null);
  const [error, setError] = useState('');

  const handleBuscar = async () => {
    setError('');
    setResultadoFactura(null);
    setResultadoAmortizacion(null);
    
    if(!idBusqueda) return;

    try {
        if (activeTab === 'factura') {
            const f = await buscarFacturaPorId(idBusqueda);
            if(f) setResultadoFactura(f);
            else setError('Factura no encontrada');
        } else {
            // Lógica para Amortización
            const tabla = await consultarAmortizacion(idBusqueda); // Asegúrate de que esta función exista en api.js
            if(tabla && tabla.length > 0) setResultadoAmortizacion(tabla);
            else setError('No se encontró crédito o tabla para este ID');
        }
    } catch (e) {
        setError('Error al conectar con el servidor');
    }
  };

  return (
    <div className="animate-fadeIn max-w-4xl mx-auto">
      <h2 className="text-3xl font-bold text-slate-800 mb-6">Centro de Consultas</h2>

      {/* PESTAÑAS */}
      <div className="flex gap-4 mb-8 border-b border-slate-200">
        <button 
            onClick={() => { setActiveTab('factura'); setIdBusqueda(''); setError(''); }}
            className={`pb-3 px-4 font-bold text-lg transition border-b-4 ${activeTab === 'factura' ? 'border-blue-500 text-blue-600' : 'border-transparent text-slate-400 hover:text-slate-600'}`}
        >
            📄 Consultar Factura
        </button>
        <button 
            onClick={() => { setActiveTab('credito'); setIdBusqueda(''); setError(''); }}
            className={`pb-3 px-4 font-bold text-lg transition border-b-4 ${activeTab === 'credito' ? 'border-purple-500 text-purple-600' : 'border-transparent text-slate-400 hover:text-slate-600'}`}
        >
            💳 Tabla de Amortización
        </button>
      </div>

      {/* BARRA DE BÚSQUEDA */}
      <div className="bg-white p-6 rounded-2xl shadow-lg flex gap-4 items-center mb-8">
        <input 
            type="number" 
            placeholder={activeTab === 'factura' ? "Ingrese Número de Factura" : "Ingrese ID del Crédito"}
            value={idBusqueda}
            onChange={e => setIdBusqueda(e.target.value)}
            className="flex-1 bg-slate-50 border p-3 rounded-lg text-lg outline-none focus:ring-2 focus:ring-blue-500"
        />
        <button onClick={handleBuscar} className="bg-slate-800 text-white px-8 py-3 rounded-lg font-bold hover:bg-slate-900 transition">
            Buscar
        </button>
      </div>

      {/* MENSAJES DE ERROR */}
      {error && (
        <div className="bg-red-100 text-red-700 p-4 rounded-lg mb-6 text-center font-bold animate-pulse">
            ❌ {error}
        </div>
      )}

      {/* RESULTADO FACTURA */}
      {resultadoFactura && (
        <div className="bg-white rounded-2xl shadow-xl overflow-hidden border border-slate-100 animate-fadeIn">
            <div className="bg-blue-600 p-4 text-white flex justify-between items-center">
                <span className="font-bold text-lg">Factura #{resultadoFactura.idFactura}</span>
                <span className="bg-blue-500 px-3 py-1 rounded text-sm">{resultadoFactura.fecha}</span>
            </div>
            <div className="p-6">
                <div className="grid grid-cols-2 gap-4 mb-6">
                    <div>
                        <p className="text-sm text-slate-400 uppercase font-bold">Cliente</p>
                        <p className="font-bold text-slate-800">{resultadoFactura.cliente?.nombres} {resultadoFactura.cliente?.apellidos}</p>
                    </div>
                    <div className="text-right">
                         <p className="text-sm text-slate-400 uppercase font-bold">Total Pagado</p>
                         <p className="text-2xl font-bold text-green-600">${resultadoFactura.total}</p>
                    </div>
                </div>
                {/* Detalles Factura */}
                <table className="w-full text-sm">
                    <thead className="bg-slate-50 text-slate-500">
                        <tr><th className="p-2 text-left">Item</th><th className="p-2 text-right">Subtotal</th></tr>
                    </thead>
                    <tbody>
                        {resultadoFactura.detalles?.map((d, i) => (
                            <tr key={i} className="border-t">
                                <td className="p-2">{d.electrodomestico?.nombre} <span className="text-slate-400">x{d.cantidad}</span></td>
                                <td className="p-2 text-right font-bold">${d.subtotalLinea}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
      )}

      {/* RESULTADO AMORTIZACIÓN */}
      {resultadoAmortizacion && (
        <div className="bg-white rounded-2xl shadow-xl overflow-hidden border border-slate-100 animate-fadeIn">
            <div className="bg-purple-600 p-4 text-white">
                <h3 className="font-bold text-lg">Plan de Pagos (Crédito)</h3>
            </div>
            <div className="overflow-x-auto">
                <table className="w-full text-left">
                    <thead className="bg-purple-50 text-purple-900 font-bold">
                        <tr>
                            <th className="p-4">Cuota</th>
                            <th className="p-4">Fecha Pago</th>
                            <th className="p-4">Valor Cuota</th>
                            <th className="p-4">Interés</th>
                            <th className="p-4">Saldo Capital</th>
                        </tr>
                    </thead>
                    <tbody className="divide-y divide-purple-100">
                        {resultadoAmortizacion.map((row, idx) => (
                            <tr key={idx} className="hover:bg-purple-50/50 transition">
                                <td className="p-4 font-bold text-purple-700">{row.numeroCuota}</td>
                                <td className="p-4">{new Date(row.fechaPagoProgramada).toLocaleDateString()}</td>
                                <td className="p-4 font-bold">${row.valorCuota}</td>
                                <td className="p-4 text-slate-500">${row.interesPagado}</td>
                                <td className="p-4 text-slate-500">${row.saldoCapital}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
      )}
    </div>
  );
}