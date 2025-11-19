package ec.edu.monster.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ec.edu.monster.model.Cliente;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.CatalogoService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/catalogo")
public class CatalogoController {

    @Inject
    private CatalogoService catalogoService;

    // CONFIGURA GSON PARA ENVIAR FECHAS EN FORMATO SQL (por si usas fechas en Cliente)
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    // ==========================
    //   ELECTRODOMÉSTICOS
    // ==========================

    // GET /api/catalogo
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarCatalogo() {
        try {
            List<Electrodomestico> lista = catalogoService.listarTodos();
            return Response.ok(gson.toJson(lista)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    // GET /api/catalogo/{id}
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducto(@PathParam("id") int id) {
        try {
            Electrodomestico e = catalogoService.getElectrodomesticoPorId(id);
            if (e != null) {
                return Response.ok(gson.toJson(e)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("{\"error\":\"Producto no encontrado\"}")
                               .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    // POST /api/catalogo
    @POST
    @Consumes(MediaType.TEXT_PLAIN)   // envías JSON como texto plano
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearElectrodomestico(String jsonElectrodomestico) {
        try {
            Electrodomestico e = gson.fromJson(jsonElectrodomestico, Electrodomestico.class);
            e = catalogoService.crear(e);
            return Response.status(Response.Status.CREATED).entity(gson.toJson(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    // PUT /api/catalogo/{id}
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarElectrodomestico(@PathParam("id") int id, String jsonElectrodomestico) {
        try {
            Electrodomestico e = gson.fromJson(jsonElectrodomestico, Electrodomestico.class);
            e.setIdElectrodomestico(id); // usamos el ID de la ruta
            catalogoService.actualizar(e);
            return Response.ok(gson.toJson(e)).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + ex.getMessage() + "\"}")
                           .build();
        }
    }

    // DELETE /api/catalogo/{id}
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarElectrodomestico(@PathParam("id") int id) {
        try {
            catalogoService.eliminar(id);
            return Response.noContent().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + ex.getMessage() + "\"}")
                           .build();
        }
    }

    // ==========================
    //   CLIENTES TIENDA
    // ==========================

    // POST /api/catalogo/cliente
    @POST
    @Path("/cliente")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearCliente(String jsonCliente) {
        try {
            Cliente c = gson.fromJson(jsonCliente, Cliente.class);
            c = catalogoService.crearCliente(c);
            return Response.status(Response.Status.CREATED).entity(gson.toJson(c)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    // GET /api/catalogo/cliente/{cedula}
    @GET
    @Path("/cliente/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCliente(@PathParam("cedula") String cedula) {
        try {
            Cliente c = catalogoService.getClientePorCedula(cedula);
            if (c != null) {
                return Response.ok(gson.toJson(c)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("{\"error\":\"Cliente no encontrado\"}")
                               .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }
}
