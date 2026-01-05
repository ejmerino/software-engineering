package ec.edu.monster.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.model.AmortizacionDetalle;
import ec.edu.monster.model.Cliente;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.RespuestaFacturacion;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ComercializadoraService {

    // CAMBIO: Apunta al nuevo servidor Spring Boot
    private final String URL_BASE = "http://localhost:8080/api";
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    public List<Electrodomestico> listarCatalogo() throws Exception {
        String jsonRespuesta = httpGet(URL_BASE + "/productos"); // Endpoint correcto
        Type tipoLista = new TypeToken<ArrayList<Electrodomestico>>(){}.getType();
        return gson.fromJson(jsonRespuesta, tipoLista);
    }

    public RespuestaFacturacion procesarFactura(String jsonPeticion) throws Exception {
        String jsonRespuesta = httpPost(URL_BASE + "/facturacion/vender", jsonPeticion);
        RespuestaFacturacion respuesta = gson.fromJson(jsonRespuesta, RespuestaFacturacion.class);
        if (respuesta == null) {
            throw new Exception("Respuesta nula del servidor.");
        }
        return respuesta;
    }
    
    public List<AmortizacionDetalle> consultarAmortizacion(int idCredito) throws Exception {
        // Endpoint correcto para Spring Boot
        String jsonRespuesta = httpGet(URL_BASE + "/credito/amortizacion/" + idCredito);
        
        if (jsonRespuesta.trim().startsWith("{") && !jsonRespuesta.contains("[")) {
            return new ArrayList<>(); 
        }
        
        Type tipoLista = new TypeToken<ArrayList<AmortizacionDetalle>>(){}.getType();
        return gson.fromJson(jsonRespuesta, tipoLista);
    }
    
    // --- NUEVO MÉTODO: BUSCAR FACTURA POR ID ---
    public Factura buscarFacturaPorId(int idFactura) throws Exception {
        String url = URL_BASE + "/facturacion/" + idFactura;
        try {
            String jsonRespuesta = httpGet(url);
            if (jsonRespuesta == null || jsonRespuesta.isEmpty()) return null;
            return gson.fromJson(jsonRespuesta, Factura.class);
        } catch (Exception e) {
            if (e.getMessage().contains("404") || e.getMessage().contains("500")) {
                return null; // No encontrada
            }
            throw e;
        }
    }
    
    public String registrarCliente(String jsonPeticion) throws Exception {
        // Convertimos el JSON a Objeto para aplicarle el PARCHE DE FECHAS
        Cliente cliente = gson.fromJson(jsonPeticion, Cliente.class);
        
        // Parche: Asignar fecha por defecto si no viene (Consola no pide fecha)
        if (cliente.getFechaNacimiento() == null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(1990, java.util.Calendar.JANUARY, 1);
            cliente.setFechaNacimiento(cal.getTime());
        }
        if (cliente.getEstadoCivil() == null || cliente.getEstadoCivil().isEmpty()) {
            cliente.setEstadoCivil("S");
        }
        
        String bodyCorregido = gson.toJson(cliente);
        
        // Endpoint correcto
        String jsonRespuesta = httpPost(URL_BASE + "/clientes", bodyCorregido);
        return jsonRespuesta;
    }
    
    public Cliente getCliente(String cedula) throws Exception {
        // Endpoint correcto
        try {
            String jsonRespuesta = httpGet(URL_BASE + "/clientes/" + cedula);
            if (jsonRespuesta == null || jsonRespuesta.isEmpty()) return null;
            return gson.fromJson(jsonRespuesta, Cliente.class);
        } catch (Exception e) {
            return null; // Si da 404 o 500 asumimos que no existe
        }
    }
    
    public Electrodomestico getElectrodomestico(int id) throws Exception {
        try {
            String jsonRespuesta = httpGet(URL_BASE + "/productos/" + id);
            return gson.fromJson(jsonRespuesta, Electrodomestico.class);
        } catch (Exception e) {
            return null;
        }
    }

    // --- Helpers HTTP (Sin cambios) ---
    private String httpGet(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json"); // Importante
        return leerRespuesta(con);
    }
    
    private String httpPost(String urlString, String jsonBody) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // JSON
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);            
        }
        return leerRespuesta(con);
    }
    
    private String leerRespuesta(HttpURLConnection con) throws Exception {
        int status = con.getResponseCode();
        BufferedReader br;
        
        if (status >= 200 && status < 300) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
        } else {
            if(con.getErrorStream() != null)
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
            else 
                throw new Exception("Error HTTP: " + status);
        }
        
        StringBuilder respuesta = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            respuesta.append(linea.trim());
        }
        br.close();
        
        if (status >= 400) {
            throw new Exception("Error del Servidor (" + status + "): " + respuesta.toString());
        }
        return respuesta.toString();
    }
}