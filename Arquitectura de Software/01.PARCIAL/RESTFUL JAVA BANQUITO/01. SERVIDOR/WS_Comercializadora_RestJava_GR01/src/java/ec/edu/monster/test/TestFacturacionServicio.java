package ec.edu.monster.test;

import com.google.gson.Gson;
import ec.edu.monster.model.Factura; // Importado para el valor de retorno
import ec.edu.monster.model.dto.ItemFactura;
import ec.edu.monster.model.dto.PeticionFactura;
import ec.edu.monster.model.dto.RespuestaFacturacion;
import ec.edu.monster.service.BanQuitoClienteService;
import ec.edu.monster.service.FacturacionService;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference; // Para manejar el resultado en el hilo

/**
 * Prueba la lógica local de FacturacionService (Punto 9).
 * Esta prueba llama a los servicios REST del otro servidor (BanQuito).
 *
 * ¡¡ASEGÚRATE DE QUE WS_BanQuito_RestJava_GR01 ESTÉ CORRIENDO!!
 */
public class TestFacturacionServicio {

    // Simula la inyección de dependencias (CDI)
    private static FacturacionService facturacionService;
    private static BanQuitoClienteService banquitoCliente;
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        
        // --- INICIALIZACIÓN (Simulamos @Inject) ---
        // Creamos manualmente los servicios
        banquitoCliente = new BanQuitoClienteService();
        facturacionService = new FacturacionService();
        
        // "Inyectamos" manualmente la dependencia (Asume que este setter existe)
        facturacionService.setBanquitoCliente(banquitoCliente);

        System.out.println("--- INICIANDO PRUEBA DE FACTURACIÓN ---");
        
        // --- PRUEBA 2: PAGO A CRÉDITO (El flujo completo) ---
        System.out.println("\n--- Prueba 2: Venta a CRÉDITO (Juan Pérez) ---");
        
        PeticionFactura peticionCredito = new PeticionFactura();
        peticionCredito.setCedulaCliente("1000000001"); // Cliente que aprueba (de db_banquito)
        peticionCredito.setFormaPago("Credito");
        peticionCredito.setNumeroCuotas(12);
        
        // Creamos la "canasta de compra" usando el constructor de dos argumentos
        peticionCredito.setItems(new ArrayList<>());
        // ID 1 (Refrigeradora) Cantidad 1
        ItemFactura item1 = new ItemFactura(1, 1); 
        peticionCredito.getItems().add(item1);

        System.out.println("Enviando Petición: " + gson.toJson(peticionCredito));

        try {
            // procesarFactura ahora devuelve RespuestaFacturacion
            RespuestaFacturacion respuesta = facturacionService.procesarFactura(peticionCredito);
            
            // 2. Comprobamos la respuesta
            if (respuesta.isFueExitoso()) {
                Factura facturaGenerada = respuesta.getFactura();
                System.out.println("\n--- Factura Generada Exitosamente (Respuesta) ---");
                System.out.println("ID Generado: " + facturaGenerada.getIdFactura());
                System.out.println("Detalle JSON: " + gson.toJson(facturaGenerada));
            } else {
                // Imprime el rechazo de negocio (ej. "Crédito supera monto")
                System.err.println("\n--- RECHAZO DE NEGOCIO (Controlado) ---");
                System.err.println("Razón: " + respuesta.getError());
            }

        } catch (Exception e) {
            // Este catch es para errores de sistema (ej. conexión a BD fallida)
            System.err.println("\n--- ERROR DE SISTEMA (No Controlado) ---");
            System.err.println("Causa: " + e.getMessage());
        }
        
        System.out.println("\n--- PRUEBA FINALIZADA ---");
    }
}