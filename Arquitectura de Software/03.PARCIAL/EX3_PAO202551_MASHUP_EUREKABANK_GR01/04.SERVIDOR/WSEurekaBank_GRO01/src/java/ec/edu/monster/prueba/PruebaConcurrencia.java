package ec.edu.monster.prueba;

import ec.edu.monster.servicio.EurekaService;

public class PruebaConcurrencia {
    public static void main(String[] args) {
        EurekaService service = new EurekaService();
        String cuentaTest = "00100001"; // Usa una cuenta que exista en tu DB

        System.out.println("--- INICIANDO PRUEBA DE EXCLUSIÓN MUTUA ---");

        // 1. Primer intento de bloqueo
        boolean primerBloqueo = service.intentarBloquearCuenta(cuentaTest);
        System.out.println("Cajero 1 intenta bloquear: " + (primerBloqueo ? "ÉXITO (Rojo)" : "FALLO"));

        // 2. Segundo intento (Simulando otro cajero mientras el primero sigue operando)
        boolean segundoBloqueo = service.intentarBloquearCuenta(cuentaTest);
        System.out.println("Cajero 2 intenta bloquear la misma cuenta: " + (segundoBloqueo ? "ÉXITO" : "FALLO (Correcto: Cuenta ya bloqueada)"));

        // 3. Liberación
        service.liberarCuenta(cuentaTest);
        System.out.println("Cajero 1 finaliza y libera la cuenta (Verde)");

        // 4. Tercer intento tras liberar
        boolean tercerBloqueo = service.intentarBloquearCuenta(cuentaTest);
        System.out.println("Cajero 2 intenta bloquear de nuevo: " + (tercerBloqueo ? "ÉXITO (Ahora sí pudo)" : "FALLO"));
        
        // Limpieza final
        service.liberarCuenta(cuentaTest);
    }
}