package ec.edu.monster.servicio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.eureka.Config;
import ec.edu.monster.modelo.Movimiento;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

public class EurekaServiceClient {
    private static final Gson GSON = new Gson();

    public List<Movimiento> movimientos(String cuenta) throws Exception {
        String url = Config.baseUrl() + "/movimientos/" + cuenta;
        String json = ApiClient.get(url);
        Type t = new TypeToken<List<Movimiento>>(){}.getType();
        List<Movimiento> list = GSON.fromJson(json, t);
        if (list == null) list = new ArrayList<>();

        // Orden DESC por fecha (más reciente primero)
        list.sort((a,b) -> date(b.getFecha()).compareTo(date(a.getFecha())));

        // Calcular saldo acumulado cronológicamente (ASC)
        double saldo = 0.0;
        List<Movimiento> asc = new ArrayList<>(list);
        asc.sort(Comparator.comparing(m -> date(m.getFecha())));
        for (Movimiento m : asc) {
            String tipo = (m.getTipo() == null ? "" : m.getTipo()).toLowerCase();
            double imp = m.getImporte();
            if (tipo.contains("apertura") || tipo.contains("deposit")) saldo += imp;
            else if (tipo.contains("retiro") || tipo.contains("cancelar")) saldo -= imp;
            m.setSaldoCalculado(saldo);
        }
        return list;
    }

    public boolean deposito(String cuenta, double importe) throws Exception {
        String url = Config.baseUrl() + "/deposito";
        String resp = ApiClient.postForm(url, new String[][]{
                {"cuenta", cuenta}, {"importe", String.valueOf(importe)}
        });
        return resp.contains("\"estado\":1");
    }

    public boolean retiro(String cuenta, double importe) throws Exception {
        String url = Config.baseUrl() + "/retiro";
        String resp = ApiClient.postForm(url, new String[][]{
                {"cuenta", cuenta}, {"importe", String.valueOf(importe)}
        });
        return resp.contains("\"estado\":1");
    }

    public boolean transferencia(String origen, String destino, double importe) throws Exception {
        String url = Config.baseUrl() + "/transferencia";
        String resp = ApiClient.postForm(url, new String[][]{
                {"origen", origen}, {"destino", destino}, {"importe", String.valueOf(importe)}
        });
        return resp.contains("\"estado\":1");
    }

    private static Date date(String s) {
        if (s == null) return new Date(0);
        try {
            // "yyyy-MM-dd HH:mm:ss.S"
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(s);
        } catch (Exception e) {
            try { return javax.xml.bind.DatatypeConverter.parseDateTime(s).getTime(); }
            catch (Exception ex) { return new Date(0); }
        }
    }
}
