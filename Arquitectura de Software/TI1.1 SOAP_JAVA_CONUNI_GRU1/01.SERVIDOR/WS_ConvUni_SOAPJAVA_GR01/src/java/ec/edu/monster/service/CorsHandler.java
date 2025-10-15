package ec.edu.monster.service;

import java.util.Set;
import java.util.HashSet;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPBody;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.MessageContext;

public class CorsHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean isOutbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (isOutbound) {
            context.put(MessageContext.HTTP_RESPONSE_HEADERS, new java.util.HashMap<String, java.util.List<String>>() {{
                put("Access-Control-Allow-Origin", java.util.Collections.singletonList("*"));
                put("Access-Control-Allow-Methods", java.util.Collections.singletonList("POST, GET, OPTIONS"));
                put("Access-Control-Allow-Headers", java.util.Collections.singletonList("Content-Type"));
            }});
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) { }

    @Override
    public Set<QName> getHeaders() {
        return new HashSet<>();
    }
}
