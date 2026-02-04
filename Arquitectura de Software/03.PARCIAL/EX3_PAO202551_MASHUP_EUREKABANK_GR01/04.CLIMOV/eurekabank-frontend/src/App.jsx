import React, { useState, useEffect, useMemo } from 'react';

const APP_CONTEXT = "WSEurekaBank_GRO01";
const BASE_URL = `http://localhost:8080/${APP_CONTEXT}/webresources/coreBancario`;

export default function App() {
  const [isAuth, setIsAuth] = useState(false);
  const [user, setUser] = useState('');
  const [pass, setPass] = useState('');
  const [ctaActual, setCtaActual] = useState('');
  const [movimientos, setMovimientos] = useState([]);
  const [filtro, setFiltro] = useState(''); 
  const [statusMsg, setStatusMsg] = useState({ text: '', type: 'none' });
  
  const [ventanillas, setVentanillas] = useState({
    '00100001': 'LIBRE', '00100002': 'LIBRE', '00200001': 'LIBRE', '00200002': 'LIBRE', '00200003': 'LIBRE', '00300001': 'LIBRE'
  });

  useEffect(() => {
    if (!isAuth) return;
    const socket = new WebSocket(`ws://localhost:8080/${APP_CONTEXT}/eureka-notificaciones`);
    socket.onmessage = (e) => {
      const [cta, estado] = e.data.split(":");
      setVentanillas(prev => ({ ...prev, [cta]: estado }));
      if (estado === "LIBRE" && cta === ctaActual) fetchMovs(cta);
    };
    return () => socket.close();
  }, [isAuth, ctaActual]);

  const fetchMovs = async (cta) => {
    if (cta.length < 8) return;
    try {
      const r = await fetch(`${BASE_URL}/movimientos/${cta}`);
      const data = await r.json();
      let saldoAcumulado = 0;
      const procesados = data
        .sort((a, b) => a.nromov - b.nromov)
        .map(m => {
          m.accion === "INGRESO" ? saldoAcumulado += m.importe : saldoAcumulado -= m.importe;
          return { ...m, _saldo: saldoAcumulado, fCorta: m.fecha.split(' ')[0] };
        });
      setMovimientos(procesados.reverse());
    } catch (e) { setMovimientos([]); }
  };

  const ejecutar = async (endpoint, params) => {
    const cuentasAValidar = [params.cuenta, params.origen, params.destino].filter(Boolean);
    if (cuentasAValidar.some(c => ventanillas[c] === 'BLOQUEADO')) {
        setStatusMsg({ text: 'OPERACIÓN DENEGADA: CUENTA EN USO', type: 'error' });
        return;
    }
    setStatusMsg({ text: 'PROCESANDO TRANSACCIÓN...', type: 'loading' });
    try {
      const body = new URLSearchParams(params);
      const r = await fetch(`${BASE_URL}/${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body
      });
      const res = await r.json();
      if (res.estado === 1) {
          setStatusMsg({ text: res.mensaje.toUpperCase(), type: 'success' });
          if (ctaActual) fetchMovs(ctaActual);
      } else {
          setStatusMsg({ text: `ERROR: ${res.mensaje.toUpperCase()}`, type: 'error' });
      }
    } catch (e) {
      setStatusMsg({ text: 'ERROR CRÍTICO: SIN CONEXIÓN', type: 'error' });
    }
  };

  const movsFiltrados = useMemo(() => 
    movimientos.filter(m => m.tipo.toLowerCase().includes(filtro.toLowerCase())),
    [movimientos, filtro]
  );

  if (!isAuth) return (
    <div className="flex h-screen w-full items-center justify-center bg-[#020617] relative overflow-hidden">
      <div className="absolute top-[-10%] left-[-10%] w-[500px] h-[500px] bg-blue-600/10 rounded-full blur-[120px]"></div>
      <form onSubmit={(e) => { e.preventDefault(); if(user==='MONSTER' && pass==='MONSTER9') setIsAuth(true); else alert("ACCESO DENEGADO"); }} 
            className="relative z-10 w-full max-w-md bg-white/[0.03] backdrop-blur-3xl p-12 rounded-[3rem] border border-white/10 shadow-[0_32px_64px_-12px_rgba(0,0,0,0.5)] mx-4">
        <div className="text-center mb-10">
            <h1 className="text-4xl font-black text-white tracking-tighter italic">EUREKA<span className="text-[#99ff33]">BANK</span></h1>
            <div className="h-1 w-12 bg-[#99ff33] mx-auto mt-2 rounded-full"></div>
        </div>
        <div className="space-y-6">
            <input className="w-full rounded-2xl bg-white/5 border border-white/10 p-4 outline-none focus:border-[#99ff33] focus:bg-white/[0.07] text-white font-bold transition-all placeholder:text-slate-600" placeholder="ID OPERADOR" onChange={e => setUser(e.target.value)} />
            <input className="w-full rounded-2xl bg-white/5 border border-white/10 p-4 outline-none focus:border-[#99ff33] focus:bg-white/[0.07] text-white font-bold transition-all placeholder:text-slate-600" type="password" placeholder="CONTRASEÑA" onChange={e => setPass(e.target.value)} />
            <button className="w-full rounded-2xl bg-[#99ff33] p-5 font-black text-slate-950 text-sm shadow-[0_20px_40px_-10px_rgba(153,255,51,0.3)] hover:scale-[1.02] active:scale-[0.98] transition-all uppercase tracking-widest mt-4">Entrar al Sistema</button>
        </div>
      </form>
    </div>
  );

  return (
    <div className="min-h-screen bg-[#020617] font-sans text-slate-200 flex flex-col relative">
      {/* HEADER REFINADO (SIN MORADO) */}
      <header className="sticky top-0 z-50 backdrop-blur-xl bg-[#020617]/80 border-b border-white/5 px-6 py-4">
        <div className="max-w-[1600px] mx-auto flex justify-between items-center">
          <div className="flex items-center gap-4">
              <div className="w-10 h-10 bg-[#99ff33] rounded-xl flex items-center justify-center font-black text-[#020617] text-xl shadow-[0_0_20px_rgba(153,255,51,0.4)]">E</div>
              <div>
                <h1 className="text-xl font-black italic tracking-tighter text-white leading-none">EUREKABANK</h1>
                <span className="text-[#99ff33] text-[10px] font-black tracking-[0.3em] uppercase opacity-70">Corporate Ops</span>
              </div>
          </div>
          <button onClick={() => window.location.reload()} className="group flex items-center gap-2 bg-red-500/10 hover:bg-red-500 text-red-500 hover:text-white border border-red-500/20 px-6 py-2.5 rounded-xl font-black text-[10px] transition-all uppercase tracking-widest">
            <span>Finalizar Turno</span>
            <span className="group-hover:translate-x-1 transition-transform">→</span>
          </button>
        </div>
      </header>

      <main className="max-w-[1600px] mx-auto w-full px-6 py-8 flex-grow">
        {/* VENTANILLAS CON MEJOR LEGIBILIDAD */}
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-5 mb-10">
          {Object.entries(ventanillas).map(([cta, est]) => (
            <div key={cta} className={`p-6 rounded-[2rem] border transition-all duration-500 ${est === 'BLOQUEADO' ? 'bg-red-500/5 border-red-500/40' : 'bg-white/[0.03] border-white/10'}`}>
              <div className="flex justify-between items-center mb-3">
                <span className="text-[10px] font-black text-slate-500 uppercase tracking-widest text-white/40">Cuenta</span>
                <div className={`h-2.5 w-2.5 rounded-full ${est === 'BLOQUEADO' ? 'bg-red-500 animate-pulse shadow-[0_0_12px_#ef4444]' : 'bg-[#99ff33] shadow-[0_0_12px_#99ff33]'}`}></div>
              </div>
              <p className="text-2xl font-mono font-black text-white tracking-tight">{cta}</p>
              <p className={`text-[10px] font-black mt-2 uppercase tracking-widest ${est === 'BLOQUEADO' ? 'text-red-400' : 'text-[#99ff33]'}`}>
                {est === 'BLOQUEADO' ? 'En Operación' : 'Disponible'}
              </p>
            </div>
          ))}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
          {/* ACCIONES DE CAJA */}
          <div className="lg:col-span-4 space-y-6">
            <section className="bg-white/[0.03] border border-white/10 p-8 rounded-[3rem] shadow-2xl">
              <h3 className="text-[11px] font-black mb-8 uppercase tracking-[0.4em] text-white text-center">Operaciones de Caja</h3>
              
              {statusMsg.type !== 'none' && (
                <div className={`mb-8 p-5 rounded-2xl text-center font-black text-[10px] border tracking-widest animate-in fade-in slide-in-from-top-2
                  ${statusMsg.type === 'success' ? 'bg-emerald-500/10 border-emerald-500/50 text-emerald-400' : 
                    statusMsg.type === 'error' ? 'bg-red-500/10 border-red-500/50 text-red-400' : 
                    'bg-blue-500/10 border-blue-500/50 text-blue-400 animate-pulse'}`}>
                  {statusMsg.text}
                </div>
              )}

              <div className="space-y-5">
                <div className="space-y-1 bg-white/5 p-4 rounded-2xl border border-white/5 focus-within:border-[#99ff33]/50 transition-all">
                   <label className="text-[9px] font-black text-white/30 uppercase tracking-widest ml-1">Cuenta Destino</label>
                   <input id="inCta" className="w-full bg-transparent outline-none font-bold text-xl text-white placeholder:text-slate-700" placeholder="00000000" />
                </div>
                <div className="space-y-1 bg-white/5 p-4 rounded-2xl border border-white/5 focus-within:border-[#99ff33]/50 transition-all">
                   <label className="text-[9px] font-black text-white/30 uppercase tracking-widest ml-1">Monto de Operación (USD)</label>
                   <input id="inImp" type="number" className="w-full bg-transparent outline-none font-black text-4xl text-[#99ff33] placeholder:text-[#99ff33]/10" placeholder="0.00" />
                </div>
                
                <div className="grid grid-cols-2 gap-4 pt-2">
                    <button onClick={() => ejecutar('deposito', {cuenta: document.getElementById('inCta').value, importe: document.getElementById('inImp').value})} className="bg-[#99ff33] text-slate-950 py-5 rounded-2xl font-black text-xs hover:brightness-110 active:scale-95 transition-all shadow-[0_10px_20px_-5px_rgba(153,255,51,0.2)] uppercase">Ingresar</button>
                    <button onClick={() => ejecutar('retiro', {cuenta: document.getElementById('inCta').value, importe: document.getElementById('inImp').value})} className="bg-slate-800 text-white py-5 rounded-2xl font-black text-xs hover:bg-slate-700 active:scale-95 transition-all border border-white/10 uppercase">Retirar</button>
                </div>
              </div>

              <div className="mt-12 pt-10 border-t border-white/5">
                <h3 className="text-[10px] font-black mb-6 uppercase text-center text-white/30 tracking-[0.3em]">Transferencia Interbancaria</h3>
                <div className="space-y-4">
                  <div className="grid grid-cols-2 gap-3">
                      <input id="inOrg" className="bg-white/5 p-4 rounded-xl border border-white/5 text-sm font-bold text-white outline-none focus:border-blue-500" placeholder="Origen" />
                      <input id="inDst" className="bg-white/5 p-4 rounded-xl border border-white/5 text-sm font-bold text-white outline-none focus:border-blue-500" placeholder="Destino" />
                  </div>
                  <input id="inTrImp" type="number" className="w-full bg-blue-600/10 p-4 rounded-xl border border-blue-600/20 font-black text-blue-400 outline-none text-center text-xl" placeholder="$ 0.00" />
                  <button onClick={() => ejecutar('transferencia', {origen: document.getElementById('inOrg').value, destino: document.getElementById('inDst').value, importe: document.getElementById('inTrImp').value})} className="w-full bg-blue-600 hover:bg-blue-500 text-white py-5 rounded-2xl font-black text-xs shadow-lg shadow-blue-600/20 transition-all active:scale-95 uppercase tracking-widest">Ejecutar Envío</button>
                </div>
              </div>
            </section>
          </div>

          {/* HISTORIAL DE MOVIMIENTOS REFINADO */}
          <div className="lg:col-span-8 bg-white/[0.02] backdrop-blur-md rounded-[3rem] shadow-2xl flex flex-col overflow-hidden border border-white/10">
            <div className="p-10 border-b border-white/5">
              <div className="flex flex-col md:flex-row gap-5 mb-8">
                  <div className="relative flex-grow group">
                      <input onChange={e => setCtaActual(e.target.value)} className="w-full bg-white/5 p-6 rounded-2xl border border-white/10 shadow-inner font-mono text-2xl font-black focus:border-[#99ff33] outline-none text-white transition-all pl-16" placeholder="CUENTA A CONSULTAR" maxLength={8} />
                      <span className="absolute left-6 top-6 text-2xl opacity-40 group-focus-within:opacity-100 transition-opacity">🔍</span>
                  </div>
                  <button onClick={() => fetchMovs(ctaActual)} className="bg-white text-[#020617] px-12 py-6 rounded-2xl font-black text-sm uppercase hover:bg-[#99ff33] transition-all shadow-xl active:scale-95">Buscar</button>
              </div>
              <input onChange={e => setFiltro(e.target.value)} className="w-full p-4 pl-6 bg-black/40 rounded-xl font-bold text-[10px] text-white/50 border border-white/5 focus:border-white/20 outline-none transition-all uppercase tracking-widest" placeholder="Filtrar por tipo (ej: Depósito)..." />
            </div>
            
            <div className="overflow-x-auto">
              <table className="w-full text-left">
                <thead className="bg-white/[0.02]">
                  <tr>
                    <th className="px-10 py-6 text-xs font-black uppercase text-white tracking-[0.2em] opacity-40">Fecha</th>
                    <th className="px-10 py-6 text-xs font-black uppercase text-white tracking-[0.2em] opacity-40">Descripción del Movimiento</th>
                    <th className="px-10 py-6 text-xs font-black uppercase text-white tracking-[0.2em] opacity-40 text-right">Importe</th>
                    <th className="px-10 py-6 text-xs font-black uppercase text-white tracking-[0.2em] opacity-40 text-right">Saldo Final</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-white/5">
                  {movsFiltrados.map((m, i) => {
                    const esSalida = m.accion === "SALIDA"; 
                    return (
                      <tr key={i} className="hover:bg-white/[0.03] transition-colors group">
                        <td className="px-10 py-7 font-mono text-[13px] text-white/40">{m.fCorta}</td>
                        <td className="px-10 py-7">
                          <div className="flex flex-col">
                              <span className="text-white text-sm font-black uppercase tracking-tight group-hover:text-[#99ff33] transition-colors">{m.tipo}</span>
                              <span className="text-[10px] text-white/20 font-bold uppercase tracking-[0.2em] mt-1">ID Transacción: #{m.nromov}</span>
                          </div>
                        </td>
                        <td className={`px-10 py-7 text-right text-xl font-black ${esSalida ? 'text-rose-500' : 'text-[#99ff33]'}`}>
                          {esSalida ? '-' : '+'} ${m.importe.toLocaleString('en-US', { minimumFractionDigits: 2 })}
                        </td>
                        <td className="px-10 py-7 text-right font-mono text-white/60 text-sm font-bold">
                          ${m._saldo.toLocaleString('en-US', { minimumFractionDigits: 2 })}
                        </td>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
              {movsFiltrados.length === 0 && (
                <div className="p-40 text-center">
                  <p className="font-black text-white/10 text-4xl uppercase tracking-tighter italic">No hay registros</p>
                </div>
              )}
            </div>
          </div>
        </div>
      </main>

      {/* FOOTER REFINADO */}
      <footer className="bg-black/60 backdrop-blur-xl py-12 px-10 border-t border-white/10">
        <div className="max-w-[1600px] mx-auto flex flex-col md:flex-row justify-between items-center gap-10">
          
          <div className="text-center md:text-left">
            <h2 className="text-white font-black italic tracking-tighter text-3xl">
              EUREKA<span className="text-[#99ff33]">BANK</span>
            </h2>
            <p className="text-[10px] font-black uppercase tracking-[0.4em] text-white/20 mt-2">
              Management System Terminal v4.5.2
            </p>
          </div>

          <div className="flex flex-wrap justify-center gap-16">
             <div className="text-center">
                <p className="text-[10px] font-black text-white/20 uppercase tracking-widest mb-2">Estado Core</p>
                <div className="flex items-center justify-center gap-2">
                    <span className="w-2 h-2 bg-[#99ff33] rounded-full animate-pulse shadow-[0_0_8px_#99ff33]"></span>
                    <p className="text-white text-[11px] font-black tracking-widest uppercase">Operacional</p>
                </div>
             </div>
             <div className="text-center">
                <p className="text-[10px] font-black text-white/20 uppercase tracking-widest mb-2">Protocolo</p>
                <p className="text-white text-[11px] font-black tracking-widest uppercase">WSS Active</p>
             </div>
          </div>

          <div className="text-center md:text-right">
            <p className="text-[#99ff33] text-[11px] font-black uppercase tracking-[0.2em] mb-2">
              © {new Date().getFullYear()} EUREKA BANK S.A.
            </p>
            <div className="text-[12px] font-bold text-white/40 italic">
              <span className="opacity-30 text-[10px] font-black not-italic mr-2 uppercase tracking-widest">Devs:</span>
              M. Almeida • J. Merino • D. Paez
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}