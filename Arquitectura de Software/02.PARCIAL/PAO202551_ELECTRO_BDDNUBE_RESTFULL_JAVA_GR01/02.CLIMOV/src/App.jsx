import { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Catalogo from './pages/Catalogo';
import Venta from './pages/Venta';
import Consultas from './pages/Consultas';
import Login from './pages/Login';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loggedIn = localStorage.getItem('isLoggedIn') === 'true';
    setIsAuthenticated(loggedIn);
    setLoading(false);
  }, []);

  const loginHandler = () => {
    localStorage.setItem('isLoggedIn', 'true');
    setIsAuthenticated(true);
  };

  const logoutHandler = () => {
    localStorage.removeItem('isLoggedIn');
    setIsAuthenticated(false);
  };

  if (loading) return <div className="min-h-screen bg-slate-900"></div>;

  return (
    <BrowserRouter>
      {/* CLASE PRINCIPAL: app-container (definida en CSS) fuerza el fondo oscuro */}
      <div className="app-container">
        {!isAuthenticated ? (
          <Login onLogin={loginHandler} />
        ) : (
          <>
            <Navbar onLogout={logoutHandler} />
            {/* Main con padding para no chocar con las barras fijas */}
            <main className="flex-grow pt-20 pb-24 px-4 overflow-y-auto">
              <Routes>
                <Route path="/" element={<Catalogo />} />
                <Route path="/venta" element={<Venta />} />
                <Route path="/consultas" element={<Consultas />} />
                <Route path="*" element={<Navigate to="/" />} />
              </Routes>
            </main>
          </>
        )}
      </div>
    </BrowserRouter>
  );
}

export default App;