import { useState, useEffect } from 'react';

export const useSocket = (cuentaActual, onRefresh) => {
  const [ventanillas, setVentanillas] = useState({});

  useEffect(() => {
    const socket = new WebSocket("ws://localhost:8080/WSEurekaBank_GRO01/eureka-notificaciones");
    
    socket.onmessage = (e) => {
      const [cta, estado] = e.data.split(":");
      setVentanillas(prev => ({ ...prev, [cta]: estado }));
      
      // Si la cuenta que estoy viendo se libera, refresco tabla
      if (estado === "LIBRE" && cta === cuentaActual) onRefresh(cta);
    };
    
    return () => socket.close();
  }, [cuentaActual, onRefresh]);

  return ventanillas;
};