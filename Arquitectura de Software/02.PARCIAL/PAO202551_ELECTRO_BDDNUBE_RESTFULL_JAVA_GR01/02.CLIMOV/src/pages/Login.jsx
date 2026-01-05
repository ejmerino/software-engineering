import { useState } from 'react';

export default function Login({ onLogin }) {
  const [user, setUser] = useState('');
  const [pass, setPass] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (user === 'MONSTER' && pass === 'MONSTER9') onLogin();
    else alert("Credenciales incorrectas");
  };

  return (
    <div className="h-screen w-full relative flex flex-col justify-end pb-12 px-6">
      
      {/* IMAGEN DE FONDO CON SUPERPOSICIÓN FUERTE */}
      <div className="absolute inset-0 z-0">
         <img src="/images/monstersinc3.png" className="w-full h-full object-cover grayscale opacity-40" />
         <div className="absolute inset-0 bg-gradient-to-t from-black via-black/90 to-black/40"></div>
      </div>

      <div className="relative z-10 w-full animate-fade">
        {/* LOGO Y TÍTULO */}
        <div className="mb-8">
           <div className="w-16 h-16 bg-purple-600 rounded-2xl flex items-center justify-center mb-4 shadow-[0_0_20px_rgba(147,51,234,0.5)]">
              <img src="/images/monstersinc2.png" className="w-10 h-10 object-contain" />
           </div>
           <h1 className="text-4xl font-bold text-white tracking-tight">Bienvenido</h1>
           <p className="text-zinc-400 mt-2 text-lg">Inicia sesión en BanQuito Móvil</p>
        </div>

        {/* FORMULARIO SÓLIDO */}
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <input 
              type="text" 
              value={user}
              onChange={(e) => setUser(e.target.value)}
              className="w-full bg-zinc-900 border border-zinc-700 text-white p-4 rounded-xl text-lg focus:border-purple-500 focus:ring-1 focus:ring-purple-500 outline-none placeholder-zinc-500 transition-colors"
              placeholder="Usuario"
            />
          </div>
          <div>
            <input 
              type="password" 
              value={pass}
              onChange={(e) => setPass(e.target.value)}
              className="w-full bg-zinc-900 border border-zinc-700 text-white p-4 rounded-xl text-lg focus:border-purple-500 focus:ring-1 focus:ring-purple-500 outline-none placeholder-zinc-500 transition-colors"
              placeholder="Contraseña"
            />
          </div>

          <button 
            type="submit" 
            className="w-full bg-white text-black font-bold py-4 rounded-xl text-lg mt-4 hover:bg-zinc-200 active:scale-95 transition-transform"
          >
            Ingresar
          </button>
        </form>
        
        <p className="text-center text-zinc-600 text-xs mt-8 uppercase tracking-widest">Secure System v3.0</p>
      </div>
    </div>
  );
}