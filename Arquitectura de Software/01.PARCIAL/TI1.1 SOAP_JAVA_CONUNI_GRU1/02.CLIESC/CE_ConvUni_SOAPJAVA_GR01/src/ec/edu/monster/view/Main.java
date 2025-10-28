package ec.edu.monster.view;

import javax.swing.SwingUtilities;
import ec.edu.monster.view.LoginFrame;

public class Main {
    public static void main(String[] args) {
        // Arrancar la UI en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
