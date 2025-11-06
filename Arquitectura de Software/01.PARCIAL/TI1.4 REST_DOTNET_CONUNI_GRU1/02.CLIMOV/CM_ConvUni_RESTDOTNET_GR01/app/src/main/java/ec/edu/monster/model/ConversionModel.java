package ec.edu.monster.model;

import org.json.JSONObject;

public class ConversionModel {

    // Método que llama al REST y devuelve el resultado
    public static double convert(String category, String from, String to, double value) throws Exception {
        String urlStr = "http://10.0.2.2:5069/api/Convert";// 10.0.2.2 apunta a localhost desde emulador
        JSONObject json = new JSONObject();
        json.put("category", category);
        json.put("from", from);
        json.put("to", to);
        json.put("value", value);

        java.net.URL url = new java.net.URL(urlStr);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.getOutputStream().write(json.toString().getBytes());

        java.io.InputStream is = conn.getInputStream();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";

        JSONObject res = new JSONObject(response);
        return res.getDouble("result");
    }
}
