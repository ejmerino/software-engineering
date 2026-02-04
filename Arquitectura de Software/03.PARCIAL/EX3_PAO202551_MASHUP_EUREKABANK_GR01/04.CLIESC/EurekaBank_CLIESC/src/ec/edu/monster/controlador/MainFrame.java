package ec.edu.monster.controlador;

import ec.edu.monster.stilos.BackgroundPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final String LOGIN = "login";
    public static final String MENU  = "menu";
    public static final String MOVS  = "movs";
    public static final String DEP   = "dep";
    public static final String RET   = "ret";
    public static final String TRF   = "trf";

    private final CardLayout cards = new CardLayout();
    private final JPanel stack = new JPanel(cards);

    public MainFrame() {
        setTitle("EurekaBank - Cliente de Escritorio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 640);
        setLocationRelativeTo(null);

        BackgroundPanel bg = new BackgroundPanel("/ec/edu/monster/stilos/monster.jpeg");
        bg.add(stack, BorderLayout.CENTER);
        setContentPane(bg);

        stack.setOpaque(false);
        stack.add(new LoginPanel(this), LOGIN);
        stack.add(new MenuPanel(this),  MENU);
        stack.add(new MovimientosPanel(this), MOVS);
        stack.add(new DepositoPanel(this),    DEP);
        stack.add(new RetiroPanel(this),      RET);
        stack.add(new TransferenciaPanel(this), TRF);

        showCard(LOGIN);
    }

    public void showCard(String name) {
        cards.show(stack, name);
    }
}
