package ec.edu.monster.test;

import ec.edu.monster.service.ValidarSujetoCreditoWS;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class TestWS {

    public static void main(String[] args) {
        System.out.println("🔍 Probando conexión con el WS de BanQuito...");

        try {
            // URL del WSDL desplegado en tu servidor
            URL wsdlURL = new URL("http://localhost:8080/WS_BanQuito_SOAPJAVA_GR01/ValidarSujetoCreditoWS?wsdl");

            // targetNamespace y nombre del servicio del WSDL
            QName qname = new QName("http://service.monster.edu.ec/", "ValidarSujetoCreditoWS");

            // Crear el servicio directamente desde el WSDL
            Service service = Service.create(wsdlURL, qname);

            // Obtener el puerto (interfaz)
            ValidarSujetoCreditoWS port = service.getPort(ValidarSujetoCreditoWS.class);

            // Llamar al método remoto
            String cedula = "1754321890";
            boolean resultado = port.esSujetoCredito(cedula);

            System.out.println("✅ Resultado: " + cedula + " → ¿Es sujeto de crédito? " + resultado);

        } catch (Exception e) {
            System.out.println("❌ Error al consumir el servicio: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
