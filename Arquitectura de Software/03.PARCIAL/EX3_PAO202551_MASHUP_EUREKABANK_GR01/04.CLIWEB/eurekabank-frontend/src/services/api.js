const BASE = "http://localhost:8080/WSEurekaBank_GRO01/webresources/coreBancario";

export const eurekaApi = {
  getMovimientos: async (cta) => {
    const r = await fetch(`${BASE}/movimientos/${cta}`);
    if (!r.ok) throw new Error("No se pudo obtener movimientos");
    return r.json();
  },
  
  // Unificamos para Depósito, Retiro y Transferencia
  operar: async (endpoint, params) => {
    const body = new URLSearchParams(params);
    const r = await fetch(`${BASE}/${endpoint}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body
    });
    
    // Si el servidor devuelve 409, es porque realmente está bloqueada en Java
    if (r.status === 409) throw new Error("Cuenta ocupada por otro cajero");
    if (!r.ok) throw new Error("Error en el servidor");
    
    return r.json();
  }
};