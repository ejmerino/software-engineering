package ec.edu.monster.prueba;

import ec.edu.monster.servicio.EurekaService;

public class PruebaStressVentanillas {
    public static void main(String[] args) {
        final EurekaService service = new EurekaService();
        final String CUENTA_COMPARTIDA = "00100001"; // Cuenta para Cajeros 1, 2 y 3
        final String CUENTA_INDIPENDIENTE = "00100002"; // Cuenta para Cajero 4

        System.out.println("--- INICIANDO SIMULACIÓN DE 4 VENTANILLAS CONCURRENTES ---");

        // Simulamos a los 3 cajeros intentando entrar a la MISMA cuenta
        for (int i = 1; i <= 3; i++) {
            final int idCajero = i;
            new Thread(() -> {
                System.out.println("[Cajero " + idCajero + "] Intentando acceder a " + CUENTA_COMPARTIDA);
                if (service.intentarBloquearCuenta(CUENTA_COMPARTIDA)) {
                    System.out.println("[Cajero " + idCajero + "] ¡BLOQUEO EXITOSO! Operando...");
                    try { Thread.sleep(2000); } catch (InterruptedException e) {} // Simula tiempo de transacción
                    service.liberarCuenta(CUENTA_COMPARTIDA);
                    System.out.println("[Cajero " + idCajero + "] Transacción finalizada y LIBERADA.");
                } else {
                    System.out.println("[Cajero " + idCajero + "] ERROR: Cuenta ocupada (Semáforo en Rojo).");
                }
            }).start();
        }

        // Simulamos al 4to cajero en una cuenta DISTINTA (Debe entrar sin problemas)
        new Thread(() -> {
            System.out.println("[Cajero 4] Intentando acceder a " + CUENTA_INDIPENDIENTE);
            if (service.intentarBloquearCuenta(CUENTA_INDIPENDIENTE)) {
                System.out.println("[Cajero 4] ¡ACCESO EXITOSO! (Cuenta independiente).");
                service.liberarCuenta(CUENTA_INDIPENDIENTE);
            }
        }).start();
    }
}