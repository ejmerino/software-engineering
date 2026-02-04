package ec.edu.monster.controlador;

import ec.edu.monster.stilos.Styles;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private final MainFrame app;
    public MenuPanel(MainFrame app) {
        this.app = app;
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = Styles.card(24);
        card.setPreferredSize(new Dimension(560, 460));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = Styles.h1("Menú Principal");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(16));

        content.add(boton("Consultar movimientos de cuenta", () -> app.showCard(MainFrame.MOVS)));
        content.add(Box.createVerticalStrut(10));
        content.add(boton("Realizar depósito", () -> app.showCard(MainFrame.DEP)));
        content.add(Box.createVerticalStrut(10));
        content.add(boton("Realizar retiro", () -> app.showCard(MainFrame.RET)));
        content.add(Box.createVerticalStrut(10));
        content.add(boton("Realizar transferencia", () -> app.showCard(MainFrame.TRF)));
        content.add(Box.createVerticalStrut(10));
        JButton salir = Styles.ghost("5. Salir");
        salir.setAlignmentX(Component.CENTER_ALIGNMENT);
        salir.addActionListener(e -> { Session.logout(); System.exit(0); });
        content.add(salir);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.add(content, BorderLayout.CENTER);

        card.add(wrap, BorderLayout.CENTER);
        add(card);
    }

    private JComponent boton(String txt, Runnable run) {
        JButton b = Styles.primary(txt);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(420, 42));
        b.setPreferredSize(new Dimension(420, 42));
        b.addActionListener(e -> run.run());
        return b;
    }
}
