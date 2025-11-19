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

    private final String URL_BASE = "http://localhost:8080/WS_Comercializadora_RestJava_GR01/api";
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    public List<Electrodomestico> listarCatalogo() throws Exception {
        String jsonRespuesta = httpGet(URL_BASE + "/catalogo");
        Type tipoLista = new TypeToken<ArrayList<Electrodomestico>>(){}.getType();
        return gson.fromJson(jsonRespuesta, tipoLista);
    }

    public RespuestaFacturacion procesarFactura(String jsonPeticion) throws Exception {
        String jsonRespuesta = httpPost(URL_BASE + "/facturacion", jsonPeticion);
        RespuestaFacturacion respuesta = gson.fromJson(jsonRespuesta, RespuestaFacturacion.class);
        if (respuesta == null) {
            throw new Exception("Respuesta nula del servidor. ¿Servidor está caído?");
        }
        return respuesta;
    }
    
    /**
     * Llama al Punto 11 (Consultar Amortización)
     */
    public List<AmortizacionDetalle> consultarAmortizacion(int idCredito) throws Exception {
        String jsonRespuesta = httpGet(URL_BASE + "/facturacion/consulta-credito/" + idCredito);
        
        // --- ARREGLO DE ERROR DE PARSEO ---
        if (jsonRespuesta.trim().startsWith("{")) {
            return new ArrayList<AmortizacionDetalle>(); // Devolvemos lista vacía si es un error
        }
        // --- FIN DEL ARREGLO ---
        
        Type tipoLista = new TypeToken<ArrayList<AmortizacionDetalle>>(){}.getType();
        return gson.fromJson(jsonRespuesta, tipoLista);
    }
    
    /**
     * Llama al Punto 9 (GET) para consultar una factura específica por ID.
     */
    public Factura getFactura(int idFactura) throws Exception {
        String urlString = URL_BASE + "/facturacion/" + idFactura;
        String jsonRespuesta = httpGet(urlString);

        // El servidor debe devolver el objeto Factura o un error.
        if (jsonRespuesta.contains("\"error\":")) {
            // Error en el servidor (ej: 404 No encontrado)
            throw new Exception("Error al consultar factura: " + jsonRespuesta);
        }

        // Deserializar directamente la Factura
        return gson.fromJson(jsonRespuesta, Factura.class);
    }
    
    public String registrarCliente(String jsonPeticion) throws Exception {
        String jsonRespuesta = httpPost(URL_BASE + "/catalogo/cliente", jsonPeticion);
        return jsonRespuesta;
    }
    
    public Cliente getCliente(String cedula) throws Exception {
        String jsonRespuesta = httpGet(URL_BASE + "/catalogo/cliente/" + cedula);
        if (jsonRespuesta.contains("\"error\":")) {
            return null;
        }
        return gson.fromJson(jsonRespuesta, Cliente.class);
    }
    
    public Electrodomestico getElectrodomestico(int id) throws Exception {
        String jsonRespuesta = httpGet(URL_BASE + "/catalogo/" + id);
        if (jsonRespuesta.contains("\"error\":")) {
            return null;
        }
        return gson.fromJson(jsonRespuesta, Electrodomestico.class);
    }

    // --- Métodos de Ayuda (Helpers) ---
    // (Estos no cambian)
    
    private String httpGet(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return leerRespuesta(con);
    }
    
    private String httpPost(String urlString, String jsonBody) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
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
            br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
        }
        
        StringBuilder respuesta = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            respuesta.append(linea.trim());
        }
        br.close();
        return respuesta.toString();
    }
    
    public Factura consultarFacturaPorId(int idFactura) throws Exception {
        String urlString = URL_BASE + "/facturacion/" + idFactura;
        String jsonRespuesta = httpGet(urlString);

        // 1. Manejo del caso en que el servidor devuelve un error HTTP (que se lee como JSON de error)
        if (jsonRespuesta.contains("\"error\":")) {
            // Asumimos que si contiene el texto "error", no es una factura válida.
            throw new Exception("El servidor devolvió un error de consulta. Verifique el ID. Respuesta: " + jsonRespuesta);
        }
        
        // 2. Manejo del error STRING (Expected BEGIN_OBJECT)
        if (jsonRespuesta.startsWith("<!DOCTYPE") || jsonRespuesta.trim().length() < 10) {
            // El servidor devolvió una página HTML de error (STRING), no el JSON esperado.
             throw new Exception("Error 404/500 en el servidor. La consulta no está devolviendo JSON.");
        }

        // 3. Deserializar Factura (Debe ser un objeto Factura si llega aquí)
        return gson.fromJson(jsonRespuesta, Factura.class);
    }
}