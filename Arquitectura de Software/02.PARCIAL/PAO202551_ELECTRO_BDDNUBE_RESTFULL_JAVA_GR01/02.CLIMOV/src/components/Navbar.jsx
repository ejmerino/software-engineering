import { Link, useLocation } from 'react-router-dom';

export default function Navbar() {
  const location = useLocation();

  const NavLink = ({ to, icon, label }) => {
    const isActive = location.pathname === to;
    return (
      <Link to={to} className="flex flex-col items-center justify-center w-full h-full relative group">
        {/* Indicador Activo Superior */}
        {isActive && <div className="absolute top-0 w-8 h-1 bg-indigo-500 rounded-b-full shadow-[0_0_10px_#6366f1]"></div>}
        
        <span className={`text-2xl mb-1 transition-all duration-300 ${isActive ? 'text-white scale-110 -translate-y-1' : 'text-slate-500 group-hover:text-slate-300'}`}>
          {icon}
        </span>
        <span className={`text-[10px] font-bold tracking-wide uppercase ${isActive ? 'text-indigo-400' : 'text-slate-600'}`}>
          {label}
        </span>
      </Link>
    );
  };

  return (
    <>
      {/* HEADER SUPERIOR */}
      <div className="fixed top-0 left-0 w-full h-16 bg-slate-900/95 backdrop-blur-md border-b border-slate-800 z-50 flex items-center justify-between px-5 shadow-lg">
         <div className="flex items-center gap-3">
             <div className="bg-indigo-600 p-1.5 rounded-lg shadow-lg shadow-indigo-500/20">
                 <img src="/images/monstersinc2.png" className="w-6 h-6 object-contain" />
             </div>
             <div>
                 <h1 className="text-white font-bold text-lg leading-none">BanQuito<span className="text-indigo-500">Go</span></h1>
                 <p className="text-[10px] text-slate-400 uppercase tracking-widest font-semibold">Agente Móvil</p>
             </div>
         </div>
         {/* Indicador de Estado */}
         <div className="w-2.5 h-2.5 rounded-full bg-emerald-500 shadow-[0_0_8px_#10b981] animate-pulse"></div>
      </div>

      {/* BARRA INFERIOR */}
      <nav className="fixed bottom-0 left-0 w-full h-20 bg-[#0f172a] border-t border-slate-800 z-50 flex justify-around pb-2 shadow-[0_-5px_20px_rgba(0,0,0,0.3)]">
        <NavLink to="/" icon="📦" label="Catálogo" />
        <NavLink to="/venta" icon="💳" label="Venta" />
        <NavLink to="/consultas" icon="📄" label="Historial" />
      </nav>
    </>
  );
}