package ec.edu.monster.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Prueba la aplicación DESPLEGADA actuando como Postman.
 * Ejecuta esta prueba DESPUÉS de que el Deploy haya sido exitoso.
 */
public class TestClienteRest {

    // El JSON que queremos enviar
    public static final String JSON_PETICION = "{\"cedula\":\"1000000001\"," +
                                             "\"precioElectrodomestico\":650.50," +
                                             "\"numeroCuotas\":12}";
    
    // La URL del servicio (Punto 7)
    public static final String URL_OTORGAR = "http://localhost:8080/WS_BanQuito_RestJava_GR01/api/credito/otorgar";

    public static void main(String[] args) {
        System.out.println("--- Probando POST a " + URL_OTORGAR + " ---");
        System.out.println("Enviando JSON: " + JSON_PETICION);
        
        try {
            URL url = new URL(URL_OTORGAR);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            con.setRequestMethod("POST");
            
            // -----------------------------------------------------------------
            // ESTA ES LA LÍNEA QUE HAY QUE CAMBIAR
            // con.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); // <-- ESTO ESTABA MAL
            con.setRequestProperty("Content-Type", "text/plain; charset=UTF-8"); // <-- ESTO ES LO CORRECTO
            // -----------------------------------------------------------------
            
            con.setDoOutput(true);
            
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = JSON_PETICION.getBytes("utf-8");
                os.write(input, 0, input.length);           
            }
            
            int status = con.getResponseCode();
            System.out.println("\nRespuesta del Servidor:");
            System.out.println("HTTP Status Code: " + status); // Ahora deberías ver 200
            
            // Si el status es un error (ej: 415, 500), debemos leer el errorStream
            BufferedReader br;
            if (status >= 400) {
                System.out.println("Respuesta de ERROR del servidor:");
                br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
            } else {
                System.out.println("Cuerpo de la Respuesta (ÉXITO):");
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            }
            
            // Leer la respuesta
            StringBuilder respuesta = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                respuesta.append(responseLine.trim());
            }
            System.out.println(respuesta.toString());

        } catch (Exception e) {
            // Este error ya no debería salir
            System.err.println("--- ERROR ---");
            System.err.println("Falló la conexión al servicio. ¿Está el servidor GlassFish corriendo?");
            e.printStackTrace();
        }
    }
}