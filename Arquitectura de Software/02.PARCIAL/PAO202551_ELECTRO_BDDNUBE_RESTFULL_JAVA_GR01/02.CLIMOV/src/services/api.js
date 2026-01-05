import axios from 'axios';

// URL BASE DE TU SPRING BOOT
const API_URL = 'http://10.40.11.235:8080/api';

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    }
});

// --- MÉTODOS DEL SERVICIO (IGUAL QUE EN JAVA) ---

export const listarProductos = async () => {
    const response = await api.get('/productos');
    return response.data;
};

export const buscarCliente = async (cedula) => {
    try {
        const response = await api.get(`/clientes/${cedula}`);
        return response.data;
    } catch (error) {
        return null; // Si falla, asumimos que no existe
    }
};

export const registrarCliente = async (cliente) => {
    // Parche de fechas igual que en Java
    const clienteCorregido = {
        ...cliente,
        fechaNacimiento: cliente.fechaNacimiento || "1990-01-01",
        estadoCivil: cliente.estadoCivil || "S"
    };
    const response = await api.post('/clientes', clienteCorregido);
    return response.data;
};

export const procesarVenta = async (venta) => {
    // venta debe tener: { cedulaCliente, formaPago, numeroCuotas, items: [] }
    const response = await api.post('/facturacion/vender', venta);
    return response.data;
};

export const buscarFacturaPorId = async (id) => {
    try {
        const response = await api.get(`/facturacion/${id}`);
        return response.data;
    } catch (error) {
        return null;
    }
};

export const obtenerHistorial = async (cedula) => {
    const response = await api.get(`/facturacion/cliente/${cedula}`);
    return response.data;
};

export const consultarAmortizacion = async (idCredito) => {
    try {
        const response = await api.get(`/credito/amortizacion/${idCredito}`);
        // Si devuelve un objeto error, retornamos null
        if (response.data && response.data.error) return null;
        return response.data;
    } catch (error) {
        return null;
    }
};