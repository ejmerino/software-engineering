package ec.edu.monster.model;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class ConversionModel {

    public double convert(String category, String from, String to, double value) throws Exception {
        URL url = new URL("http://localhost:5069/api/Convert");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type","application/json");
        conn.setDoOutput(true);

        JSONObject json = new JSONObject();
        json.put("category", category);
        json.put("from", from);
        json.put("to", to);
        json.put("value", value);

        OutputStream os = conn.getOutputStream();
        os.write(json.toString().getBytes());
        os.flush();
        os.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while((line=in.readLine())!=null) content.append(line);
        in.close();
        conn.disconnect();

        JSONObject response = new JSONObject(content.toString());
        return response.getDouble("result");
    }
}
