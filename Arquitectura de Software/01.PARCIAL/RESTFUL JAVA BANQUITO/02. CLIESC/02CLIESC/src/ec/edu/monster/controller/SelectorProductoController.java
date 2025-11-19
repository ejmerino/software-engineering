package ec.edu.monster.controller;

import ec.edu.monster.model.Electrodomestico;
import ec.edu.monster.service.ComercializadoraService;
import ec.edu.monster.view.SelectorProductoView;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.concurrent.atomic.AtomicReference; // Para manejar el resultado en el hilo

public class SelectorProductoController {
    
    private SelectorProductoView view;
    private ComercializadoraService service;
    private Electrodomestico productoSeleccionado = null;
    
    // Almacenamos el catálogo completo con todas las rutas de imagen
    private List<Electrodomestico> catalogoCompleto; 

    public SelectorProductoController(SelectorProductoView view, ComercializadoraService service) {
        this.view = view;
        this.service = service;
        
        // Lógica de carga y eventos
        cargarCatalogo();
        
        this.view.getBtnSeleccionar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seleccionarProducto();
            }
        });
        
        // Detección de selección de fila
        this.view.getTblProductos().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostrarDetalle();
                }
            }
        });
    }
    
    public Electrodomestico getProductoSeleccionado() {
        return productoSeleccionado;
    }
    
    private void cargarCatalogo() {
        new Thread(() -> {
            try {
                // Obtenemos el catálogo completo desde el servidor (incluye ruta_imagen)
                catalogoCompleto = service.listarCatalogo(); 
                
                SwingUtilities.invokeLater(() -> {
                    DefaultTableModel model = (DefaultTableModel) view.getTblProductos().getModel();
                    model.setRowCount(0); 
                    
                    // Solo mostramos las columnas necesarias para seleccionar
                    for(Electrodomestico item : catalogoCompleto) {
                        model.addRow(new Object[]{
                            item.getIdElectrodomestico(),
                            item.getNombre(),
                            item.getPrecioVenta(),
                            item.getDescripcion() 
                        });
                    }
                });
            } catch (Exception ex) {
                mostrarError("Error al cargar catálogo: " + ex.getMessage());
            }
        }).start();
    }
    
    private void mostrarDetalle() {
        int filaSeleccionada = view.getTblProductos().getSelectedRow();
        if (filaSeleccionada == -1) return;
        
        DefaultTableModel model = (DefaultTableModel) view.getTblProductos().getModel();
        int id = (int) model.getValueAt(filaSeleccionada, 0); // Obtenemos el ID de la fila seleccionada
        
        // Buscamos el producto completo con la ruta de imagen en la lista de catálogo
        Electrodomestico producto = catalogoCompleto.stream()
                .filter(p -> p.getIdElectrodomestico() == id)
                .findFirst().orElse(null);
        
        if (producto != null) {
            // 1. Cargamos la imagen con el nombre de archivo exacto
            cargarImagen(producto.getRutaImagen());
            
            // 2. Mostramos el texto en la caja de detalles
            view.getTxtProductoSeleccionado().setText(producto.getNombre() + " - $" + producto.getPrecioVenta().toString());
            
            // 3. Almacenamos el objeto completo (incluye ruta)
            this.productoSeleccionado = producto;
        }
    }

    private void cargarImagen(String nombreArchivo) {
        // La ruta final utiliza el nombre de archivo (ej: 1.png) obtenido de la BD
        String path = "/ec/edu/monster/images/" + nombreArchivo; 
        
        try {
            Image img = new ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH); // 200x200 para la vista del selector
            SwingUtilities.invokeLater(() -> {
                view.getLblImagenProducto().setIcon(new ImageIcon(img));
                view.getLblImagenProducto().setText("");
            });
        } catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                view.getLblImagenProducto().setIcon(null);
                view.getLblImagenProducto().setText("IMG NO DISP: " + nombreArchivo);
            });
        }
    }
    
    private void seleccionarProducto() {
        if (this.productoSeleccionado == null) {
            mostrarError("Debe seleccionar un producto de la lista.");
            return;
        }
        // Si hay un producto seleccionado, cerramos la ventana
        view.dispose();
    }
    
    private void mostrarError(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(view, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        });
    }
}