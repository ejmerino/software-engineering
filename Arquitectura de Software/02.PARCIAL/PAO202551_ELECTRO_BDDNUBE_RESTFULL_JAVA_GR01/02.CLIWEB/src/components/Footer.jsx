export default function Footer() {
  return (
    <footer className="bg-slate-900 text-blue-200 py-8 mt-auto border-t border-blue-600">
      <div className="container mx-auto px-4 flex flex-col md:flex-row justify-between items-center">
        
        {/* Lado Izquierdo */}
        <div className="mb-4 md:mb-0 text-center md:text-left">
          <h4 className="text-xl font-bold text-white mb-1">Comercializadora BanQuito</h4>
          <p className="text-sm opacity-70">
            Tu tienda de confianza, respaldada por la energía de los sustos (y créditos).
          </p>
        </div>

        {/* Lado Derecho */}
        <div className="text-center md:text-right">
          <p className="text-sm font-semibold">© 2025 Grupo BanQuito & Monster Inc.</p>
          <div className="flex gap-4 justify-center md:justify-end mt-2 text-xs">
            <a href="#" className="hover:text-green-400 transition">Términos</a>
            <a href="#" className="hover:text-green-400 transition">Privacidad</a>
            <a href="#" className="hover:text-green-400 transition">Soporte</a>
          </div>
        </div>
      </div>
    </footer>
  );
}