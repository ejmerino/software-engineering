package ec.edu.monster.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class SelectorProductoView extends javax.swing.JDialog {

    // --- Componentes ---
    private JButton btnSeleccionar;
    private JTable tblProductos;
    private JLabel lblImagenProducto; 
    private JTextField txtProductoSeleccionado; 
    
    public SelectorProductoView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent); 
    }
    
    // Getters para el controlador
    public JButton getBtnSeleccionar() { return btnSeleccionar; }
    public JTable getTblProductos() { return tblProductos; }
    public JLabel getLblImagenProducto() { return lblImagenProducto; }
    public JTextField getTxtProductoSeleccionado() { return txtProductoSeleccionado; }
    
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Seleccionar Producto del Catálogo");
        setPreferredSize(new Dimension(800, 500)); 

        // --- Contenedor Principal (Split Pane) ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250); 
        splitPane.setBackground(Color.WHITE); // Fondo blanco para la tabla (mejor legibilidad)
        
        // 1. Panel Izquierdo: Imagen y Detalles (250px)
        JPanel panelDetalle = new JPanel(new BorderLayout(10, 10));
        panelDetalle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelDetalle.setBackground(new Color(240, 240, 240)); // Fondo gris claro
        
        // Marco de la Imagen
        lblImagenProducto = new JLabel("Click en una fila para ver imagen"); 
        lblImagenProducto.setPreferredSize(new Dimension(200, 200));
        lblImagenProducto.setHorizontalAlignment(JLabel.CENTER);
        lblImagenProducto.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        lblImagenProducto.setBackground(Color.WHITE);
        lblImagenProducto.setOpaque(true);
        panelDetalle.add(lblImagenProducto, BorderLayout.NORTH);
        
        // Campo de texto para el producto seleccionado
        txtProductoSeleccionado = new JTextField();
        txtProductoSeleccionado.setEditable(false);
        txtProductoSeleccionado.setFont(new Font("Tahoma", Font.BOLD, 14));
        panelDetalle.add(txtProductoSeleccionado, BorderLayout.CENTER);
        
        btnSeleccionar = new JButton("Seleccionar Producto");
        btnSeleccionar.setBackground(new Color(60, 179, 113)); // Verde
        btnSeleccionar.setForeground(Color.WHITE);
        panelDetalle.add(btnSeleccionar, BorderLayout.SOUTH);
        
        splitPane.setLeftComponent(panelDetalle);

        // 2. Panel Derecho: Tabla de Productos
        JPanel panelTabla = new JPanel(new BorderLayout());
        tblProductos = new JTable(new DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Nombre", "Precio", "Descripción"} // El controlador usa ID(0), Nombre(1), Precio(2), Ruta(4)
        ));
        tblProductos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        panelTabla.add(new JScrollPane(tblProductos), BorderLayout.CENTER);
        
        splitPane.setRightComponent(panelTabla);

        add(splitPane, BorderLayout.CENTER);
        pack();
    }
}