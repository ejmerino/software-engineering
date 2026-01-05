import { useState } from 'react';
import { buscarFacturaPorId, consultarAmortizacion } from '../services/api';

export default function Consultas() {
  const [activeTab, setActiveTab] = useState('factura');
  const [idBusqueda, setIdBusqueda] = useState('');
  const [data, setData] = useState(null);
  const [error, setError] = useState('');

  const handleBuscar = async () => {
    setError(''); setData(null);
    if(!idBusqueda) return;

    try {
        if (activeTab === 'factura') {
            const f = await buscarFacturaPorId(idBusqueda);
            if(f) setData(f);
            else setError('Factura no encontrada');
        } else {
            const t = await consultarAmortizacion(idBusqueda);
            if(t && t.length > 0) setData(t);
            else setError('Sin datos de crédito');
        }
    } catch (e) { setError('Error de conexión'); }
  };

  return (
    <div className="animate-fade px-4 pb-24 pt-6">
      <h2 className="text-xl font-bold text-slate-400 uppercase tracking-widest mb-4">Centro de Consultas</h2>

      {/* 1. TABS (Segmented Control) */}
      <div className="flex bg-slate-900 p-1 rounded-xl mb-6 border border-slate-800 mx-auto max-w-md">
         <button 
            onClick={() => { setActiveTab('factura'); setIdBusqueda(''); setData(null); }}
            className={`flex-1 py-3 rounded-lg text-xs font-bold transition-all ${activeTab === 'factura' ? 'bg-indigo-600 text-white shadow-lg' : 'text-slate-400 hover:text-white'}`}
         >
            📄 FACTURA
         </button>
         <button 
            onClick={() => { setActiveTab('credito'); setIdBusqueda(''); setData(null); }}
            className={`flex-1 py-3 rounded-lg text-xs font-bold transition-all ${activeTab === 'credito' ? 'bg-purple-600 text-white shadow-lg' : 'text-slate-400 hover:text-white'}`}
         >
            💳 CRÉDITO
         </button>
      </div>

      {/* 2. BUSCADOR */}
      <div className="flex gap-3 mb-8">
         <input 
            type="number" 
            placeholder={activeTab === 'factura' ? "Nro. Factura" : "ID Crédito"}
            value={idBusqueda}
            onChange={e => setIdBusqueda(e.target.value)}
            className="input-dark flex-1 text-lg"
         />
         <button onClick={handleBuscar} className="bg-slate-700 text-white px-6 rounded-xl font-bold hover:bg-slate-600 transition shadow-lg">
            🔍
         </button>
      </div>

      {/* MENSAJE ERROR */}
      {error && <div className="bg-rose-500/10 border border-rose-500/30 text-rose-400 p-4 rounded-xl text-center font-bold mb-4 animate-fade">{error}</div>}

      {/* 3. RESULTADOS */}
      
      {/* VISTA FACTURA (Card Oscura) */}
      {activeTab === 'factura' && data && (
        <div className="card-neumorphic overflow-hidden animate-fade">
            <div className="bg-indigo-600/20 p-4 border-b border-indigo-500/20 flex justify-between items-center">
                <span className="text-indigo-400 font-bold uppercase text-xs tracking-wider">Factura #{data.idFactura}</span>
                <span className="text-white font-bold text-sm bg-indigo-500 px-2 py-1 rounded">{new Date(data.fecha).toLocaleDateString()}</span>
            </div>
            <div className="p-6">
                <div className="mb-6">
                    <p className="text-[10px] text-slate-500 uppercase font-bold tracking-widest">Cliente</p>
                    <p className="text-white font-medium text-xl">{data.cliente?.nombres} {data.cliente?.apellidos}</p>
                    <p className="text-slate-400 text-sm">{data.cliente?.cedula}</p>
                </div>
                
                <div className="bg-slate-950/50 rounded-xl p-4 mb-6 space-y-3 border border-slate-800">
                    {data.detalles?.map((d, i) => (
                        <div key={i} className="flex justify-between text-sm border-b border-slate-800/50 pb-2 last:border-0 last:pb-0">
                            <div>
                                <p className="text-slate-200 font-medium">{d.electrodomestico?.nombre}</p>
                                <p className="text-slate-500 text-xs">Cant: {d.cantidad}</p>
                            </div>
                            <span className="text-white font-mono font-bold">${d.subtotalLinea}</span>
                        </div>
                    ))}
                </div>

                <div className="flex justify-between items-end border-t border-slate-700 pt-4">
                    <div className="flex flex-col">
                        <span className="text-slate-400 text-xs uppercase font-bold">Total Pagado</span>
                        <span className="text-xs text-slate-500">{data.formaPago}</span>
                    </div>
                    <span className="text-emerald-400 text-3xl font-black tracking-tight">${data.total}</span>
                </div>
            </div>
        </div>
      )}

      {/* VISTA AMORTIZACIÓN (ESTILO IDENTICO A LA IMAGEN) */}
      {activeTab === 'credito' && data && (
        <div className="rounded-xl overflow-hidden shadow-2xl animate-fade bg-white text-slate-800 font-sans">
            
            {/* ENCABEZADO MORADO SÓLIDO */}
            <div className="bg-[#8b5cf6] p-4 flex items-center gap-3">
                <div className="bg-white/20 p-2 rounded-lg text-white">📅</div>
                <h3 className="text-white font-bold text-lg tracking-wide">Plan de Pagos (Crédito)</h3>
            </div>

            {/* TABLA ESTILO DOCUMENTO */}
            <div className="overflow-x-auto">
                <table className="w-full text-left border-collapse">
                    <thead>
                        <tr className="bg-purple-50 text-purple-900 border-b border-purple-100">
                            <th className="p-4 font-bold text-xs uppercase tracking-wider">Cuota</th>
                            <th className="p-4 font-bold text-xs uppercase tracking-wider">Fecha Pago</th>
                            <th className="p-4 font-bold text-xs uppercase tracking-wider">Valor Cuota</th>
                            <th className="p-4 font-bold text-xs uppercase tracking-wider text-slate-500">Interés</th>
                            <th className="p-4 font-bold text-xs uppercase tracking-wider text-slate-500">Saldo Cap.</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white">
                        {data.map((row, idx) => (
                            <tr key={idx} className="border-b border-slate-100 hover:bg-purple-50/30 transition-colors">
                                <td className="p-4 font-bold text-purple-700 text-sm">
                                    {row.numeroCuota}
                                </td>
                                <td className="p-4 text-slate-600 text-sm font-medium">
                                    {new Date(row.fechaPagoProgramada).toLocaleDateString()}
                                </td>
                                <td className="p-4 text-slate-900 font-bold text-sm">
                                    ${row.valorCuota}
                                </td>
                                <td className="p-4 text-slate-500 text-xs">
                                    ${row.interesPagado}
                                </td>
                                <td className="p-4 text-slate-400 text-xs font-mono">
                                    ${row.saldoCapital}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
            
            {/* Footer de la tabla */}
            <div className="bg-slate-50 p-3 text-center border-t border-slate-200">
                <p className="text-[10px] text-slate-400 uppercase tracking-widest font-bold">Documento Generado por BanQuito</p>
            </div>
        </div>
      )}
    </div>
  );
}