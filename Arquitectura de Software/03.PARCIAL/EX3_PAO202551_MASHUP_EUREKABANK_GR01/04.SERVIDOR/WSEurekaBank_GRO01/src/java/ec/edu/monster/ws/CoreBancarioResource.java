package ec.edu.monster.ws;

import ec.edu.monster.modelo.Movimiento;
import ec.edu.monster.servicio.EurekaService;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Recurso REST para la gestión de operaciones bancarias bajo arquitectura SOA. [cite: 44]
 * Implementa control de concurrencia y notificaciones en tiempo real para el Mashup. [cite: 32, 37]
 */
@Path("coreBancario")
public class CoreBancarioResource {

    @Context
    private UriInfo context;

    /**
     * GET: Lista los movimientos de una cuenta específica. [cite: 3]
     * No requiere bloqueo ya que es una operación de solo lectura. [cite: 11]
     */
    @GET
    @Path("/movimientos/{cuenta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovimientos(@PathParam("cuenta") String cuenta) {
        try {
            EurekaService service = new EurekaService();
            List<Movimiento> lista = service.leerMovimientos(cuenta);
            
            if (lista.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"No hay movimientos para la cuenta " + cuenta + "\"}")
                        .build();
            }
            
            String json = service.convertirMovimientosAJSON(lista);
            return Response.ok(json).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al consultar: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * POST: Registra un depósito. [cite: 3]
     * Bloquea la cuenta automáticamente y notifica a las demás ventanillas. [cite: 30, 31]
     */
   @POST
    @Path("/deposito")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposito(@FormParam("cuenta") String cuenta, @FormParam("importe") double importe) {
        try {
            Thread.sleep(3000);
            // Log para ver qué llega
            System.out.println("Intentando depósito en: " + cuenta + " Monto: " + importe);

            EurekaService service = new EurekaService();
            service.registrarDeposito(cuenta, importe, "0001"); // El ID '0001' debe existir

            return Response.ok("{\"estado\":1, \"mensaje\":\"Depósito exitoso\"}").build();
        } catch (Exception e) {
            // ESTO ES LO QUE NECESITAMOS VER EN EL OUTPUT DE NETBEANS
            e.printStackTrace(); 
            return Response.status(500).entity("{\"estado\":0, \"mensaje\":\"Error interno: " + e.getMessage() + "\"}").build();
        }
    }

    /**
     * POST: Registra un retiro. [cite: 3]
     * Garantiza que solo una operación se ejecute a la vez sobre la cuenta. [cite: 23]
     */
    @POST
    @Path("/retiro")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarRetiro(@FormParam("cuenta") String cuenta,
                                    @FormParam("importe") double importe) {
        EurekaService service = new EurekaService();
        
        if (service.intentarBloquearCuenta(cuenta)) {
            try {
                Thread.sleep(3000); // Retardo para visualización de sincronización
                service.registrarRetiro(cuenta, importe, "0001");
                return Response.ok("{\"estado\":1, \"mensaje\":\"Retiro procesado exitosamente.\"}").build();
            } catch (Exception e) {
                return Response.ok("{\"estado\":0, \"mensaje\":\"Error: " + e.getMessage() + "\"}").build();
            } finally {
                service.liberarCuenta(cuenta);
            }
        } else {
            return Response.status(409).entity("{\"estado\":0, \"mensaje\":\"Cuenta bloqueada temporalmente.\"}").build();
        }
    }

    /**
     * POST: Realiza una transferencia entre cuentas. [cite: 3]
     * Bloquea la cuenta de origen durante la ejecución. [cite: 11, 31]
     */
    @POST
    @Path("/transferencia")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarTransferencia(@FormParam("origen") String origen,
                                           @FormParam("destino") String destino,
                                           @FormParam("importe") double importe) {
        EurekaService service = new EurekaService();
        
        if (service.intentarBloquearCuenta(origen)) {
            try {
                Thread.sleep(3000);
                service.registrarTransferencia(origen, destino, importe, "0001");
                return Response.ok("{\"estado\":1, \"mensaje\":\"Transferencia realizada exitosamente.\"}").build();
            } catch (Exception e) {
                return Response.ok("{\"estado\":0, \"mensaje\":\"Error en transferencia: " + e.getMessage() + "\"}").build();
            } finally {
                service.liberarCuenta(origen);
            }
        } else {
            return Response.status(409).entity("{\"estado\":0, \"mensaje\":\"Cuenta origen ocupada por otro proceso.\"}").build();
        }
    }
}