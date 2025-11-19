package ec.edu.monster.controller;

import ec.edu.monster.model.Cliente;
import ec.edu.monster.service.ClienteService;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteController {

    private final ClienteService service = new ClienteService();

    // GET /api/clientes
    @GET
    public List<Cliente> listar() throws Exception {
        return service.listar();
    }

    // GET /api/clientes/{cedula}
    @GET
    @Path("{cedula}")
    public Response obtener(@PathParam("cedula") String cedula) throws Exception {
        Cliente c = service.buscarPorCedula(cedula);
        if (c == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(c).build();
    }

    // POST /api/clientes
    @POST
    public Response crear(Cliente c) {
        try {
            service.crear(c);
            return Response.status(Response.Status.CREATED).entity(c).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear cliente: " + e.getMessage())
                    .build();
        }
    }

    // PUT /api/clientes/{cedula}
    @PUT
    @Path("{cedula}")
    public Response actualizar(@PathParam("cedula") String cedula, Cliente c) {
        try {
            c.setCedula(cedula); // seguridad: usar la cédula de la ruta
            service.actualizar(c);
            return Response.ok(c).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar cliente: " + e.getMessage())
                    .build();
        }
    }

    // DELETE /api/clientes/{cedula}
    @DELETE
    @Path("{cedula}")
    public Response eliminar(@PathParam("cedula") String cedula) {
        try {
            service.eliminar(cedula);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar cliente: " + e.getMessage())
                    .build();
        }
    }
}
