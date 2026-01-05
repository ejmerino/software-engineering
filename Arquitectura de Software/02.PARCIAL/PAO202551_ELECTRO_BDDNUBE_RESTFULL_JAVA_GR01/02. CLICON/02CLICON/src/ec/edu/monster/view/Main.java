package ec.edu.monster.view;

import ec.edu.monster.controller.ClienteController;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner menuScanner = new Scanner(System.in);
        
        System.out.println("--- Cliente de Consola (02CLICON) ---");
        
        if (!realizarLogin(menuScanner)) {
            System.out.println("Acceso denegado. Saliendo del sistema.");
            return; 
        }
        System.out.println("\n¡Acceso concedido! Bienvenido a la Comercializadora.");

        ClienteController controller = new ClienteController();

        boolean salir = false;
        while (!salir) {
            System.out.println("\n========== MENÚ PRINCIPAL ==========");
            System.out.println("1. Ver Catálogo de Electrodomésticos");
            System.out.println("2. Registrar Cliente en Tienda");
            System.out.println("3. Realizar una Compra");
            System.out.println("4. Consultar Tabla de Amortización");
            System.out.println("5. Buscar Factura por ID (NUEVO)"); // <--- NUEVA OPCIÓN
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            
            String opcion = menuScanner.nextLine();
            
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
                case "4":
                    controller.consultarAmortizacion();
                    break;
                case "5":
                    controller.buscarFactura(); // <--- LLAMADA NUEVA
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
            
            if (!salir) {
                System.out.println("\n(Presione Enter para volver al menú...)");
                menuScanner.nextLine();
            }
        }
        System.out.println("Cliente cerrado. ¡Hasta luego!");
    }
    
    private static boolean realizarLogin(Scanner scanner) {
        System.out.println("Por favor, inicie sesión para continuar.");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();
        
        if ("MONSTER".equals(user) && "MONSTER9".equals(pass)) {
            return true;
        } else {
            return false;
        }
    }
}