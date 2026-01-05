package ec.edu.monster.view;

import ec.edu.monster.model.Electrodomestico;
import java.awt.*;
import java.math.BigDecimal;
import javax.swing.*;

public class ProductoDialog extends JDialog {

    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JTextField txtImagen;
    private boolean confirmado = false;

    public ProductoDialog(java.awt.Frame parent, String titulo) {
        super(parent, titulo, true);
        initComponents();
        setSize(400, 500);
        setLocationRelativeTo(parent);
    }

    public boolean isConfirmado() { return confirmado; }

    // Método para llenar el form si es edición
    public void setProducto(Electrodomestico p) {
        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        txtPrecio.setText(p.getPrecioVenta().toString());
        txtStock.setText(String.valueOf(p.getStock()));
        txtImagen.setText(p.getRutaImagen());
    }

    // Método para sacar los datos del form
    public Electrodomestico getProducto() {
        Electrodomestico p = new Electrodomestico();
        p.setNombre(txtNombre.getText());
        p.setDescripcion(txtDescripcion.getText());
        try {
            p.setPrecioVenta(new BigDecimal(txtPrecio.getText()));
            p.setStock(Integer.parseInt(txtStock.getText()));
        } catch(Exception e) {
            p.setPrecioVenta(BigDecimal.ZERO);
            p.setStock(0);
        }
        p.setRutaImagen(txtImagen.getText());
        return p;
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel pForm = new JPanel(new GridLayout(6, 1, 5, 5));
        pForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtNombre = new JTextField();
        txtDescripcion = new JTextArea(3, 20);
        txtPrecio = new JTextField();
        txtStock = new JTextField();
        txtImagen = new JTextField();

        agregarCampo(pForm, "Nombre:", txtNombre);
        agregarCampo(pForm, "Descripción:", new JScrollPane(txtDescripcion));
        agregarCampo(pForm, "Precio ($):", txtPrecio);
        agregarCampo(pForm, "Stock:", txtStock);
        agregarCampo(pForm, "URL Imagen:", txtImagen);

        add(pForm, BorderLayout.CENTER);

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            confirmado = true;
            dispose();
        });
        btnCancelar.addActionListener(e -> dispose());

        pBtn.add(btnCancelar);
        pBtn.add(btnGuardar);
        add(pBtn, BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel p, String label, Component comp) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.add(new JLabel(label), BorderLayout.NORTH);
        row.add(comp, BorderLayout.CENTER);
        p.add(row);
    }
}