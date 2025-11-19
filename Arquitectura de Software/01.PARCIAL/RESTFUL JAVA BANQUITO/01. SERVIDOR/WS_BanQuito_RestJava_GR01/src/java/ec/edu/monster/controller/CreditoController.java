package ec.edu.monster.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder; // <-- 1. IMPORTA EL BUILDER
import ec.edu.monster.model.dto.AmortizacionDetalleDTO;
import ec.edu.monster.model.dto.PeticionCredito;
import ec.edu.monster.model.dto.RespuestaCredito;
import ec.edu.monster.model.dto.RespuestaMonto;
import ec.edu.monster.model.dto.RespuestaValidacion;
import ec.edu.monster.service.CreditoService;
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

@Path("/credito")
public class CreditoController {

    @Inject
    private CreditoService creditoService;

    // 2. CONFIGURA GSON PARA ENVIAR FECHAS EN FORMATO SQL
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    /**
     * Servicio para el Punto 5: Validar Sujeto de Crédito
     */
    @GET
    @Path("/validar/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validarSujetoCredito(@PathParam("cedula") String cedula) {
        RespuestaValidacion respuesta = creditoService.validarSujetoDeCredito(cedula);
        return Response.ok(gson.toJson(respuesta)).build();
    }

    /**
     * Servicio para el Punto 6: Calcular Monto Máximo de Crédito
     */
    @GET
    @Path("/monto-maximo/{cedula}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularMontoMaximo(@PathParam("cedula") String cedula) {
        RespuestaMonto respuesta = creditoService.calcularMontoMaximo(cedula);
        return Response.ok(gson.toJson(respuesta)).build();
    }
    
    /**
     * Servicio para el Punto 7: Otorgar Crédito y crear Amortización
     */
    @POST
    @Path("/otorgar")
    @Consumes(MediaType.TEXT_PLAIN) // Recibe TEXTO PLANO
    @Produces(MediaType.APPLICATION_JSON)
    public Response otorgarCredito(String jsonPeticion) {
        
        PeticionCredito peticion;
        try {
            peticion = gson.fromJson(jsonPeticion, PeticionCredito.class);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(gson.toJson(new RespuestaCredito(false, "Error: JSON mal formado.")))
                           .build();
        }
        
        RespuestaCredito respuesta = creditoService.otorgarCredito(peticion);
        
        if (respuesta.isCreditoAprobado()) {
            return Response.ok(gson.toJson(respuesta)).build();
        } else {
            return Response.status(Response.Status.ACCEPTED)
                           .entity(gson.toJson(respuesta)).build();
        }
    }

    /**
     * Servicio para el Punto 10: Consultar Tabla de Amortización (Corregido)
     */
    @GET
    @Path("/amortizacion/{idCredito}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarAmortizacion(@PathParam("idCredito") int idCredito) {
        
        try {
            List<AmortizacionDetalleDTO> tabla = creditoService.consultarTablaAmortizacion(idCredito);
            
            // 3. AHORA USA EL GSON CONFIGURADO
            String jsonRespuesta = gson.toJson(tabla);
            
            return Response.ok(jsonRespuesta).build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(gson.toJson(new RespuestaCredito(false, e.getMessage())))
                           .build();
        }
    }
}