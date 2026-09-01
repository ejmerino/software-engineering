import React, { useState } from 'react';
import { Lock, User, ArrowRight, Activity } from 'lucide-react';

export default function Login({ onLogin }) {
  const [user, setUser] = useState('');
  const [pass, setPass] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault(); // ¡CRUCIAL! Evita la recarga de página
    setError('');
    setLoading(true);

    // Simulación de delay de red
    setTimeout(() => {
      if (user === 'MONSTER' && pass === 'MONSTER9') {
        onLogin();
      } else {
        setError('Credenciales incorrectas. Intente nuevamente.');
        setLoading(false);
      }
    }, 800);
  };

  return (
    <div className="min-h-screen flex items-center justify-center relative overflow-hidden bg-monster-bg">
      {/* Fondo Abstracto (Reemplaza las imágenes) */}
      <div className="absolute top-0 left-0 w-full h-full overflow-hidden z-0">
        <div className="absolute top-[-10%] left-[-10%] w-[500px] h-[500px] bg-monster-accent/20 rounded-full blur-[120px]"></div>
        <div className="absolute bottom-[-10%] right-[-10%] w-[600px] h-[600px] bg-monster-primary/20 rounded-full blur-[120px]"></div>
      </div>

      <div className="w-full max-w-md z-10 p-6 animate-slide-up">
        <div className="glass-panel p-10 rounded-3xl shadow-2xl">
          <div className="text-center mb-10">
            <div className="inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-tr from-monster-primary to-monster-accent mb-6 shadow-lg shadow-monster-primary/30">
              <Activity className="text-white w-8 h-8" />
            </div>
            <h1 className="text-4xl font-bold tracking-tight text-white mb-2">EUREKABANK</h1>
            <p className="text-monster-muted">Corporate Banking Portal</p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-2">
              <label className="text-sm font-medium text-monster-muted ml-1">Usuario Corporativo</label>
              <div className="relative group">
                <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                  <User className="h-5 w-5 text-slate-500 group-focus-within:text-monster-accent transition-colors" />
                </div>
                <input
                  type="text"
                  className="block w-full pl-11 pr-4 py-4 bg-slate-900/50 border border-slate-700 rounded-xl text-white placeholder-slate-500 focus:border-monster-accent focus:ring-1 focus:ring-monster-accent outline-none transition-all"
                  placeholder="ID de Empleado"
                  value={user}
                  onChange={(e) => setUser(e.target.value)}
                />
              </div>
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium text-monster-muted ml-1">Contraseña</label>
              <div className="relative group">
                <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
                  <Lock className="h-5 w-5 text-slate-500 group-focus-within:text-monster-accent transition-colors" />
                </div>
                <input
                  type="password"
                  className="block w-full pl-11 pr-4 py-4 bg-slate-900/50 border border-slate-700 rounded-xl text-white placeholder-slate-500 focus:border-monster-accent focus:ring-1 focus:ring-monster-accent outline-none transition-all"
                  placeholder="••••••••"
                  value={pass}
                  onChange={(e) => setPass(e.target.value)}
                />
              </div>
            </div>

            {error && (
              <div className="p-4 bg-red-500/10 border border-red-500/50 rounded-xl text-red-200 text-sm flex items-center gap-3 animate-pulse">
                <span className="w-2 h-2 bg-red-500 rounded-full"></span>
                {error}
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className="w-full py-4 bg-gradient-to-r from-monster-primary to-monster-accent hover:to-monster-primary text-white font-bold rounded-xl shadow-lg shadow-monster-primary/25 transition-all transform active:scale-[0.98] flex justify-center items-center gap-2"
            >
              {loading ? 'Autenticando...' : <>Acceder al Sistema <ArrowRight size={20} /></>}
            </button>
          </form>
        </div>
        <p className="text-center text-slate-600 text-xs mt-8">© 2026 Eurekabank. Monsters Inc. Subsidiary.</p>
      </div>
    </div>
  );
}