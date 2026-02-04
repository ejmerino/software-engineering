package ec.edu.monster.config;

import java.io.IOException;
import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Context;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

@Provider
public class CorsFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
        // Obtenemos el origen de la petición dinámicamente
        String origin = request.getHeaderString("Origin");
        
        response.getHeaders().add("Access-Control-Allow-Origin", origin); // Permite el origen actual
        response.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}