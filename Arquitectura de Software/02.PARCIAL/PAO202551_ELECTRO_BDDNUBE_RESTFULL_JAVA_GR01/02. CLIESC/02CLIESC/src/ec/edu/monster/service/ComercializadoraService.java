package ec.edu.monster.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.model.AmortizacionDetalle;
import ec.edu.monster.model.Cliente;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.PeticionFactura;
import ec.edu.monster.model.RespuestaFacturacion;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ComercializadoraService {

    private final String BASE_URL = "http://localhost:8080/api";
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    // 1. GESTIÓN DE PRODUCTOS
    public List<Electrodomestico> listarProductos() throws Exception {
        String url = BASE_URL + "/productos";
        String respuestaJson = realizarPeticionGet(url);
        Type tipoLista = new TypeToken<ArrayList<Electrodomestico>>(){}.getType();
        return gson.fromJson(respuestaJson, tipoLista);
    }

    // 2. GESTIÓN DE CLIENTES
    public Cliente buscarCliente(String cedula) throws Exception {
        String url = BASE_URL + "/clientes/" + cedula;
        try {
            String respuestaJson = realizarPeticionGet(url);
            return gson.fromJson(respuestaJson, Cliente.class);
        } catch (Exception e) {
            if (e.getMessage().contains("404")) return null;
            throw e;
        }
    }

    public Cliente crearCliente(Cliente cliente) throws Exception {
        String url = BASE_URL + "/clientes";
        String jsonBody = gson.toJson(cliente);
        
        // PARCHE DE FECHAS (Para que el servidor no rechace el registro)
        if (cliente.getFechaNacimiento() == null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(1990, java.util.Calendar.JANUARY, 1);
            cliente.setFechaNacimiento(cal.getTime());
        }
        if (cliente.getEstadoCivil() == null || cliente.getEstadoCivil().isEmpty()) {
            cliente.setEstadoCivil("S");
        }

        String respuestaJson = realizarPeticionPost(url, jsonBody);
        return gson.fromJson(respuestaJson, Cliente.class);
    }

    // 3. VENTAS
    public RespuestaFacturacion procesarVenta(PeticionFactura peticion) throws Exception {
        String url = BASE_URL + "/facturacion/vender";
        String jsonBody = gson.toJson(peticion);
        String respuestaJson = realizarPeticionPost(url, jsonBody);
        return gson.fromJson(respuestaJson, RespuestaFacturacion.class);
    }

    // 4. HISTORIAL Y AMORTIZACIÓN
    
    // --- ESTE ES EL QUE FALTABA Y DABA ERROR ---
    public List<Factura> obtenerHistorialCompras(String cedula) throws Exception {
        String url = BASE_URL + "/facturacion/cliente/" + cedula;
        String respuestaJson = realizarPeticionGet(url);
        Type tipoLista = new TypeToken<ArrayList<Factura>>(){}.getType();
        return gson.fromJson(respuestaJson, tipoLista);
    }

    public List<AmortizacionDetalle> consultarAmortizacion(int idCredito) throws Exception {
        String url = BASE_URL + "/credito/amortizacion/" + idCredito;
        String respuestaJson = realizarPeticionGet(url);
        Type tipoLista = new TypeToken<ArrayList<AmortizacionDetalle>>(){}.getType();
        return gson.fromJson(respuestaJson, tipoLista);
    }
    
    // --- BUSCAR FACTURA ESPECÍFICA POR ID ---
    public Factura buscarFacturaPorId(int idFactura) throws Exception {
        String url = BASE_URL + "/facturacion/" + idFactura;
        try {
            String respuestaJson = realizarPeticionGet(url);
            // Si el servidor devuelve vacío o null, manejamos el error
            if (respuestaJson == null || respuestaJson.isEmpty()) return null;
            
            return gson.fromJson(respuestaJson, Factura.class);
        } catch (Exception e) {
            if (e.getMessage().contains("404")) return null; // No existe
            throw e;
        }
    }

    // 5. MÉTODOS DE COMPATIBILIDAD
    public List<Electrodomestico> listarCatalogo() throws Exception {
        return listarProductos();
    }

    public Cliente getCliente(String cedula) throws Exception {
        return buscarCliente(cedula);
    }

    public String registrarCliente(String jsonCliente) throws Exception {
        Cliente clienteObj = gson.fromJson(jsonCliente, Cliente.class);
        Cliente creado = crearCliente(clienteObj);
        return gson.toJson(creado);
    }

    public RespuestaFacturacion procesarFactura(String jsonPeticion) throws Exception {
        PeticionFactura peticionObj = gson.fromJson(jsonPeticion, PeticionFactura.class);
        return procesarVenta(peticionObj);
    }

    // 6. HELPERS HTTP
    private String realizarPeticionGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        return leerRespuesta(con);
    }

    private String realizarPeticionPost(String urlStr, String jsonBody) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return leerRespuesta(con);
    }

    private String leerRespuesta(HttpURLConnection con) throws Exception {
        int status = con.getResponseCode();
        BufferedReader br;
        if (status >= 200 && status < 300) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        } else {
            if (con.getErrorStream() != null) {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));
            } else {
                throw new Exception("Error HTTP: " + status); 
            }
        }
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) response.append(line.trim());
        br.close();
        if (status >= 400) {
            throw new Exception("Error del Servidor (" + status + "): " + response.toString());
        }
        return response.toString();
    }
}