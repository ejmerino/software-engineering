package ec.edu.monster.test;

import com.google.gson.Gson; // Importa Gson
import ec.edu.monster.model.dto.RespuestaMonto;
import ec.edu.monster.model.dto.RespuestaValidacion;
import ec.edu.monster.service.CreditoService;

/**
 * Esta prueba ejecuta la lógica de negocio real en CreditoService
 * desde un método main simple, sin necesidad del servidor (GlassFish).
 */
public class TestLogicaServicio {

    public static void main(String[] args) {
        
        // 1. Creamos manualmente los objetos (sin @Inject)
        CreditoService servicio = new CreditoService();
        Gson gson = new Gson();
        
        System.out.println("--- INICIANDO PRUEBA DE LÓGICA DE SERVICIO ---");
        System.out.println("Usando la conexión: jdbc:mariadb://localhost:3307/db_banquito\n");

        // --- PRUEBA PUNTO 5: Validar Sujeto de Crédito ---
        
        System.out.println("--- Prueba Punto 5: Validaciones ---");
        
        // Prueba 1: Cliente APROBADO (Juan Pérez: 1000000001)
        String cedulaAprobado = "1000000001";
        RespuestaValidacion resAprobado = servicio.validarSujetoDeCredito(cedulaAprobado);
        System.out.println("Resultado para " + cedulaAprobado + ": " + gson.toJson(resAprobado));

        // Prueba 2: Cliente RECHAZADO (Regla 2: Sin depósito) (Carlos Sánchez: 1000000003)
        String cedulaRechazadoDep = "1000000003";
        RespuestaValidacion resRechazadoDep = servicio.validarSujetoDeCredito(cedulaRechazadoDep);
        System.out.println("Resultado para " + cedulaRechazadoDep + ": " + gson.toJson(resRechazadoDep));
        
        // Prueba 3: Cliente RECHAZADO (Regla 4: Crédito activo) (Ana Gómez: 1000000004)
        String cedulaRechazadoCred = "1000000004";
        RespuestaValidacion resRechazadoCred = servicio.validarSujetoDeCredito(cedulaRechazadoCred);
        System.out.println("Resultado para " + cedulaRechazadoCred + ": " + gson.toJson(resRechazadoCred));

        // Prueba 4: Cliente NO EXISTE
        String cedulaNoExiste = "9999999999";
        RespuestaValidacion resNoExiste = servicio.validarSujetoDeCredito(cedulaNoExiste);
        System.out.println("Resultado para " + cedulaNoExiste + ": " + gson.toJson(resNoExiste));
        
        System.out.println("\n--- Prueba Punto 6: Cálculo de Monto ---");

        // --- PRUEBA PUNTO 6: Calcular Monto Máximo ---

        // Prueba 5: Monto para cliente APROBADO (Juan Pérez: 1000000001)
        RespuestaMonto montoAprobado = servicio.calcularMontoMaximo(cedulaAprobado);
        System.out.println("Monto para " + cedulaAprobado + ": " + gson.toJson(montoAprobado));
        
        // Prueba 6: Monto para cliente RECHAZADO (Ana Gómez: 1000000004)
        // (Debería fallar en la validación interna)
        RespuestaMonto montoRechazado = servicio.calcularMontoMaximo(cedulaRechazadoCred);
        System.out.println("Monto para " + cedulaRechazadoCred + ": " + gson.toJson(montoRechazado));
        
        System.out.println("\n--- PRUEBA FINALIZADA ---");
    }
}