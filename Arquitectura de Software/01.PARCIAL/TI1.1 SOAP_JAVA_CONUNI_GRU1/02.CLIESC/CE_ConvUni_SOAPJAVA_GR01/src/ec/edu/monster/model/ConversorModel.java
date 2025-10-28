package ec.edu.monster.model;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;

public class ConversorModel {

    private static final String NAMESPACE = "ser";
    private static final String URI = "http://service.monster.edu.ec/";
    private static final String URL = "http://localhost:8080/WS_ConvUni_SOAPJAVA_GR01/ConversorUnidadesService";

    public double convertir(String tipo, double valor) throws Exception {
        // Crear mensaje SOAP
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage();
        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        SOAPBody body = envelope.getBody();

        // Crear body según tipo de conversión
        SOAPBodyElement element = body.addBodyElement(envelope.createName(tipo, NAMESPACE, URI));
        String paramName = getParametro(tipo);
        element.addChildElement(paramName).addTextNode(String.valueOf(valor));

        // Conexión
        SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection connection = connectionFactory.createConnection();
        SOAPMessage response = connection.call(message, URL);
        connection.close();

        // Parsear respuesta
        SOAPBody responseBody = response.getSOAPBody();
        String result = responseBody.getFirstChild().getFirstChild().getTextContent();

        return Double.parseDouble(result);
    }

    private String getParametro(String tipo) {
    switch(tipo) {
        case "metros_a_kilometros": return "metros";
        case "kilometros_a_metros": return "kilometros";
        case "centimetros_a_metros": return "centimetros";
        case "gramos_a_kilogramos": return "gramos";
        case "kilogramos_a_gramos": return "kilogramos";
        case "libras_a_kilogramos": return "libras";
        case "celsius_a_fahrenheit":
        case "celsius_a_kelvin": return "celsius";
        case "fahrenheit_a_celsius": return "fahrenheit";
        default: return "valor";
        }
    }       

}
