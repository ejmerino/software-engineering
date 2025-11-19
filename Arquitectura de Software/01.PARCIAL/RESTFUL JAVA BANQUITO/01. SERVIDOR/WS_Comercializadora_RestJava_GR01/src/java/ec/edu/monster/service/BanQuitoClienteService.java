package ec.edu.monster.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.model.AmortizacionDetalle;
import ec.edu.monster.model.Cliente; // Importación que faltaba para el getter
import ec.edu.monster.model.Electrodomestico; // Importación que faltaba para el getter
import ec.edu.monster.model.dto.RespuestaCredito;
import ec.edu.monster.model.dto.RespuestaMonto;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale; // <-- 1. IMPORTA LOCALE

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BanQuitoClienteService {

    private final String URL_BASE_BANQUITO = "http://localhost:8080/WS_BanQuito_RestJava_GR01/api/credito";
    
    // Configura Gson para que entienda fechas SQL (yyyy-MM-dd)
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    /**
     * Llama al servicio de Monto Máximo (Punto 6)
     */
    public RespuestaMonto consultarMontoMaximo(String cedula) throws Exception {
        URL url = new URL(URL_BASE_BANQUITO + "/monto-maximo/" + cedula);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                respuesta.append(linea.trim());
            }
            return gson.fromJson(respuesta.toString(), RespuestaMonto.class);
        }
    }

    /**
     * Llama al servicio de Otorgar Crédito (Punto 7)
     */
    public RespuestaCredito solicitarCredito(String cedula, double monto, int cuotas) throws Exception {
        URL url = new URL(URL_BASE_BANQUITO + "/otorgar");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
        con.setDoOutput(true);

        // --- 2. ESTA ES LA LÍNEA CORREGIDA ---
        // Forzamos el Locale.US para que el decimal sea '.' y no ','
        String jsonInputString = String.format(
            Locale.US, // <-- La corrección
            "{\"cedula\":\"%s\", \"precioElectrodomestico\":%.2f, \"numeroCuotas\":%d}",
            cedula, monto, cuotas
        );

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);           
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                respuesta.append(linea.trim());
            }
            // Si el servidor devuelve un 200 OK, GSON lo leerá
            return gson.fromJson(respuesta.toString(), RespuestaCredito.class);
        }
    }
    
    /**
     * Llama al servicio de Consultar Amortización (Punto 10)
     */
    public List<AmortizacionDetalle> consultarAmortizacion(int idCreditoBanco) throws Exception {
        
        URL url = new URL(URL_BASE_BANQUITO + "/amortizacion/" + idCreditoBanco);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder respuesta = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                respuesta.append(linea.trim());
            }
            
            Type tipoLista = new TypeToken<ArrayList<AmortizacionDetalle>>(){}.getType();
            return gson.fromJson(respuesta.toString(), tipoLista);
        }
    }
    
    // --- Métodos de Ayuda (Helpers) ---

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
            // Si el servidor nos da un 400 o 500, leemos el error
            // y lanzamos una excepción para que el cliente la atrape
            br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
            StringBuilder respuestaError = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                respuestaError.append(linea.trim());
            }
            br.close();
            // Lanzamos el error HTTP
            throw new Exception("El servidor BanQuito respondió con error " + status + ": " + respuestaError.toString());
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