package ec.edu.monster.view;

import ec.edu.monster.controller.ClienteController;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        
        Scanner menuScanner = new Scanner(System.in);
        // Nota: Asumo que ClienteController está en el classpath
        ClienteController controller = new ClienteController(); 

        System.out.println("==========================================");
        System.out.println("--- Cliente Consola BanQuito-Comercializadora ---");
        System.out.println("==========================================");

        boolean salir = false;
        while (!salir) {
            imprimirMenu();
            
            // Verificación para consumir solo si hay entrada
            if (!menuScanner.hasNextLine()) {
                break;
            }
            
            String opcion = menuScanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    controller.mostrarCatalogo();
                    break;
                case "2":
                    controller.registrarCliente();
                    break;
                case "3":
                    controller.realizarCompra();
                    break;
                case "4": // CORREGIDO: Opción 4 = Consultar Factura (la lógica que falta)
                    controller.consultarFacturaPorId(); 
                    break;
                case "5": // CORREGIDO: Opción 5 = Consultar Amortización
                    controller.consultarAmortizacion();
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
            
            if (!salir) {
                System.out.println("\n(Presione Enter para volver al menú...)");
                menuScanner.nextLine();
            }
        }
        System.out.println("Cliente cerrado. ¡Hasta luego!");
    }
    
    /**
     * Lógica de Login simple.
     */
    private static boolean realizarLogin(Scanner scanner) {
        System.out.println("Por favor, inicie sesión para continuar.");
        
        System.out.print("Usuario: ");
        String user = scanner.nextLine().trim();
        
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine().trim();
        
        // Datos quemados según lo solicitado
        if ("MONSTER".equals(user) && "MONSTER9".equals(pass)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Imprime el menú principal.
     */
    private static void imprimirMenu() {
        System.out.println("\n========== MENÚ PRINCIPAL ==========");
        System.out.println("1. Ver Catálogo de Electrodomésticos (Stock)");
        System.out.println("2. Registrar Cliente en Tienda");
        System.out.println("3. Realizar una Compra");
        System.out.println("4. Consultar Factura por ID"); // <- Nuevo
        System.out.println("5. Consultar Tabla de Amortización");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }
}