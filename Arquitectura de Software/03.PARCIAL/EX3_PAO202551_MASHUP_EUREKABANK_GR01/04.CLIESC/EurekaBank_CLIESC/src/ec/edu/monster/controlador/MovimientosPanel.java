package ec.edu.monster.controlador;

import ec.edu.monster.modelo.Movimiento;
import ec.edu.monster.servicio.EurekaServiceClient;
import ec.edu.monster.stilos.Styles;
import ec.edu.monster.stilos.UiTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MovimientosPanel extends JPanel {
    private final MainFrame app;
    private final JTextField txtCuenta = Styles.field();
    private final JLabel lblSaldo = new JLabel("Saldo actual: $0.00");
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Fecha","Tipo","Importe","Saldo"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    public MovimientosPanel(MainFrame app) {
        this.app = app;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = Styles.card(16);
        card.setPreferredSize(new Dimension(900, 480));

        JPanel top = Styles.row(8);
        top.add(new JLabel("Cuenta:"));
        txtCuenta.setColumns(12);
        top.add(txtCuenta);

        JButton btn = Styles.primary("Consultar");
        btn.addActionListener(e -> consultar());
        JButton back = Styles.ghost("← Regresar");
        back.addActionListener(e -> app.showCard(MainFrame.MENU));
        lblSaldo.setFont(lblSaldo.getFont().deriveFont(Font.BOLD));
        top.add(btn); top.add(back); top.add(lblSaldo);

        JTable table = new JTable(model);
        UiTable.setup(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(110); // Fecha
        table.getColumnModel().getColumn(1).setPreferredWidth(240); // Tipo

        JScrollPane sp = new JScrollPane(table);

        JPanel content = new JPanel(new BorderLayout(8,8));
        content.setOpaque(false);
        content.add(top, BorderLayout.NORTH);
        content.add(sp,  BorderLayout.CENTER);

        card.add(content, BorderLayout.CENTER);
        card.setMaximumSize(card.getPreferredSize());
        add(card);
    }

    private String fmt(double v) { return String.format("%.2f", v); }

    // Convierte "2008-01-06 00:00:00.0" -> "2008-01-06"
    private String soloFecha(String s) {
        if (s == null) return "";
        int i = s.indexOf(' ');
        if (i > 0) return s.substring(0, i);
        int t = s.indexOf('T');
        if (t > 0) return s.substring(0, t);
        try {
            Date d = javax.xml.bind.DatatypeConverter.parseDateTime(s).getTime();
            return new SimpleDateFormat("yyyy-MM-dd").format(d);
        } catch(Exception ignore) { return s; }
    }

    private void consultar() {
        String cuenta = txtCuenta.getText().trim();
        if (cuenta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa una cuenta.");
            return;
        }
        model.setRowCount(0);
        try {
            EurekaServiceClient api = new EurekaServiceClient();
            List<Movimiento> list = api.movimientos(cuenta);

            double saldoActual = 0.0;
            if (!list.isEmpty()) {
                Movimiento first = list.get(0); // más reciente
                Double s = first.getSaldo() != null ? first.getSaldo() : first.getSaldoCalculado();
                if (s != null) saldoActual = s;
            }
            lblSaldo.setText("Saldo actual: $" + fmt(saldoActual));

            for (Movimiento m : list) {
                Double saldo = (m.getSaldo() != null) ? m.getSaldo() : m.getSaldoCalculado();
                String s = (saldo == null) ? "" : fmt(saldo);
                model.addRow(new Object[]{ soloFecha(m.getFecha()), m.getTipo(), fmt(m.getImporte()), s });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error consultando movimientos:\n" + ex.getMessage());
        }
    }
}
