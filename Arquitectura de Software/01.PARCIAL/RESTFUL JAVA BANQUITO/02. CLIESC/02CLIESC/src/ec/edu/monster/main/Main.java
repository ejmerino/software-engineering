package ec.edu.monster.main;

import com.formdev.flatlaf.FlatLightLaf; // <-- 1. IMPORTA EL TEMA CLARO
// import com.formdev.flatlaf.FlatDarkLaf; // (Opcional: Si quieres tema oscuro)
import ec.edu.monster.controller.LoginController;
import ec.edu.monster.view.LoginView;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        
        // --- ESTO HACE QUE SE VEA "LINDO Y MODERNO" ---
        try {
            // Configura el "skin" de FlatLaf (Tema Claro)
            FlatLightLaf.setup(); 
            
            // (Si prefieres tema oscuro, descomenta la siguiente línea y comenta la de arriba)
            // FlatDarkLaf.setup();
            
        } catch (Exception e) {
            // Si FlatLaf falla, usa el "look and feel" por defecto
            System.err.println("Falló al inicializar FlatLaf. Usando Look&Feel por defecto.");
            try {
                 UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                // No se puede hacer nada
            }
        }
        // ------------------------------------------------
        
        // Inicia la ventana de Login
        // Usamos invokeLater para asegurar que Swing inicie en el hilo correcto
        java.awt.EventQueue.invokeLater(() -> {
            LoginView loginView = new LoginView();
            
            // El controlador se encarga de hacerla visible
            new LoginController(loginView);
            
            loginView.setLocationRelativeTo(null); // Centra la ventana
            loginView.setVisible(true);
        });
    }
}