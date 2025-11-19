package ec.edu.monster.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.model.AmortizacionDetalle;
import ec.edu.monster.model.Cliente;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.RespuestaFacturacion;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que hace las llamadas HTTP al backend de la Comercializadora.
 */
public class ComercializadoraService {

    // URL del servidor desplegado (puerto 8080)
    private final String URL_BASE = "http://localhost:8080/WS_Comercializadora_RestJava_GR01/api";
    
    // Configura Gson para que entienda fechas SQL (yyyy-MM-dd)
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    // --- MÉTODOS DE CONSULTA (GET) ---
    
    public List<Electrodomestico> listarCatalogo() throws Exception {
        String jsonRespuesta = httpGet(URL_BASE + "/catalogo");
        Type tipoLista = new TypeToken<ArrayList<Electrodomestico>>(){}.getType();
        return gson.fromJson(jsonRespuesta, tipoLista);
    }

    public Cliente getCliente(String cedula) throws Exception {
        String jsonRespuesta = httpGet(URL_BASE + "/catalogo/cliente/" + cedula);
        if (jsonRespuesta.contains("\"error\":")) {
            return null; // Cliente no encontrado
        }
        return gson.fromJson(jsonRespuesta, Cliente.class);
    }
    
    public Electrodomestico getElectrodomestico(int id) throws Exception {
        String jsonRespuesta = httpGet(URL_BASE + "/catalogo/" + id);
        if (jsonRespuesta.contains("\"error\":")) {
            return null; // Producto no encontrado
        }
        return gson.fromJson(jsonRespuesta, Electrodomestico.class);
    }

    public List<AmortizacionDetalle> consultarAmortizacion(int idCredito) throws Exception {
        String jsonRespuesta = httpGet(URL_BASE + "/facturacion/consulta-credito/" + idCredito);
        
        if (jsonRespuesta.trim().startsWith("{")) {
            // Es un objeto de error o vacío, no una lista.
            return new ArrayList<>(); 
        }
        
        Type tipoLista = new TypeToken<ArrayList<AmortizacionDetalle>>(){}.getType();
        return gson.fromJson(jsonRespuesta, tipoLista);
    }
    
    // --- MÉTODOS DE TRANSACCIÓN (POST) ---
    
    public RespuestaFacturacion procesarFactura(String jsonPeticion) throws Exception {
        String jsonRespuesta = httpPost(URL_BASE + "/facturacion", jsonPeticion);
        RespuestaFacturacion respuesta = gson.fromJson(jsonRespuesta, RespuestaFacturacion.class);
        if (respuesta == null) {
            throw new Exception("Respuesta nula del servidor. ¿Servidor está caído?");
        }
        return respuesta;
    }
    
    public String registrarCliente(String jsonPeticion) throws Exception {
        String jsonRespuesta = httpPost(URL_BASE + "/catalogo/cliente", jsonPeticion);
        return jsonRespuesta;
    }

    // --- MÉTODOS DE AYUDA (HTTP CORE) ---

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
            // Si el servidor devuelve un error HTTP (4xx o 5xx), leemos el mensaje de error
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
}