package ec.edu.monster.controller;

import java.io.InputStream;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.glassfish.jersey.media.multipart.*;

@Path("/images")
public class ImagenController {

    @GET
    @Path("/{nombre}")
    @Produces({"image/png", "image/jpeg"})
    public Response obtenerImagen(@PathParam("nombre") String nombre) {

        // Ruta dentro del classpath
        String ruta = "/ec/edu/monster/images/" + nombre;

        // Cargar archivo dentro del JAR / paquete
        InputStream is = getClass().getResourceAsStream(ruta);

        if (is == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Imagen no encontrada: " + nombre)
                    .build();
        }

        return Response.ok(is).build();
    }
}
