package ec.edu.monster.controlador;

import ec.edu.monster.servicio.EurekaServiceClient;
import ec.edu.monster.stilos.Styles;

import javax.swing.*;
import java.awt.*;

public class RetiroPanel extends JPanel {
    private final MainFrame app;
    private final JTextField txtCuenta  = Styles.field();
    private final JTextField txtImporte = Styles.field();

    public RetiroPanel(MainFrame app) {
        this.app = app;
        setOpaque(false);
        setLayout(new GridBagLayout());

        Dimension campo = new Dimension(220, 30);
        txtCuenta.setPreferredSize(campo);
        txtImporte.setPreferredSize(campo);
        txtCuenta.setMaximumSize(campo);
        txtImporte.setMaximumSize(campo);

        JPanel card = Styles.card(18);
        card.setPreferredSize(new Dimension(560, 260));
        card.setMaximumSize(card.getPreferredSize());

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0; c.weighty = 0;

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        form.add(Styles.h1("Retiro"), c);

        c.gridwidth = 1; c.gridy++;
        form.add(new JLabel("Cuenta:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        form.add(txtCuenta, c);

        c.gridx = 0; c.gridy++; c.fill = GridBagConstraints.NONE; c.weightx = 0;
        form.add(new JLabel("Importe:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        form.add(txtImporte, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.setOpaque(false);
        JButton ok = Styles.primary("Retirar");
        JButton back = Styles.ghost("← Regresar");
        ok.addActionListener(e -> retirar());
        back.addActionListener(e -> app.showCard(MainFrame.MENU));
        buttons.add(ok); buttons.add(back);

        c.gridx = 0; c.gridy++; c.gridwidth = 2;
        form.add(buttons, c);

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(form, BorderLayout.NORTH);

        card.setLayout(new BorderLayout());
        card.add(north, BorderLayout.CENTER);
        add(card);
    }

    private void retirar() {
        String cta = txtCuenta.getText().trim();
        double imp;
        try { imp = Double.parseDouble(txtImporte.getText().trim()); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, "Importe inválido."); return; }
        if (cta.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingresa la cuenta."); return; }

        try {
            EurekaServiceClient api = new EurekaServiceClient();
            boolean ok = api.retiro(cta, imp);
            JOptionPane.showMessageDialog(this, ok ? "Retiro realizado." : "No se pudo retirar.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error:\n" + ex.getMessage());
        }
    }
}
