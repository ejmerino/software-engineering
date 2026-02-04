package ec.edu.monster.servicio;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ApiClient {

    public static String get(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        try {
            return readFully(conn.getInputStream());
        } finally {
            conn.disconnect();
        }
    }

    public static String postForm(String urlStr, String[][] params) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        StringBuilder body = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) body.append("&");
            body.append(URLEncoder.encode(params[i][0], "UTF-8"))
                .append("=")
                .append(URLEncoder.encode(params[i][1], "UTF-8"));
        }

        OutputStream os = null;
        try {
            os = conn.getOutputStream();
            os.write(body.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
        } finally {
            if (os != null) try { os.close(); } catch (IOException ignore) {}
        }

        try {
            return readFully(conn.getInputStream());
        } finally {
            conn.disconnect();
        }
    }

    /** Lectura compatible con Java 8 (sin InputStream.readAllBytes) */
    private static String readFully(InputStream in) throws IOException {
        if (in == null) return "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        try {
            while ((n = in.read(buf)) != -1) {
                baos.write(buf, 0, n);
            }
        } finally {
            try { in.close(); } catch (IOException ignore) {}
        }
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }
}
