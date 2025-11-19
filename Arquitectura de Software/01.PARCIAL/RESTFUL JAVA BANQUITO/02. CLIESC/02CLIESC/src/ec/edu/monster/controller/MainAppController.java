package ec.edu.monster.controller;

// No hay cambios en los imports, excepto JOptionPane que ya deberías tener
import ec.edu.monster.model.AmortizacionDetalle;
import ec.edu.monster.model.Cliente;
import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.RespuestaFacturacion;
import ec.edu.monster.service.ComercializadoraService;
import ec.edu.monster.view.MainAppView;
import ec.edu.monster.view.SelectorProductoView;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


public class MainAppController {
    
    private MainAppView view;
    private ComercializadoraService service;
    // No hay cambios en las propiedades de la clase
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    private Cliente clienteSeleccionado;
    private Electrodomestico productoSeleccionado;

    public MainAppController(MainAppView view, ComercializadoraService service) {
        this.view = view;
        this.service = service;
        iniciarEventos();
        cargarCatalogo(view.getTblCatalogo()); 
    }
    
    // El método iniciarEventos no cambia, ya que los botones siguen existiendo
    private void iniciarEventos() {
        view.getBtnRegistrarCliente().addActionListener(e -> registrarCliente()); 
        view.getBtnBuscarCliente().addActionListener(e -> buscarCliente()); 
        view.getBtnBuscarProducto().addActionListener(e -> abrirSelectorProducto()); 
        view.getCmbVentaFormaPago().addItemListener(e -> toggleCuotas(e));
        view.getBtnProcesarCompra().addActionListener(e -> procesarCompra()); 
        view.getBtnAgregarAlCarrito().addActionListener(e -> agregarAlCarrito());
        view.getBtnQuitarItem().addActionListener(e -> quitarItem()); 
        
        view.getBtnListarCatalogo().addActionListener(e -> cargarCatalogo(view.getTblCatalogo()));
        view.getBtnBuscarAmortizacion().addActionListener(e -> buscarAmortizacion()); 
        
        view.getTblCatalogo().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && view.getTblCatalogo().getSelectedRow() != -1) {
                seleccionarProductoDesdeTabla(view.getTblCatalogo());
            }
        });
    }

    // --- CAMBIO PRINCIPAL: MÉTODO procesarCompra() ---
    
    private void procesarCompra() {
        // 1. Validaciones iniciales (estas se mantienen igual)
        DefaultTableModel model = (DefaultTableModel) view.getTblCarrito().getModel();
        if (model.getRowCount() == 0) {
            mostrarError("El carrito está vacío. Añada al menos un producto.");
            return;
        }
        if (clienteSeleccionado == null) {
            mostrarError("Debe buscar y seleccionar un cliente válido.");
            return;
        }
        
        int cuotas = 0;
        String formaPago = view.getCmbVentaFormaPago().getSelectedItem().toString();
        if ("Credito".equals(formaPago)) {
            try {
                cuotas = Integer.parseInt(view.getTxtVentaCuotas().getText());
                if (cuotas < 3 || cuotas > 24) {
                    mostrarError("El 'Nro. de Cuotas' debe ser entre 3 y 24.");
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarError("El 'Nro. de Cuotas' debe ser un número válido.");
                return;
            }
        }

        // 2. NUEVO: Generar el resumen de la factura ANTES de enviarla
        String resumenFactura = generarResumenParaDialogo();
        
        // 3. NUEVO: Mostrar el diálogo de confirmación desde la vista y esperar la respuesta
        int decision = view.showInvoiceConfirmationDialog(resumenFactura);

        // 4. NUEVO: Si el usuario confirma, entonces procedemos a enviar la data al servidor
        if (decision == JOptionPane.OK_OPTION) {
            // El código que estaba aquí (construir JSON y llamar al servicio) ahora va DENTRO de este 'if'.
            
            // Construir el array JSON de items
            List<String> itemsJson = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                itemsJson.add(String.format("{\"idElectrodomestico\":%d,\"cantidad\":%d}",
                        (int) model.getValueAt(i, 0), (int) model.getValueAt(i, 2)));
            }
            String itemsArray = "[" + String.join(",", itemsJson) + "]";

            // Construir la petición final
            String jsonPeticion = String.format(
                    "{\"cedulaCliente\":\"%s\",\"formaPago\":\"%s\",\"numeroCuotas\":%d,\"items\":%s}",
                    clienteSeleccionado.getCedula(),
                    formaPago,
                    cuotas,
                    itemsArray
            );

            // Iniciar el hilo para llamar al servicio (esto se mantiene igual)
            new Thread(() -> {
                try {
                    RespuestaFacturacion respuesta = service.procesarFactura(jsonPeticion);
                    
                    SwingUtilities.invokeLater(() -> {
                        if (respuesta.isFueExitoso()) {
                            Factura f = respuesta.getFactura();
                            // En lugar de imprimir en el JTextArea, ahora mostramos un mensaje final de éxito
                            mostrarMensaje("Éxito", "Factura procesada correctamente.\nID de Factura: " + f.getIdFactura());
                            
                            // Limpiamos el carrito y otros campos
                            ((DefaultTableModel) view.getTblCarrito().getModel()).setRowCount(0); 
                            limpiarFormularioVenta();

                            if (f.getIdCreditoBanco() > 0) {
                                cargarAmortizacion(f.getIdCreditoBanco());
                            }
                        } else {
                            mostrarError("Factura Rechazada por el servidor: " + respuesta.getError());
                        }
                    });
                } catch (Exception ex) {
                    mostrarError("Error de sistema al procesar la factura: " + ex.getMessage());
                }
            }).start();
        } else {
            // Si el usuario cancela, simplemente mostramos un mensaje y no hacemos nada más.
            mostrarMensaje("Cancelado", "La operación de compra ha sido cancelada.");
        }
    }
    
    // --- NUEVO MÉTODO HELPER ---
    
    /**
     * Genera un string bien formateado con el resumen de la venta actual para mostrarlo en el diálogo.
     * @return Un String con la información de la factura.
     */
    private String generarResumenParaDialogo() {
        StringBuilder sb = new StringBuilder();
        sb.append("      *** RESUMEN DE LA COMPRA ***\n\n");
        sb.append("Cliente: ").append(view.getTxtVentaCedulaNombre().getText()).append("\n");
        sb.append("Cédula:  ").append(view.getTxtVentaCedula().getText()).append("\n\n");
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-25s %5s %15s\n", "Producto", "Cant.", "Subtotal"));
        sb.append("--------------------------------------------------\n");

        DefaultTableModel model = (DefaultTableModel) view.getTblCarrito().getModel();
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < model.getRowCount(); i++) {
            String nombre = model.getValueAt(i, 1).toString();
            int cantidad = (int) model.getValueAt(i, 2);
            BigDecimal subtotal = (BigDecimal) model.getValueAt(i, 4);
            total = total.add(subtotal);
            
            if (nombre.length() > 24) {
                nombre = nombre.substring(0, 21) + "...";
            }
            sb.append(String.format("%-25s %5d %15.2f\n", nombre, cantidad, subtotal));
        }

        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-31s %15.2f\n", "TOTAL:", total));
        
        if (view.getCmbVentaFormaPago().getSelectedItem().equals("Credito")) {
            sb.append("\nForma de Pago: Crédito a ").append(view.getTxtVentaCuotas().getText()).append(" cuotas.");
        } else {
            sb.append("\nForma de Pago: Efectivo.");
        }

        return sb.toString();
    }
    
    // --- NUEVO MÉTODO HELPER PARA LIMPIAR ---
    private void limpiarFormularioVenta() {
        clienteSeleccionado = null;
        productoSeleccionado = null;
        view.getTxtVentaCedula().setText("");
        view.getTxtVentaCedulaNombre().setText("");
        view.getTxtVentaProductoNombre().setText("(Seleccione un producto)");
        view.getTxtVentaCantidad().setText("1");
        view.getLblImagenProducto().setIcon(null);
        view.getCmbVentaFormaPago().setSelectedIndex(0);
        view.getTxtVentaCuotas().setText("0");
    }

    // --- EL RESTO DE MÉTODOS SE MANTIENE IGUAL ---
    // (registrarCliente, buscarCliente, buscarAmortizacion, etc., no necesitan cambios)
    
    private void registrarCliente() { 
        String cedula = view.getTxtCliCedula().getText();
        if (cedula.isEmpty() || view.getTxtCliNombres().getText().isEmpty()) {
            mostrarError("La cédula y el nombre son obligatorios.");
            return;
        }
        
        String jsonPeticion = String.format(
            "{\"cedula\":\"%s\",\"nombres\":\"%s\",\"apellidos\":\"%s\",\"direccion\":\"%s\",\"telefono\":\"%s\",\"email\":\"%s\"}",
            cedula, view.getTxtCliNombres().getText(), view.getTxtCliApellidos().getText(), view.getTxtCliDireccion().getText(), view.getTxtCliTelefono().getText(), view.getTxtCliEmail().getText()
        );
        
        new Thread(() -> {
            try {
                String respuesta = service.registrarCliente(jsonPeticion);
                mostrarMensaje("Registro de Cliente", "Respuesta del Servidor: \n" + respuesta);
            } catch (Exception ex) {
                mostrarError("Error al registrar cliente: " + ex.getMessage());
            }
        }).start();
    }
    
    private void buscarCliente() { 
        String cedula = view.getTxtVentaCedula().getText();
        if (cedula.isEmpty()) {
            mostrarError("Ingrese una cédula para buscar.");
            return;
        }
        
        new Thread(() -> {
            try {
                clienteSeleccionado = service.getCliente(cedula);
                SwingUtilities.invokeLater(() -> {
                    if (clienteSeleccionado != null) {
                        view.getTxtVentaCedulaNombre().setText(clienteSeleccionado.getNombres() + " " + clienteSeleccionado.getApellidos());
                    } else {
                        clienteSeleccionado = null;
                        view.getTxtVentaCedulaNombre().setText("(Cliente no encontrado)");
                        mostrarError("Cliente con cédula " + cedula + " no está registrado en la tienda.");
                    }
                });
            } catch (Exception ex) {
                clienteSeleccionado = null;
                mostrarError("Error de red al buscar cliente: " + ex.getMessage());
            }
        }).start();
    }
    
    private void buscarAmortizacion() { 
        try {
            int idCredito = Integer.parseInt(view.getTxtConsultaIdCredito().getText());
            cargarAmortizacion(idCredito);
        } catch (NumberFormatException e) {
            mostrarError("Error: El ID del crédito debe ser un número.");
        }
    }
    
    private void seleccionarProductoDesdeTabla(JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            int id = (int) model.getValueAt(fila, 0);
            String nombre = (String) model.getValueAt(fila, 1);
            BigDecimal precio = (BigDecimal) model.getValueAt(fila, 3);

            productoSeleccionado = new Electrodomestico();
            productoSeleccionado.setIdElectrodomestico(id);
            productoSeleccionado.setNombre(nombre);
            productoSeleccionado.setPrecioVenta(precio);
            
            SwingUtilities.invokeLater(() -> {
                view.getTxtVentaProductoNombre().setText(nombre + " ($" + precio.setScale(2, BigDecimal.ROUND_HALF_UP) + ")");
                view.getLblVentaIdProducto().setText(String.valueOf(id));
                cargarImagenProducto(id);
                view.getTabbedPane().setSelectedIndex(0); 
            });
        }
    }

    private void abrirSelectorProducto() { 
        SelectorProductoView dialogView = new SelectorProductoView(view, true);
        SelectorProductoController dialogController = new SelectorProductoController(dialogView, service);
        dialogView.setVisible(true);
        
        productoSeleccionado = dialogController.getProductoSeleccionado();
        
        if (productoSeleccionado != null) {
            SwingUtilities.invokeLater(() -> {
                view.getTxtVentaProductoNombre().setText(productoSeleccionado.getNombre() + " ($" + productoSeleccionado.getPrecioVenta().setScale(2, BigDecimal.ROUND_HALF_UP) + ")");
                view.getLblVentaIdProducto().setText(String.valueOf(productoSeleccionado.getIdElectrodomestico()));
                cargarImagenProducto(productoSeleccionado.getIdElectrodomestico());
            });
        }
    }

    private void cargarImagenProducto(int idProducto) {
        String path = "/ec/edu/monster/images/" + idProducto + ".png"; 
        
        try {
            Image img = new ImageIcon(getClass().getResource(path)).getImage()
                    .getScaledInstance(100, 100, Image.SCALE_SMOOTH); 
            SwingUtilities.invokeLater(() -> {
                view.getLblImagenProducto().setIcon(new ImageIcon(img));
                view.getLblImagenProducto().setText("");
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                view.getLblImagenProducto().setIcon(null);
                view.getLblImagenProducto().setText("IMG " + idProducto);
            });
        }
    }

    private void toggleCuotas(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (view.getCmbVentaFormaPago().getSelectedItem().equals("Efectivo")) {
                view.getTxtVentaCuotas().setText("0");
                view.getTxtVentaCuotas().setEnabled(false);
            } else {
                view.getTxtVentaCuotas().setText("");
                view.getTxtVentaCuotas().setEnabled(true);
            }
        }
    }
    
    private void agregarAlCarrito() {
        if (productoSeleccionado == null) {
            mostrarError("Debe seleccionar un producto del catálogo para añadir.");
            return;
        }
        
        int cantidad;
        try {
            cantidad = Integer.parseInt(view.getTxtVentaCantidad().getText());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un número entero mayor a 0.");
            return;
        }
        
        DefaultTableModel model = (DefaultTableModel) view.getTblCarrito().getModel();
        BigDecimal precioU = productoSeleccionado.getPrecioVenta();
        BigDecimal subtotal = precioU.multiply(new BigDecimal(cantidad));
        
        model.addRow(new Object[]{
            productoSeleccionado.getIdElectrodomestico(),
            productoSeleccionado.getNombre(),
            cantidad,
            precioU.setScale(2, BigDecimal.ROUND_HALF_UP),
            subtotal
        });
        
        productoSeleccionado = null;
        view.getTxtVentaProductoNombre().setText("(Seleccione un producto)");
        view.getTxtVentaCantidad().setText("1");
        view.getLblImagenProducto().setIcon(null);
    }

    private void quitarItem() {
        DefaultTableModel model = (DefaultTableModel) view.getTblCarrito().getModel();
        int fila = view.getTblCarrito().getSelectedRow();
        if (fila != -1) {
            model.removeRow(fila);
        } else {
            mostrarError("Seleccione un item para quitar.");
        }
    }

    private void cargarCatalogo(JTable tablaDestino) {
        new Thread(() -> {
            try {
                List<Electrodomestico> catalogo = service.listarCatalogo();
                SwingUtilities.invokeLater(() -> {
                    DefaultTableModel model = (DefaultTableModel) tablaDestino.getModel();
                    model.setRowCount(0); 
                    
                    for(Electrodomestico item : catalogo) {
                        model.addRow(new Object[]{
                            item.getIdElectrodomestico(),
                            item.getNombre(),
                            item.getDescripcion(),
                            item.getPrecioVenta()
                        });
                    }
                });
            } catch (Exception ex) {
                mostrarError("Error de red: " + ex.getMessage());
            }
        }).start();
    }
    
    private void cargarAmortizacion(int idCredito) {
        new Thread(() -> {
            try {
                List<AmortizacionDetalle> tabla = service.consultarAmortizacion(idCredito);
                SwingUtilities.invokeLater(() -> {
                    DefaultTableModel model = (DefaultTableModel) view.getTblAmortizacion().getModel();
                    model.setRowCount(0); 

                    if(tabla.isEmpty()) {
                        mostrarError("No se encontró amortización para el ID: " + idCredito);
                        return;
                    }
                    
                    for(AmortizacionDetalle d : tabla) {
                        model.addRow(new Object[]{
                            d.getNumeroCuota(),
                            dateFormat.format(d.getFechaPagoProgramada()),
                            d.getValorCuota(),
                            d.getInteresPagado(),
                            d.getCapitalPagado(),
                            d.getSaldoCapital()
                        });
                    }
                    view.getTabbedPane().setSelectedIndex(1);
                });
            } catch (Exception ex) {
                mostrarError("Error de red: " + ex.getMessage());
            }
        }).start();
    }
    
    // Este método ya no es necesario, pero lo dejamos por si quieres reutilizarlo en otro lugar.
    private void imprimirFacturaEnGUI(Factura f) {
        // ...
    }

    private void mostrarError(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(view, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        });
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(view, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
        });
    }
}