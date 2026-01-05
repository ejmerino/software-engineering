import { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Footer from './components/Footer'; // <--- Importamos Footer
import Catalogo from './pages/Catalogo';
import Venta from './pages/Venta';
import Admin from './pages/Admin';
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

  if (loading) return null;

  return (
    <BrowserRouter>
      {!isAuthenticated ? (
        <Login onLogin={loginHandler} />
      ) : (
        <div className="min-h-screen bg-slate-50 text-slate-900 font-sans flex flex-col">
          <Navbar onLogout={logoutHandler} />
          
          <main className="container mx-auto mt-8 px-4 flex-grow">
            <Routes>
              <Route path="/" element={<Catalogo />} />
              <Route path="/admin" element={<Admin />} />
              <Route path="/venta" element={<Venta />} />
              <Route path="/consultas" element={<Consultas />} />
              <Route path="*" element={<Navigate to="/" />} />
            </Routes>
          </main>

          <Footer /> {/* <--- Footer aquí al final */}
        </div>
      )}
    </BrowserRouter>
  );
}

export default App;