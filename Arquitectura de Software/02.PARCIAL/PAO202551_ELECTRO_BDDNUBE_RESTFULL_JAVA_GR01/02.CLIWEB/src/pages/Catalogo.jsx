import { useEffect, useState } from 'react';
import { listarProductos } from '../services/api';

export default function Catalogo() {
  const [productos, setProductos] = useState([]);

  useEffect(() => {
    listarProductos().then(setProductos).catch(console.error);
  }, []);

  // Función auxiliar para cargar imagen, si falla pone una por defecto
  // Función auxiliar mejorada
const getImagenUrl = (ruta) => {
  if (!ruta) return '/images/monstersinc2.png'; // Fallback por defecto
  
  // LOGICA HIBRIDA:
  if (ruta.startsWith('http')) {
      return ruta; // Es URL de internet (Google Images, Amazon, etc.)
  } else {
      // Es imagen local antigua (limpiamos 'img/' por si acaso)
      const nombreLimpio = ruta.replace('img/', '');
      return `/images/${nombreLimpio}`;
  }
};

  return (
    <div className="animate-fadeIn pb-8">
      {/* HERO SECTION */}
      <div className="bg-gradient-to-r from-blue-800 to-slate-900 rounded-3xl p-8 md:p-12 mb-12 text-white shadow-2xl flex flex-col md:flex-row items-center justify-between relative overflow-hidden group">
        <div className="z-10 max-w-lg">
          <h1 className="text-4xl md:text-6xl font-extrabold mb-4 leading-tight">
            Bienvenido, <span className="text-transparent bg-clip-text bg-gradient-to-r from-green-400 to-teal-300">Asociado</span>
          </h1>
          <p className="text-lg text-blue-100 mb-8 leading-relaxed">
            Gestiona el inventario y realiza ventas en la plataforma oficial de Comercializadora BanQuito.
          </p>
        </div>
        
        {/* IMAGEN 3 GRANDE */}
        <div className="relative z-10 mt-6 md:mt-0 transition-transform duration-700 hover:scale-105">
            <img 
                src="/images/monstersinc3.png" 
                alt="Bienvenida" 
                className="w-64 md:w-[450px] drop-shadow-2xl filter brightness-110" 
            />
        </div>

        {/* Fondos animados */}
        <div className="absolute top-0 right-0 w-96 h-96 bg-purple-600 rounded-full mix-blend-multiply filter blur-[100px] opacity-20 animate-blob"></div>
        <div className="absolute bottom-0 left-0 w-96 h-96 bg-blue-600 rounded-full mix-blend-multiply filter blur-[100px] opacity-20 animate-blob animation-delay-2000"></div>
      </div>

      <h2 className="text-3xl font-bold mb-8 text-slate-800 border-l-8 border-blue-600 pl-4">Catálogo Disponible</h2>
      
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
        {productos.map((p) => (
          <div key={p.idElectrodomestico} className="bg-white rounded-2xl shadow-lg hover:shadow-2xl transition-all duration-300 group border border-slate-100 flex flex-col">
            
            {/* ZONA DE IMAGEN DEL PRODUCTO */}
            <div className="h-56 p-4 bg-slate-50 flex items-center justify-center relative overflow-hidden rounded-t-2xl">
                <img 
                  src={getImagenUrl(p.rutaImagen)} 
                  alt={p.nombre}
                  className="max-h-full max-w-full object-contain group-hover:scale-110 transition duration-500"
                  onError={(e) => { e.target.onerror = null; e.target.src = "/images/monstersinc2.png"; }} // Fallback si no existe la foto
                />
                <span className="absolute top-3 right-3 bg-blue-600 text-white text-xs font-bold px-3 py-1 rounded-full shadow">
                  Stock: {p.stock}
                </span>
            </div>
            
            <div className="p-6 flex-1 flex flex-col">
              <h3 className="font-bold text-xl text-slate-800 mb-2 leading-tight">{p.nombre}</h3>
              <p className="text-slate-500 text-sm mb-4 line-clamp-2 flex-1">{p.descripcion}</p>
              
              <div className="flex justify-between items-end border-t border-slate-100 pt-4 mt-auto">
                <div>
                  <span className="text-xs text-slate-400 font-bold uppercase tracking-wide">Precio</span>
                  <div className="text-2xl font-extrabold text-green-600">${p.precioVenta}</div>
                </div>
                <button className="bg-blue-50 text-blue-600 p-2.5 rounded-full hover:bg-blue-600 hover:text-white transition shadow-sm">
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                  </svg>
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}