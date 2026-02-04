package wseurekabank_clicon;

import ec.edu.monster.controlador.EurekaController;
import ec.edu.monster.modelo.Movimiento;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

public class WSEurekaBank_CLICON {

    // Credenciales QUEMADAS en el cliente
    private static final String USER_HARDCODED = "MONSTER";
    private static final String PASS_HARDCODED = "MONSTER9";

    public static void main(String[] args) {
        // --- LOGIN ---
        if (!loginInteractivo()) {
            System.out.println("Credenciales inválidas. Saliendo...");
            System.exit(1);
        }

        // --- Menú principal ---
        Scanner scanner = new Scanner(System.in);
        EurekaController controller = new EurekaController();

        while (true) {
            System.out.println("\n--- Eurekabank Services Menu ---");
            System.out.println("1. Consultar movimientos de cuenta");
            System.out.println("2. Realizar depósito");
            System.out.println("3. Realizar retiro");
            System.out.println("4. Realizar transferencia");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            int option = leerEnteroSeguro(scanner);

            switch (option) {
                case 1: {
                    System.out.print("Ingrese el número de cuenta: ");
                    String account = scanner.nextLine().trim();
                    List<Movimiento> movements = controller.traerMovimientos(account);

                    if (movements.isEmpty()) {
                        System.out.println("No se encontraron movimientos para la cuenta " + account);
                    } else {
                        // Más reciente primero
                        movements.sort((a, b) -> b.getNromov() - a.getNromov());
                        double saldo = controller.traerSaldo(account);

                        System.out.println("\nMovimientos de la cuenta " + account + ":");
                        System.out.println("--------------------------------------------------------------------------");
                        System.out.printf("%-10s %-20s %-10s %-10s %-10s%n",
                                "NroMov", "Fecha", "Tipo", "Acción", "Importe");
                        System.out.println("--------------------------------------------------------------------------");
                        for (Movimiento m : movements) {
                            System.out.printf("%-10d %-20s %-10s %-10s %-10.2f%n",
                                    m.getNromov(), m.getFecha(), m.getTipo(), m.getAccion(), m.getImporte());
                        }
                        System.out.println("--------------------------------------------------------------------------");
                        System.out.printf("Saldo actual: %.2f%n", saldo);
                    }
                    break;
                }
                case 2: {
                    System.out.print("Ingrese el número de cuenta: ");
                    String depositAccount = scanner.nextLine().trim();
                    System.out.print("Ingrese el importe a depositar: ");
                    double amount = leerDoubleSeguro(scanner);
                    System.out.print("Ingrese el código del empleado: ");
                    String codEmp = scanner.nextLine().trim();
                    try {
                        controller.regDeposito(depositAccount, amount, codEmp);
                        System.out.println("Depósito realizado con éxito.");
                    } catch (RuntimeException e) {
                        System.out.println("Error al realizar el depósito: " + e.getMessage());
                    }
                    break;
                }
                case 3: {
                    System.out.print("Ingrese el número de cuenta: ");
                    String retiroAccount = scanner.nextLine().trim();
                    double saldoInicial = controller.traerSaldo(retiroAccount);
                    System.out.printf("Saldo actual: %.2f%n", saldoInicial);
                    System.out.print("Ingrese el importe a retirar: ");
                    double retiroImporte = leerDoubleSeguro(scanner);
                    if (retiroImporte > saldoInicial) {
                        System.out.println("Fondos insuficientes.");
                        break;
                    }
                    System.out.print("Ingrese el código del empleado: ");
                    String retiroEmp = scanner.nextLine().trim();
                    try {
                        controller.regRetiro(retiroAccount, retiroImporte, retiroEmp);
                        double saldoFinal = controller.traerSaldo(retiroAccount);
                        System.out.printf("Retiro realizado. Saldo final: %.2f%n", saldoFinal);
                    } catch (RuntimeException e) {
                        System.out.println("Error al realizar el retiro: " + e.getMessage());
                    }
                    break;
                }
                case 4: {
                    System.out.print("Cuenta origen: ");
                    String origen = scanner.nextLine().trim();
                    System.out.print("Cuenta destino: ");
                    String destino = scanner.nextLine().trim();
                    System.out.print("Importe a transferir: ");
                    double monto = leerDoubleSeguro(scanner);
                    System.out.print("Código de empleado: ");
                    String codTransfer = scanner.nextLine().trim();
                    try {
                        controller.regTransferencia(origen, destino, monto, codTransfer);
                        System.out.println("Transferencia realizada con éxito.");
                    } catch (RuntimeException e) {
                        System.out.println("Error al realizar transferencia: " + e.getMessage());
                    }
                    break;
                }
                case 5: {
                    System.out.println("Saliendo del programa...");
                    scanner.close();
                    System.exit(0);
                    break;
                }
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        }
    }

    // ===================== Helpers =====================

    private static boolean loginInteractivo() {
        Scanner sc = new Scanner(System.in);
        Console console = System.console();

        final int MAX_INTENTOS = 3;
        for (int intento = 1; intento <= MAX_INTENTOS; intento++) {
            System.out.println("=== Login Eurekabank ===");
            System.out.print("Usuario: ");
            String user = sc.nextLine().trim();

            String pass;
            if (console != null) {
                char[] pwdChars = console.readPassword("Contraseña: ");
                pass = (pwdChars == null) ? "" : new String(pwdChars);
            } else {
                // Fallback si no hay consola (NetBeans)
                System.out.print("Contraseña: ");
                pass = sc.nextLine();
            }

            if (validarCredenciales(user, pass)) {
                System.out.println("Acceso concedido. ¡Bienvenido, " + USER_HARDCODED + "!");
                return true;
            } else {
                System.out.println("Credenciales inválidas. Intento " + intento + " de " + MAX_INTENTOS + ".");
            }
        }
        return false;
    }

    private static boolean validarCredenciales(String user, String pass) {
        // Usuario insensible a mayúsculas; contraseña sensible.
        return USER_HARDCODED.equalsIgnoreCase(user) && PASS_HARDCODED.equals(pass);
    }

    private static int leerEnteroSeguro(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    private static double leerDoubleSeguro(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }
}
