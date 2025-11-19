package ec.edu.monster.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ec.edu.monster.model.AmortizacionDetalle;
import ec.edu.monster.model.Cliente;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.RespuestaFacturacion;
import ec.edu.monster.service.ComercializadoraService;
import ec.edu.monster.model.dto.ItemFactura; 
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteController {
    
    private final ComercializadoraService servicio = new ComercializadoraService();
    private final Scanner scanner = new Scanner(System.in);
    private final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void mostrarCatalogo() {
        System.out.println("\n--- Obteniendo Catálogo de Productos ---");
        try {
            List<Electrodomestico> catalogo = servicio.listarCatalogo();
            if (catalogo == null || catalogo.isEmpty()) {
                System.out.println("No hay productos en el catálogo.");
                return;
            }
            // Imprime los productos con Stock y Precio
            for (Electrodomestico item : catalogo) {
                System.out.println("ID: " + item.getIdElectrodomestico() + 
                                   " | Producto: " + item.getNombre() + 
                                   " | Precio: $" + item.getPrecioVenta() + 
                                   " | Stock: " + item.getStock());
            }
        } catch (Exception e) {
            System.err.println("Error al obtener catálogo: " + e.getMessage());
        }
    }
    
    public void registrarCliente() {
        System.out.println("\n--- Registrar Nuevo Cliente (en la Tienda) ---");
        try {
            System.out.print("Cédula: ");
            String cedula = scanner.nextLine();
            System.out.print("Nombres: ");
            String nombres = scanner.nextLine();
            System.out.print("Apellidos: ");
            String apellidos = scanner.nextLine();
            System.out.print("Dirección: ");
            String direccion = scanner.nextLine();
            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            String jsonPeticion = String.format(
                "{\"cedula\":\"%s\",\"nombres\":\"%s\",\"apellidos\":\"%s\",\"direccion\":\"%s\",\"telefono\":\"%s\",\"email\":\"%s\"}",
                cedula, nombres, apellidos, direccion, telefono, email
            );
            System.out.println("\nRegistrando cliente...");
            String respuesta = servicio.registrarCliente(jsonPeticion);
            System.out.println("Respuesta del Servidor: " + respuesta);
        } catch (Exception e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
        }
    }

    public void realizarCompra() {
        System.out.println("\n--- Nueva Compra ---");
        
        try {
            System.out.print("Ingrese Cédula del cliente: ");
            String cedula = scanner.nextLine();
            Cliente cliente = servicio.getCliente(cedula);
            if (cliente == null) {
                System.out.println("(!) Cliente no encontrado. Por favor, regístrelo primero (Opción 2).");
                return;
            }
            System.out.println("\n¡Bienvenido, " + cliente.getNombres() + " " + cliente.getApellidos() + "!");

            // --- Lógica del Carrito (Múltiples Items) ---
            List<ItemFactura> items = new ArrayList<>(); 
            
            do {
                System.out.print("ID del Electrodomestico a comprar (0 para finalizar): ");
                int idProducto = Integer.parseInt(scanner.nextLine());
                if (idProducto == 0) break;
                
                Electrodomestico producto = servicio.getElectrodomestico(idProducto);
                if (producto == null) {
                    System.out.println("(!) Producto con ID " + idProducto + " no existe.");
                    continue;
                }
                
                System.out.print("-> Producto: " + producto.getNombre() + " (Stock: " + producto.getStock() + ") - Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());
                
                if (cantidad > producto.getStock()) {
                    System.out.println("(!) Stock insuficiente. Solo quedan " + producto.getStock() + " unidades. Intente de nuevo.");
                    continue;
                }
                
                items.add(new ItemFactura(idProducto, cantidad)); 
                System.out.println("  [Añadido al carrito. Items totales: " + items.size() + "]");
                
            } while (true);
            
            if (items.isEmpty()) {
                System.out.println("Compra cancelada. Carrito vacío.");
                return;
            }
            // --- Fin Lógica del Carrito ---
            
            System.out.print("Forma de Pago (Efectivo / Credito): ");
            String formaPago = scanner.nextLine();
            
            int cuotas = 0;
            if ("Credito".equalsIgnoreCase(formaPago)) {
                System.out.print("Número de Cuotas (3-24): ");
                cuotas = Integer.parseInt(scanner.nextLine());
            }
            
            // Construimos la petición JSON final
            String itemsArray = gson.toJson(items); // Usamos el gson configurado
            
            String jsonPeticion = String.format(
                "{\"cedulaCliente\":\"%s\",\"formaPago\":\"%s\",\"numeroCuotas\":%d,\"items\":%s}",
                cedula, formaPago, cuotas, itemsArray
            );
            
            System.out.println("\nProcesando factura...");
            RespuestaFacturacion respuestaServidor = servicio.procesarFactura(jsonPeticion);
            
            if (respuestaServidor.isFueExitoso()) {
                Factura f = respuestaServidor.getFactura();
                System.out.println("==========================================");
                System.out.println("  FACTURA GENERADA EXITOSAMENTE");
                System.out.println("==========================================");
                System.out.println(" Nro. Factura: " + f.getIdFactura());
                System.out.println(" Fecha: " + dateFormat.format(f.getFecha()));
                System.out.println(" Cliente: " + cliente.getNombres() + " " + cliente.getApellidos()); // UX FIX
                System.out.println("------------------------------------------");
                System.out.println(" Forma de Pago: " + f.getFormaPago());
                
                // Estos campos tienen un null-check en el getter del DTO
                System.out.printf(" Subtotal:      $%.2f\n", f.getSubtotal().doubleValue());
                System.out.printf(" Descuento:     $%.2f\n", f.getDescuento().doubleValue());
                System.out.printf(" TOTAL A PAGAR: $%.2f\n", f.getTotal().doubleValue());
                
                System.out.println("------------------------------------------");
                
                if (f.getIdCreditoBanco() > 0) {
                    System.out.println(" ID Crédito (BanQuito): " + f.getIdCreditoBanco());
                    System.out.println("==========================================");
                    System.out.println("\nCargando tabla de amortización generada...");
                    List<AmortizacionDetalle> tabla = servicio.consultarAmortizacion(f.getIdCreditoBanco());
                    imprimirTablaAmortizacion(tabla);
                } else {
                    System.out.println("==========================================");
                }
            } else {
                System.out.println("------------------------------------------");
                System.out.println(" (!) LA FACTURA NO PUDO SER PROCESADA");
                System.out.println(" Razón: " + respuestaServidor.getError());
                System.out.println("------------------------------------------");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Error de entrada: Ingrese un número válido.");
        } catch (Exception e) {
            System.err.println("Error de sistema al procesar la compra: " + e.getMessage());
        }
    }

    public void consultarFacturaPorId() { // <-- MÉTODO QUE CAUSÓ LA FALLA DE COMPILACIÓN
        System.out.println("\n--- Consulta de Factura por ID ---");
        System.out.print("Ingrese el ID de la Factura a consultar: ");
        
        try {
            int idFactura = Integer.parseInt(scanner.nextLine());
            
            Factura factura = servicio.getFactura(idFactura); 
            
            if (factura != null) {
                imprimirFactura(factura);
            } else {
                System.out.println("⚠️ Factura con ID " + idFactura + " no encontrada.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Error: Ingrese un número entero válido.");
        } catch (Exception e) {
            System.err.println("Error en la comunicación: " + e.getMessage());
        } finally {
            System.out.println("\n(Presione Enter para volver al menú...)");
            scanner.nextLine();
        }
    }
    
    public void consultarAmortizacion() {
        System.out.println("\n--- Consultar Tabla de Amortización ---");
        try {
            System.out.print("Ingrese el ID del Crédito (el que generó BanQuito): ");
            int idCredito = Integer.parseInt(scanner.nextLine());
            
            System.out.println("Buscando tabla para el crédito ID: " + idCredito);
            List<AmortizacionDetalle> tabla = servicio.consultarAmortizacion(idCredito);
            
            imprimirTablaAmortizacion(tabla);

        } catch (NumberFormatException e) {
            System.err.println("Error: Ingrese un número válido.");
        } catch (Exception e) {
            System.err.println("Error al consultar amortización: " + e.getMessage());
        }
    }
    
    /**
     * MÉTODO PRIVADO PARA IMPRIMIR LA TABLA
     */
    private void imprimirTablaAmortizacion(List<AmortizacionDetalle> tabla) {
        if (tabla == null || tabla.isEmpty()) {
            System.out.println("No se encontraron detalles de amortización para ese crédito.");
            return;
        }
        
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-12s | %-12s | %-15s | %-15s | %-15s |\n", 
                            "Cuota", "Fecha Pago", "Valor Cuota", "Interés Pagado", "Capital Pagado", "Saldo Capital");
        System.out.println("--------------------------------------------------------------------------------------------------");
        
        for (AmortizacionDetalle d : tabla) {
            System.out.printf("| %-5d | %-12s | $%-11.2f | $%-14.2f | $%-14.2f | $%-14.2f |\n",
                              d.getNumeroCuota(), 
                              dateFormat.format(d.getFechaPagoProgramada()), 
                              d.getValorCuota().doubleValue(),       
                              d.getInteresPagado().doubleValue(),
                              d.getCapitalPagado().doubleValue(),
                              d.getSaldoCapital().doubleValue());
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }
    
    /**
     * MÉTODO PRIVADO PARA IMPRIMIR LOS DETALLES DE LA FACTURA
     */
    private void imprimirFactura(Factura f) {
        System.out.println("\n================================================");
        System.out.println("           DETALLE DE FACTURA N° " + f.getIdFactura());
        System.out.println("================================================");
        System.out.printf("FECHA EMISIÓN: %s\n", dateFormat.format(f.getFecha()));
        System.out.printf("C.I. CLIENTE:  %s\n", f.getCedulaCliente());
        System.out.printf("FORMA PAGO:    %s\n", f.getFormaPago());
        System.out.println("------------------------------------------------");
        System.out.printf("SUBTOTAL:      $%-10.2f\n", f.getSubtotal().doubleValue());
        System.out.printf("DESCUENTO:     $%-10.2f\n", f.getDescuento().doubleValue());
        System.out.println("------------------------------------------------");
        System.out.printf("TOTAL A PAGAR: $%-10.2f\n", f.getTotal().doubleValue());
        
        if ("Credito".equalsIgnoreCase(f.getFormaPago())) {
            System.out.printf("ID CRÉDITO BANCO: %d (para consultar amortización)\n", f.getIdCreditoBanco());
        }
        System.out.println("================================================");
    }
}