package ec.edu.monster.servicio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.modelo.Movimiento;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EurekaService {

    private static final String BASE =
            "http://localhost:8080/WSEurekaBank_GRO01/webresources/coreBancario";
    private final Gson gson = new Gson();

    public List<Movimiento> traerMovimientos(String cuenta) throws Exception {
        String endpoint = BASE + "/movimientos/" + encode(cuenta);
        HttpURLConnection con = (HttpURLConnection) new URL(endpoint).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        int code = con.getResponseCode();
        String body = read(con);
        if (code != 200) throw new RuntimeException("HTTP " + code + " -> " + body);
        Type listType = new TypeToken<List<Movimiento>>(){}.getType();
        return gson.fromJson(body, listType);
    }

    public void regDeposito(String cuenta, double importe, String codEmp) throws Exception {
        String endpoint = BASE + "/deposito";
        String params = "cuenta=" + encode(cuenta) + "&importe=" + encode(String.valueOf(importe));
        post(endpoint, params);
    }

    public void regRetiro(String cuenta, double importe, String codEmp) throws Exception {
        String endpoint = BASE + "/retiro";
        String params = "cuenta=" + encode(cuenta) + "&importe=" + encode(String.valueOf(importe));
        post(endpoint, params);
    }

    public void regTransferencia(String origen, String destino, double importe, String codEmp) throws Exception {
        String endpoint = BASE + "/transferencia";
        String params = "origen=" + encode(origen) + "&destino=" + encode(destino) + "&importe=" + encode(String.valueOf(importe));
        post(endpoint, params);
    }

    public double obtenerSaldo(String cuenta) throws Exception {
        List<Movimiento> lista = traerMovimientos(cuenta);
        double saldo = 0;
        for (Movimiento m : lista) {
            if ("INGRESO".equalsIgnoreCase(m.getAccion())) saldo += m.getImporte();
            else saldo -= m.getImporte();
        }
        return saldo;
    }

    // ===== utilitarios =====
    private void post(String endpoint, String params) throws Exception {
        HttpURLConnection con = (HttpURLConnection) new URL(endpoint).openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
            out.write(params.getBytes(StandardCharsets.UTF_8));
        }
        int code = con.getResponseCode();
        String body = read(con);
        if (code != 200) throw new RuntimeException("HTTP " + code + " -> " + body);
    }

    private static String read(HttpURLConnection con) throws Exception {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        (con.getResponseCode() >= 400 ? con.getErrorStream() : con.getInputStream()),
                        StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line; while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        }
    }

    private static String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
