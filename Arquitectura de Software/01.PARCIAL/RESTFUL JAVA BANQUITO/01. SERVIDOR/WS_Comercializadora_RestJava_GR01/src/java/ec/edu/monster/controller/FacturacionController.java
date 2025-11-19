package ec.edu.monster.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder; // <-- IMPORTA EL BUILDER
import ec.edu.monster.model.AmortizacionDetalle;
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.dto.PeticionFactura;
import ec.edu.monster.model.dto.RespuestaFacturacion;
import ec.edu.monster.service.BanQuitoClienteService;
import ec.edu.monster.service.FacturacionService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/facturacion")
public class FacturacionController {

    @Inject
    private FacturacionService facturacionService;
    @Inject
    private BanQuitoClienteService banquitoCliente;
    
    // CONFIGURA GSON PARA ENVIAR FECHAS EN FORMATO SQL
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearFactura(String jsonPeticion) {
        try {
            PeticionFactura peticion = gson.fromJson(jsonPeticion, PeticionFactura.class);
            RespuestaFacturacion respuesta = facturacionService.procesarFactura(peticion);
            return Response.ok(gson.toJson(respuesta)).build();
        } catch (Exception e) {
             return Response.status(Response.Status.BAD_REQUEST)
                           .entity(gson.toJson(new RespuestaFacturacion("Error: JSON mal formado.")))
                           .build();
        }
    }
    
    @GET
    @Path("/consulta-credito/{idCreditoBanco}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarCreditoExterno(@PathParam("idCreditoBanco") int idCreditoBanco) {
        try {
            List<AmortizacionDetalle> tabla = banquitoCliente.consultarAmortizacion(idCreditoBanco);
            return Response.ok(gson.toJson(tabla)).build();
        } catch (Exception e) {
             return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }
    
    /**
     * Servicio para el Punto 9 (Consultar Factura por ID)
     * URL: GET /api/facturacion/{idFactura}
     */
    @GET
    @Path("/{idFactura}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFacturaById(@PathParam("idFactura") int idFactura) {
        try {
            Factura factura = facturacionService.getFacturaById(idFactura);
            
            if (factura != null) {
                return Response.ok(gson.toJson(factura)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("{\"error\":\"Factura ID " + idFactura + " no encontrada.\"}")
                               .build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"Error de servidor al buscar la factura: " + e.getMessage() + "\"}")
                           .build();
        }
    }
}