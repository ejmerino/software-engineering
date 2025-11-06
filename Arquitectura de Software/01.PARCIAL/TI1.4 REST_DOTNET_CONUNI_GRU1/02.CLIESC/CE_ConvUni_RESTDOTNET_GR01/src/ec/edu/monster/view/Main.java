package ec.edu.monster.view;

import ec.edu.monster.controller.LoginController;
import ec.edu.monster.controller.ConversionController;
import ec.edu.monster.model.ConversionModel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // Ejecutar en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            // Crear LoginView
            LoginView loginView = new LoginView();
            loginView.setVisible(true);

            // Controlador de login
            LoginController loginController = new LoginController(loginView);

            // Listener de login exitoso
            loginController.setLoginSuccessListener(() -> {
                loginView.dispose(); // cerrar ventana de login

                // Crear vista de conversión
                ConversionView conversionView = new ConversionView();
                conversionView.setVisible(true);

                // Crear modelo y controlador de conversión
                ConversionModel model = new ConversionModel();
                ConversionController conversionController = new ConversionController(conversionView, model);

                // Inicializar combobox de unidades al abrir
                conversionController.updateUnits();
            });
        });
    }
}
