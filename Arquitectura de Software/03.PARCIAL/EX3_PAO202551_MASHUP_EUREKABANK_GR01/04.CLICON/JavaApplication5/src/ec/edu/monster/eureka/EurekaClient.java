/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.eureka;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:CoreBancarioResource
 * [coreBancario]<br>
 * USAGE:
 * <pre>
 *        EurekaClient client = new EurekaClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author diego
 */
public class EurekaClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/WSEurekaBank_GRO01/webresources";

    public EurekaClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("coreBancario");
    }

    public <T> T getMovimientos(Class<T> responseType, String cuenta) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("movimientos/{0}", new Object[]{cuenta}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public Response registrarDeposito() throws ClientErrorException {
        return webTarget.path("deposito").request(javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED).post(null, Response.class);
    }

    public void close() {
        client.close();
    }
    
}
