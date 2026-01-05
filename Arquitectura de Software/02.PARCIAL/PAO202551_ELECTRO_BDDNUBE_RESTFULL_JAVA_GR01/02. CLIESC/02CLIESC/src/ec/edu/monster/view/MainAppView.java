package ec.edu.monster.view;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainAppView extends JFrame {

    // --- Componentes ---
    private JTabbedPane tabbedPane;
    private JButton btnProcesarCompra, btnBuscarCliente, btnBuscarProducto, btnAgregarAlCarrito, btnQuitarItem, btnRegistrarCliente;
    private JComboBox<String> cmbVentaFormaPago;
    private JTextField txtVentaCantidad, txtVentaCedula, txtVentaCedulaNombre, txtVentaCuotas, txtVentaProductoNombre;
    private JTextArea txtFacturaResumen;
    private JLabel lblVentaIdProducto;
    private JTable tblCarrito;
    private JLabel lblImagenProducto;
    
    // Componentes de Registro Cliente
    private JTextField txtCliApellidos, txtCliCedula, txtCliDireccion, txtCliEmail, txtCliNombres, txtCliTelefono;
    
    // Componentes de Consultas Generales (Para mantener compatibilidad con tu Controller)
    private JButton btnBuscarAmortizacion, btnListarCatalogo;
    private JTable tblAmortizacion, tblCatalogo;
    private JTextField txtConsultaIdCredito;

    public MainAppView() {
        // Configuramos el tema visual
        try {
            FlatMacDarkLaf.setup();
        } catch(Exception ex) {
            System.err.println("Error al cargar FlatLaf");
        }
        initComponents();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Comercializadora - Cliente Escritorio");
        setPreferredSize(new Dimension(1200, 720));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_AREA_ALIGNMENT, "center");

        // TAB 1: FACTURACIÓN (Se mantiene igual)
        tabbedPane.addTab("Facturación y Clientes", createBillingTab());
        
        // TAB 2: CONSULTAS GENERALES (Catálogo y Amortización)
        // Mantenemos esto porque tu MainAppController usa estos botones y tablas.
        tabbedPane.addTab("Catálogo y Amortización", createConsultationTab());
        
        // TAB 3: HISTORIAL (NUEVO)
        // Aquí agregamos el panel nuevo que creamos en el paso anterior.
        // Como está en el mismo paquete (ec.edu.monster.view), no requiere import extra si está compilado.
        tabbedPane.addTab("Historial de Compras", new ConsultasPanel());

        add(tabbedPane, BorderLayout.CENTER);
        pack();
    }

    private JComponent createBillingTab() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createClientRegistrationPanel(),
                createSalePanel());
        splitPane.setDividerLocation(380);
        splitPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        return splitPane;
    }

    private JPanel createClientRegistrationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Cliente"));
        panel.setPreferredSize(new Dimension(360, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"Cédula:", "Nombres:", "Apellidos:", "Dirección:", "Teléfono:", "Email:"};
        txtCliCedula = new JTextField();
        txtCliNombres = new JTextField();
        txtCliApellidos = new JTextField();
        txtCliDireccion = new JTextField();
        txtCliTelefono = new JTextField();
        txtCliEmail = new JTextField();
        JTextField[] fields = {txtCliCedula, txtCliNombres, txtCliApellidos, txtCliDireccion, txtCliTelefono, txtCliEmail};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.2;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1; gbc.gridy = i; gbc.weightx = 0.8;
            panel.add(fields[i], gbc);
        }

        btnRegistrarCliente = new JButton("Registrar Cliente");
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 8, 8, 8);
        panel.add(btnRegistrarCliente, gbc);

        gbc.gridy = labels.length + 1; gbc.weighty = 1.0;
        panel.add(new JLabel(), gbc);

        return panel;
    }

    private JPanel createSalePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Nueva Venta"));

        panel.add(createSaleFormPanel(), BorderLayout.NORTH);

        tblCarrito = new JTable(new DefaultTableModel(new Object[]{"ID", "Nombre", "Cant.", "Precio U.", "Subtotal"}, 0));
        JScrollPane scrollCarrito = new JScrollPane(tblCarrito);
        scrollCarrito.putClientProperty(FlatClientProperties.STYLE, "arc: 8");
        panel.add(scrollCarrito, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnProcesarCompra = new JButton("Procesar Compra");
        btnProcesarCompra.setFont(btnProcesarCompra.getFont().deriveFont(Font.BOLD));
        bottomPanel.add(btnProcesarCompra);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        txtFacturaResumen = new JTextArea();
        lblVentaIdProducto = new JLabel();

        return panel;
    }
    
    private JPanel createSaleFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; panel.add(new JLabel("Cliente:"), gbc);
        txtVentaCedula = new JTextField();
        txtVentaCedula.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Cédula del cliente");
        gbc.gridx = 1; gbc.weightx = 0.5; panel.add(txtVentaCedula, gbc);
        
        txtVentaCedulaNombre = new JTextField();
        txtVentaCedulaNombre.setEditable(false);
        txtVentaCedulaNombre.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre del Cliente");
        gbc.gridx = 2; gbc.weightx = 0.5; panel.add(txtVentaCedulaNombre, gbc);
        
        btnBuscarCliente = new JButton("Buscar");
        gbc.gridx = 3; gbc.weightx = 0; panel.add(btnBuscarCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Producto:"), gbc);
        
        lblImagenProducto = new JLabel();
        lblImagenProducto.setPreferredSize(new Dimension(48, 48));
        lblImagenProducto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblImagenProducto.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagenProducto.setText("Img");
        gbc.gridx = 1; panel.add(lblImagenProducto, gbc);
        
        txtVentaProductoNombre = new JTextField();
        txtVentaProductoNombre.setEditable(false);
        txtVentaProductoNombre.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre del Producto");
        gbc.gridx = 2; panel.add(txtVentaProductoNombre, gbc);
        
        btnBuscarProducto = new JButton("Catálogo");
        gbc.gridx = 3; panel.add(btnBuscarProducto, gbc);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actionPanel.add(new JLabel("Cantidad:"));
        txtVentaCantidad = new JTextField("1", 5);
        actionPanel.add(txtVentaCantidad);
        
        btnAgregarAlCarrito = new JButton("Añadir");
        actionPanel.add(btnAgregarAlCarrito);

        btnQuitarItem = new JButton("Quitar");
        actionPanel.add(btnQuitarItem);
        
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(actionPanel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth=1; panel.add(new JLabel("Pago:"), gbc);
        
        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        paymentPanel.add(new JLabel("Forma:"));
        cmbVentaFormaPago = new JComboBox<>(new String[]{"Efectivo", "Credito"});
        paymentPanel.add(cmbVentaFormaPago);

        paymentPanel.add(new JLabel("Cuotas:"));
        txtVentaCuotas = new JTextField("0", 5);
        txtVentaCuotas.setEnabled(false);
        paymentPanel.add(txtVentaCuotas);
        
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 3; gbc.anchor = GridBagConstraints.WEST;
        panel.add(paymentPanel, gbc);

        return panel;
    }

    // Este método se mantiene para que el Controller no falle al buscar btnListarCatalogo, etc.
    private JComponent createConsultationTab() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel catalogoPanel = new JPanel(new BorderLayout(5, 5));
        catalogoPanel.setBorder(BorderFactory.createTitledBorder("Catálogo de Productos"));
        btnListarCatalogo = new JButton("Mostrar Catálogo");
        tblCatalogo = new JTable(new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Precio"}, 0));
        catalogoPanel.add(btnListarCatalogo, BorderLayout.NORTH);
        catalogoPanel.add(new JScrollPane(tblCatalogo), BorderLayout.CENTER);

        JPanel amortizacionPanel = new JPanel(new BorderLayout(5, 5));
        amortizacionPanel.setBorder(BorderFactory.createTitledBorder("Consulta de Amortización"));
        
        JPanel buscaAmortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtConsultaIdCredito = new JTextField(15);
        txtConsultaIdCredito.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ID del Crédito (BanQuito)");
        btnBuscarAmortizacion = new JButton("Buscar");
        buscaAmortPanel.add(new JLabel("Crédito:"));
        buscaAmortPanel.add(txtConsultaIdCredito);
        buscaAmortPanel.add(btnBuscarAmortizacion);
        
        tblAmortizacion = new JTable(new DefaultTableModel(new Object[]{"Cuota", "Fecha Pago", "Valor Cuota", "Interés", "Capital", "Saldo"}, 0));
        amortizacionPanel.add(buscaAmortPanel, BorderLayout.NORTH);
        amortizacionPanel.add(new JScrollPane(tblAmortizacion), BorderLayout.CENTER);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, catalogoPanel, amortizacionPanel);
        splitPane.setDividerLocation(350);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        return mainPanel;
    }

    public int showInvoiceConfirmationDialog(String resumen) {
        JTextArea areaResumen = new JTextArea(resumen);
        areaResumen.setEditable(false);
        areaResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaResumen);
        scrollPane.setPreferredSize(new Dimension(450, 400));

        return JOptionPane.showConfirmDialog(
                this,
                scrollPane,
                "Confirmar Compra",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    // --- GETTERS (CRUCIALES PARA TU CONTROLADOR) ---
    public JButton getBtnProcesarCompra() { return btnProcesarCompra; }
    public JButton getBtnRegistrarCliente() { return btnRegistrarCliente; }
    public JComboBox<String> getCmbVentaFormaPago() { return cmbVentaFormaPago; }
    public JTextArea getTxtFacturaResumen() { return txtFacturaResumen; }
    public JTextField getTxtCliApellidos() { return txtCliApellidos; }
    public JTextField getTxtCliCedula() { return txtCliCedula; }
    public JTextField getTxtCliDireccion() { return txtCliDireccion; }
    public JTextField getTxtCliEmail() { return txtCliEmail; }
    public JTextField getTxtCliNombres() { return txtCliNombres; }
    public JTextField getTxtCliTelefono() { return txtCliTelefono; }
    public JTextField getTxtVentaCantidad() { return txtVentaCantidad; }
    public JTextField getTxtVentaCedula() { return txtVentaCedula; }
    public JTextField getTxtVentaCedulaNombre() { return txtVentaCedulaNombre; }
    public JTextField getTxtVentaCuotas() { return txtVentaCuotas; }
    public JTextField getTxtVentaProductoNombre() { return txtVentaProductoNombre; }
    public JLabel getLblVentaIdProducto() { return lblVentaIdProducto; }
    public JButton getBtnBuscarAmortizacion() { return btnBuscarAmortizacion; }
    public JButton getBtnBuscarCliente() { return btnBuscarCliente; }
    public JButton getBtnBuscarProducto() { return btnBuscarProducto; }
    public JButton getBtnListarCatalogo() { return btnListarCatalogo; }
    public JTabbedPane getTabbedPane() { return tabbedPane; }
    public JTable getTblAmortizacion() { return tblAmortizacion; }
    public JTable getTblCatalogo() { return tblCatalogo; }
    public JTextField getTxtConsultaIdCredito() { return txtConsultaIdCredito; }
    public JButton getBtnAgregarAlCarrito() { return btnAgregarAlCarrito; }
    public JButton getBtnQuitarItem() { return btnQuitarItem; }
    public JTable getTblCarrito() { return tblCarrito; }
    public JLabel getLblImagenProducto() { return lblImagenProducto; }
}