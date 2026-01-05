package ec.edu.monster.view;

import ec.edu.monster.model.Factura;
import ec.edu.monster.service.ComercializadoraService;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ConsultasPanel extends JPanel {

    private JTextField txtCedula;
    private JTextField txtIdFactura; // Nuevo campo
    private JButton btnBuscarHistorial;
    private JButton btnBuscarFacturaID; // Nuevo botón
    private JTable tablaFacturas;
    private DefaultTableModel modeloTabla;
    private ComercializadoraService service;

    public ConsultasPanel() {
        service = new ComercializadoraService();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Panel Superior (Buscadores)
        JPanel panelTop = new JPanel(new GridBagLayout());
        panelTop.setBorder(BorderFactory.createTitledBorder("Opciones de Búsqueda"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // --- BUSCADOR 1: POR CÉDULA ---
        gbc.gridx = 0; gbc.gridy = 0;
        panelTop.add(new JLabel("Por Cédula Cliente:"), gbc);
        
        txtCedula = new JTextField(15);
        gbc.gridx = 1;
        panelTop.add(txtCedula, gbc);
        
        btnBuscarHistorial = new JButton("Ver Todo el Historial");
        gbc.gridx = 2;
        panelTop.add(btnBuscarHistorial, gbc);

        // --- BUSCADOR 2: POR ID FACTURA ---
        gbc.gridx = 0; gbc.gridy = 1;
        panelTop.add(new JLabel("Por ID Factura:"), gbc);
        
        txtIdFactura = new JTextField(15);
        gbc.gridx = 1;
        panelTop.add(txtIdFactura, gbc);
        
        btnBuscarFacturaID = new JButton("Buscar Factura Específica");
        gbc.gridx = 2;
        panelTop.add(btnBuscarFacturaID, gbc);
        
        add(panelTop, BorderLayout.NORTH);

        // 2. Panel Central (Tabla)
        String[] columnas = {"ID Factura", "Cliente", "Fecha", "Forma Pago", "Total", "Items"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaFacturas = new JTable(modeloTabla);
        add(new JScrollPane(tablaFacturas), BorderLayout.CENTER);

        // 3. Acciones de Botones
        btnBuscarHistorial.addActionListener((ActionEvent e) -> buscarHistorial());
        btnBuscarFacturaID.addActionListener((ActionEvent e) -> buscarFacturaPorID());
    }

    private void buscarHistorial() {
        String cedula = txtCedula.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula.");
            return;
        }
        try {
            modeloTabla.setRowCount(0); // Limpiar tabla
            List<Factura> historial = service.obtenerHistorialCompras(cedula);

            if (historial == null || historial.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay compras para este cliente.");
                return;
            }
            for (Factura f : historial) agregarFilaTabla(f);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void buscarFacturaPorID() {
        String idStr = txtIdFactura.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID de la factura (Número).");
            return;
        }
        try {
            int id = Integer.parseInt(idStr);
            modeloTabla.setRowCount(0); // Limpiar tabla
            
            // LLAMADA NUEVA AL SERVICIO
            Factura factura = service.buscarFacturaPorId(id);

            if (factura == null) {
                JOptionPane.showMessageDialog(this, "No existe la factura #" + id);
                return;
            }
            agregarFilaTabla(factura); // Mostramos la factura encontrada
            
        } catch (NumberFormatException nfe) {
             JOptionPane.showMessageDialog(this, "El ID debe ser un número entero.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void agregarFilaTabla(Factura f) {
        int cantItems = 0;
        if (f.getDetalles() != null) cantItems = f.getDetalles().size();

        // Intentamos obtener el nombre del cliente si viene en el objeto, si no, mostramos la cédula
        String clienteInfo = "C.I. " + (f.getCliente() != null ? f.getCliente().getCedula() : "N/A");

        Object[] fila = {
            f.getIdFactura(),
            clienteInfo,
            f.getFecha(), 
            f.getFormaPago(),
            "$" + f.getTotal(),
            cantItems + " productos"
        };
        modeloTabla.addRow(fila);
    }
}