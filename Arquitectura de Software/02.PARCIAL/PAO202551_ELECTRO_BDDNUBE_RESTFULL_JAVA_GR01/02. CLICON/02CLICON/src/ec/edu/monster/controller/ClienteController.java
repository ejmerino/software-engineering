package ec.edu.monster.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ec.edu.monster.model.AmortizacionDetalle;
import ec.edu.monster.model.Cliente;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.FacturaDetalle; // Asegúrate de tener este import
import ec.edu.monster.model.RespuestaFacturacion;
import ec.edu.monster.service.ComercializadoraService;
import ec.edu.monster.model.dto.ItemFactura;
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
        System.out.println("\n--- Registrar Nuevo Cliente ---");
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
            
            // Creamos el objeto para usar Gson (más limpio que String.format)
            Cliente c = new Cliente();
            c.setCedula(cedula); c.setNombres(nombres); c.setApellidos(apellidos);
            c.setDireccion(direccion); c.setTelefono(telefono); c.setEmail(email);
            
            String jsonPeticion = gson.toJson(c);
            
            System.out.println("\nRegistrando cliente...");
            String respuesta = servicio.registrarCliente(jsonPeticion);
            System.out.println("Respuesta del Servidor: Cliente registrado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
        }
    }

    public void realizarCompra() {
    System.out.println("\n--- Nueva Compra ---");
    try {
        Scanner scanner = new Scanner(System.in); // Aseguramos scanner fresco

        // 1. Validar Cliente
        System.out.print("Ingrese Cédula del cliente: ");
        String cedula = scanner.nextLine();
        Cliente cliente = servicio.getCliente(cedula); // O servicio.buscarCliente(cedula)
        
        if (cliente == null) {
            System.out.println("(!) Cliente no encontrado. Por favor, regístrelo primero.");
            return;
        }
        System.out.println("\n¡Bienvenido, " + cliente.getNombres() + "!");

        // =========================================================================
        // [MEJORA CLAVE] CARGAR CATÁLOGO EN MEMORIA ANTES DE PEDIR PRODUCTOS
        // =========================================================================
        System.out.println("(Cargando catálogo actualizado...)");
        List<Electrodomestico> catalogo = servicio.listarCatalogo(); // Asegúrate de tener este método en tu servicio
        
        if (catalogo == null || catalogo.isEmpty()) {
            System.out.println("(!) Error: No se pudo obtener el catálogo o está vacío.");
            return;
        }
        // =========================================================================

        List<ItemFactura> items = new ArrayList<>(); 
        
        do {
            System.out.print("ID del Electrodomestico (0 para finalizar): ");
            try {
                int idProducto = Integer.parseInt(scanner.nextLine());
                if (idProducto == 0) break;
                
                // BUSQUEDA LOCAL (Mucho más rápida y segura)
                Electrodomestico producto = catalogo.stream()
                    .filter(p -> p.getIdElectrodomestico() == idProducto)
                    .findFirst()
                    .orElse(null);

                if (producto == null) {
                    System.out.println("(!) Producto no existe.");
                    // Ayuda visual: Mostrar IDs válidos
                    System.out.print("   IDs disponibles: ");
                    catalogo.forEach(p -> System.out.print(p.getIdElectrodomestico() + " "));
                    System.out.println();
                    continue;
                }
                
                System.out.print("-> Producto: " + producto.getNombre() + " (Precio: $"+producto.getPrecioVenta()+") - Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());
                
                if (cantidad <= 0) {
                    System.out.println("(!) La cantidad debe ser mayor a 0.");
                    continue;
                }

                if (cantidad > producto.getStock()) {
                    System.out.println("(!) Stock insuficiente. Disponible: " + producto.getStock());
                    continue;
                }

                items.add(new ItemFactura(idProducto, cantidad)); 
                System.out.println("   [Añadido al carrito]");

            } catch (NumberFormatException e) {
                System.out.println("(!) Error: Ingrese un número válido.");
            }
        } while (true);
        
        if (items.isEmpty()) {
            System.out.println("Carrito vacío. Cancelado.");
            return;
        }
        
        // --- PROCESO DE PAGO (Igual que tenías, pero validando inputs) ---
        System.out.print("Forma de Pago (Efectivo / Credito): ");
        String formaPago = scanner.nextLine();
        
        int cuotas = 0;
        if ("Credito".equalsIgnoreCase(formaPago)) {
            System.out.print("Número de Cuotas (3-24): ");
            try {
                cuotas = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                cuotas = 0; // Fallback
            }
        }
        
        // CONSTRUCCIÓN DEL JSON
        // Usamos Gson para los items para evitar errores de formato manual
        String itemsArray = new com.google.gson.Gson().toJson(items);
        
        String jsonPeticion = String.format(
            "{\"cedulaCliente\":\"%s\",\"formaPago\":\"%s\",\"numeroCuotas\":%d,\"items\":%s}",
            cedula, formaPago, cuotas, itemsArray
        );
        
        System.out.println("\nProcesando factura con el servidor...");
        RespuestaFacturacion respuestaServidor = servicio.procesarFactura(jsonPeticion);
        
        // VERIFICACIÓN DE RESPUESTA
        // Nota: Asegúrate si tu backend devuelve "exito" o "fueExitoso" en el JSON. 
        // Usaré isFueExitoso() asumiendo tu modelo RespuestaFacturacion.
        if (respuestaServidor.isFueExitoso()) { 
            Factura f = respuestaServidor.getFactura(); // O getFacturaGenerada() según tu modelo
            
            System.out.println("\n✅ VENTA EXITOSA - Factura #" + f.getIdFactura());
            System.out.println("Total a Pagar: $" + f.getTotal());
            
            if (f.getIdCreditoBanco() > 0) {
                System.out.println("\nCargando tabla de amortización...");
                List<AmortizacionDetalle> tabla = servicio.consultarAmortizacion(f.getIdCreditoBanco());
                // Asumo que tienes un método para imprimir esto, sino usa un for simple
                System.out.println("--- PLAN DE PAGOS ---");
                for(AmortizacionDetalle d : tabla) {
                    System.out.println("Cuota " + d.getNumeroCuota() + ": $" + d.getValorCuota());
                }
            }
        } else {
            System.out.println("(!) ERROR DEL SERVIDOR: " + respuestaServidor.getError());
        }
        
    } catch (Exception e) {
        System.err.println("Error grave en el cliente: " + e.getMessage());
        e.printStackTrace();
    }
}

    // --- NUEVO MÉTODO PARA BUSCAR FACTURA ---
    public void buscarFactura() {
        System.out.println("\n--- Buscar Factura por ID ---");
        try {
            System.out.print("Ingrese el ID de la Factura: ");
            String input = scanner.nextLine();
            int idFactura = Integer.parseInt(input);
            
            Factura f = servicio.buscarFacturaPorId(idFactura);
            
            if (f != null) {
                imprimirDetalleFactura(f);
            } else {
                System.out.println("(!) Factura no encontrada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un número válido.");
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    public void consultarAmortizacion() {
        System.out.println("\n--- Consultar Tabla de Amortización ---");
        try {
            System.out.print("ID Crédito: ");
            int idCredito = Integer.parseInt(scanner.nextLine());
            List<AmortizacionDetalle> tabla = servicio.consultarAmortizacion(idCredito);
            imprimirTablaAmortizacion(tabla);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    // --- HELPERS DE IMPRESIÓN ---
    
    private void imprimirDetalleFactura(Factura f) {
        System.out.println("\n==========================================");
        System.out.println("          DETALLE DE FACTURA #" + f.getIdFactura());
        System.out.println("==========================================");
        System.out.println(" Fecha: " + dateFormat.format(f.getFecha()));
        if (f.getCliente() != null) {
            System.out.println(" Cliente: " + f.getCliente().getNombres() + " " + f.getCliente().getApellidos());
        }
        System.out.println(" Forma de Pago: " + f.getFormaPago());
        System.out.println("------------------------------------------");
        
        // Imprimir items (Si el backend los manda, aquí saldrán)
        if (f.getDetalles() != null && !f.getDetalles().isEmpty()) {
            System.out.printf("%-20s %-10s %-10s\n", "Producto", "Cant.", "Subtotal");
            for (FacturaDetalle det : f.getDetalles()) {
                // Validación de nulos para evitar crash si el electrodoméstico no vino completo
                String prodNombre = (det.getElectrodomestico() != null) ? det.getElectrodomestico().getNombre() : "Item";
                System.out.printf("%-20s %-10d $%-10.2f\n", 
                        prodNombre, 
                        det.getCantidad(), 
                        det.getSubtotalLinea());
            }
            System.out.println("------------------------------------------");
        } else {
            System.out.println(" (Sin detalles de productos)");
        }
        
        System.out.printf(" Subtotal:      $%.2f\n", f.getSubtotal());
        System.out.printf(" Descuento:     $%.2f\n", f.getDescuento());
        System.out.printf(" TOTAL:         $%.2f\n", f.getTotal());
        System.out.println("==========================================\n");
    }
    
    private void imprimirTablaAmortizacion(List<AmortizacionDetalle> tabla) {
        if (tabla == null || tabla.isEmpty()) {
            System.out.println("Sin datos de amortización.");
            return;
        }
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("| %-5s | %-12s | %-10s | %-10s | %-10s |\n", "Cuota", "Fecha", "Valor", "Interés", "Saldo");
        System.out.println("----------------------------------------------------------------------");
        for (AmortizacionDetalle d : tabla) {
            System.out.printf("| %-5d | %-12s | $%-9.2f | $%-9.2f | $%-9.2f |\n",
                    d.getNumeroCuota(), dateFormat.format(d.getFechaPagoProgramada()), 
                    d.getValorCuota(), d.getInteresPagado(), d.getSaldoCapital());
        }
        System.out.println("----------------------------------------------------------------------");
    }
}