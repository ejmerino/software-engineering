package eurekabank_cliesc;

import javax.swing.SwingUtilities;
import ec.edu.monster.controlador.MainFrame;

public class EurekaBank_CLIESC {

    public static void main(String[] args) {
        // Usamos Runnable para compatibilidad con Java 8 (sin lambda)
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
