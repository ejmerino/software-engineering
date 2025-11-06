package ec.edu.monster.controller;

import ec.edu.monster.service.ValidarSujetoCreditoWS;
import ec.edu.monster.service.ValidarSujetoCreditoWS_Service;
import java.net.URL;
import javax.xml.namespace.QName;

public class FacturacionController {

    public boolean verificarCredito(String cedula) {
        try {
            URL wsdlURL = new URL("http://localhost:8080/WS_BanQuito_SOAPJAVA_GR01/ValidarSujetoCreditoWS?wsdl");
            QName qname = new QName("http://service.monster.edu.ec/", "ValidarSujetoCreditoWS");
            ValidarSujetoCreditoWS_Service service = new ValidarSujetoCreditoWS_Service(wsdlURL, qname);

            ValidarSujetoCreditoWS port = service.getValidarSujetoCreditoWSPort();

            boolean sujeto = port.esSujetoCredito(cedula);
            System.out.println("¿Es sujeto de crédito?: " + sujeto);
            return sujeto;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
