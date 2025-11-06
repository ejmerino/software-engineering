package ec.edu.monster.controller;

import ec.edu.monster.view.LoginView;
import javax.swing.*;
import java.awt.event.ActionListener;

public class LoginController {
    private final LoginView view;
    private Runnable loginSuccessListener; // listener cuando el login es exitoso

    public LoginController(LoginView view) {
        this.view = view;

        // Acción del botón Login
        this.view.loginButton.addActionListener(e -> login());
    }

    // Método para asignar el listener de éxito
    public void setLoginSuccessListener(Runnable listener){
        this.loginSuccessListener = listener;
    }

    private void login() {
        String user = view.usernameField.getText().trim();
        String pass = new String(view.passwordField.getPassword()).trim();

        if(user.equals("MONSTER") && pass.equals("MONSTER9")){
            JOptionPane.showMessageDialog(view, "¡Login exitoso!");
            if(loginSuccessListener != null){
                loginSuccessListener.run(); // dispara transición a ConversionView
            }
        } else {
            view.errorLabel.setText("Usuario o contraseña incorrectos.");
        }
    }
}
