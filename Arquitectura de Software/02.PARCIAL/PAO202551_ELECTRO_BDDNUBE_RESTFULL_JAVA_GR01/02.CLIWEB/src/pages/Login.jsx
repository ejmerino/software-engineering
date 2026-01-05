import { useState } from 'react';

export default function Login({ onLogin }) {
  const [user, setUser] = useState('');
  const [pass, setPass] = useState('');
  const [error, setError] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (user === 'MONSTER' && pass === 'MONSTER9') {
      onLogin();
    } else {
      setError(true);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-blue-900 to-purple-900 p-4">
      <div className="bg-white/95 backdrop-blur rounded-2xl shadow-2xl p-8 max-w-md w-full transform transition hover:scale-[1.01] duration-500 border border-white/20">
        
        {/* IMAGEN 1: LOGO LOGIN */}
        <div className="flex justify-center -mt-20 mb-6">
          <img 
            src="/images/monstersinc1.png" 
            alt="Login Monster" 
            className="w-36 h-36 object-contain bg-white rounded-full p-2 shadow-xl border-4 border-blue-500" 
          />
        </div>

        <h2 className="text-3xl font-extrabold text-center text-slate-800 mb-2">BanQuito Store</h2>
        <p className="text-center text-slate-500 mb-8 text-sm">Acceso exclusivo para asociados</p>
        
        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-bold text-slate-700 mb-1 ml-1">Usuario</label>
            <input 
              type="text" 
              value={user}
              onChange={(e) => { setUser(e.target.value); setError(false); }}
              className="w-full px-4 py-3 bg-slate-50 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition font-medium"
              placeholder="Ej: MONSTER"
            />
          </div>

          <div>
            <label className="block text-sm font-bold text-slate-700 mb-1 ml-1">Contraseña</label>
            <input 
              type="password" 
              value={pass}
              onChange={(e) => { setPass(e.target.value); setError(false); }}
              className="w-full px-4 py-3 bg-slate-50 border border-slate-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-purple-500 outline-none transition font-medium"
              placeholder="••••••••"
            />
          </div>

          {error && (
            <div className="bg-red-100 border-l-4 border-red-500 text-red-700 p-3 rounded text-sm font-semibold animate-pulse">
              ⛔ Acceso denegado. Intente MONSTER / MONSTER9
            </div>
          )}

          <button 
            type="submit" 
            className="w-full bg-gradient-to-r from-blue-600 to-purple-600 text-white font-bold py-3.5 rounded-lg hover:from-blue-700 hover:to-purple-700 shadow-lg transform active:scale-95 transition-all duration-200"
          >
            INGRESAR AL SISTEMA
          </button>
        </form>
      </div>
    </div>
  );
}