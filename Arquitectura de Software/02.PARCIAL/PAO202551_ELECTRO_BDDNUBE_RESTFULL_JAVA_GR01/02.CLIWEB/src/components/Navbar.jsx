import { Link, useNavigate } from 'react-router-dom';

export default function Navbar({ onLogout }) {
  const navigate = useNavigate();

  const handleLogout = () => {
    onLogout();
    navigate('/');
  };

  return (
    <nav className="bg-slate-900 border-b border-blue-500 shadow-lg sticky top-0 z-50">
      <div className="container mx-auto px-4">
        <div className="flex justify-between items-center h-16">
          
          {/* LOGO Y MARCA */}
          <Link to="/" className="flex items-center gap-3 group">
            <img 
              src="/images/monstersinc2.png" 
              alt="Logo" 
              className="h-10 w-10 object-contain transition group-hover:rotate-12" 
            />
            <span className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-green-400">
              Comercializadora BanQuito
            </span>
          </Link>

          {/* MENU ESCRITORIO */}
          <div className="hidden md:flex space-x-8 items-center">
            <Link to="/" className="text-gray-300 hover:text-white hover:border-b-2 border-blue-400 transition pb-1 font-medium">Catálogo</Link>
            <Link to="/venta" className="text-gray-300 hover:text-white hover:border-b-2 border-green-400 transition pb-1 font-medium">Nueva Venta</Link>
            <Link to="/consultas" className="text-gray-300 hover:text-white hover:border-b-2 border-purple-400 transition pb-1 font-medium">Consultas</Link>
            
            {/* --- NUEVO ENLACE: INVENTARIO (ADMIN) --- */}
            <Link to="/admin" className="text-gray-300 hover:text-white hover:border-b-2 border-indigo-400 transition pb-1 font-medium">
                Inventario
            </Link>
            
            <button 
              onClick={handleLogout}
              className="bg-red-600 hover:bg-red-700 text-white px-5 py-1.5 rounded-full text-sm font-bold transition shadow-md hover:shadow-red-500/50"
            >
              Cerrar Sesión
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
}