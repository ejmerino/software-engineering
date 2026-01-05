import { useEffect, useState } from 'react';
import { listarProductos } from '../services/api';

export default function Catalogo() {
  const [productos, setProductos] = useState([]);
  useEffect(() => { listarProductos().then(setProductos).catch(console.error); }, []);
 const getImg = (ruta) => {
    if (!ruta) return '/images/monstersinc2.png'; // Imagen por defecto si viene null
    if (ruta.startsWith('http')) return ruta;      // Si es URL de internet, úsala directa
    return `/images/${ruta.replace('img/', '')}`;  // Si es local, límpiala y busca en /images
};

  return (
    <div className="animate-fade">
      {/* BANNER PROMOCIONAL */}
      <div className="bg-gradient-to-r from-indigo-900 to-slate-900 rounded-2xl p-6 mb-6 relative overflow-hidden shadow-2xl border border-indigo-500/20">
        <div className="relative z-10">
            <h2 className="text-2xl font-bold text-white mb-1">Inventario</h2>
            <p className="text-indigo-200 text-sm opacity-80">Gestión de stock en tiempo real</p>
        </div>
        <div className="absolute right-0 top-0 h-full w-32 bg-indigo-500/10 transform skew-x-12 blur-xl"></div>
      </div>

      {/* GRID MEJORADO */}
      <div className="grid grid-cols-2 gap-4">
        {productos.map((p) => (
          <div key={p.idElectrodomestico} className="card-neumorphic flex flex-col overflow-hidden group">
            
            {/* CONTENEDOR IMAGEN (Con fondo suave para resaltar el producto) */}
            <div className="bg-slate-800/50 p-6 aspect-square flex items-center justify-center relative">
               <img 
                 src={getImg(p.rutaImagen)} 
                 className="w-full h-full object-contain drop-shadow-xl transition-transform duration-500 group-hover:scale-110"
                 onError={(e) => { e.target.onerror = null; e.target.src = "/images/monstersinc2.png"; }}
               />
               {/* Badge de Stock */}
               <div className="absolute top-2 right-2 bg-black/60 backdrop-blur text-[10px] font-bold px-2 py-1 rounded text-white border border-white/10">
                 x{p.stock}
               </div>
            </div>

            {/* INFO PRODUCTO */}
            <div className="p-4 flex flex-col flex-grow bg-[#1e293b]">
               <h3 className="text-white font-semibold text-sm leading-tight mb-3 line-clamp-2 min-h-[2.5em]">
                 {p.nombre}
               </h3>
               
               <div className="mt-auto flex items-center justify-between">
                  <span className="text-emerald-400 font-bold text-lg">
                    ${p.precioVenta}
                  </span>
                  <button className="w-8 h-8 rounded-full bg-indigo-600 flex items-center justify-center text-white shadow-lg shadow-indigo-500/30 active:scale-90 transition">
                    +
                  </button>
               </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}