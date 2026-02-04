package ec.edu.monster.controlador;

import ec.edu.monster.stilos.Styles;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final MainFrame app;
    private final JTextField txtUser = Styles.field();
    private final JPasswordField txtPass = Styles.pass();

    public LoginPanel(MainFrame app) {
        this.app = app;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = Styles.card(24);
        card.setPreferredSize(new Dimension(560, 340));

        JPanel inner = new JPanel(new GridBagLayout());
        inner.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        inner.add(Styles.h1("Ingreso"), c);

        c.gridy++; c.gridwidth = 1;
        inner.add(new JLabel("Usuario:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        inner.add(txtUser, c);

        c.gridx = 0; c.gridy++; c.weightx = 0; c.fill = GridBagConstraints.NONE;
        inner.add(new JLabel("Contraseña:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        inner.add(txtPass, c);

        c.gridx = 0; c.gridy++; c.gridwidth = 2; c.weightx = 0; c.fill = GridBagConstraints.NONE;
        JButton btn = Styles.primary("Entrar");
        btn.addActionListener(e -> login());
        JPanel row = Styles.row(8); row.add(btn);
        inner.add(row, c);

        card.add(inner, BorderLayout.CENTER);
        add(card);
    }

    private void login() {
        String u = txtUser.getText().trim();
        if (u.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa un usuario.");
            return;
        }
        Session.login(u);
        app.showCard(MainFrame.MENU);
    }
}
