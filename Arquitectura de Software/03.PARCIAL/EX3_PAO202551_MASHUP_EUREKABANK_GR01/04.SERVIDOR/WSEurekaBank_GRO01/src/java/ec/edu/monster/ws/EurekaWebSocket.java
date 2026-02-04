package ec.edu.monster.ws;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/eureka-notificaciones")
public class EurekaWebSocket {
    // Lista de todas las ventanillas conectadas (Web, Móvil, Escritorio)
    private static Set<Session> clientes = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) {
        clientes.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        clientes.remove(session);
    }

    // Método para avisar a todos que una cuenta se bloqueó o liberó
    public static void notificarCambioEstado(String idCuenta, String estado) {
        String mensaje = idCuenta + ":" + estado; // Ejemplo: "001:BLOQUEADO"
        for (Session s : clientes) {
            if (s.isOpen()) {
                try { s.getBasicRemote().sendText(mensaje); } catch (IOException e) {}
            }
        }
    }
}