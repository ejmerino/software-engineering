package ec.edu.monster.controller;

import javax.swing.*;
import ec.edu.monster.view.LoginFrame;
import ec.edu.monster.view.MainFrame;

public class LoginController {

    private LoginFrame view;

    public LoginController(LoginFrame view) {
        this.view = view;
    }

    public void validarLogin() {
        if(view.getUsuario().equals("MONSTER") && view.getContraseña().equals("MONSTER9")) {
            JOptionPane.showMessageDialog(null, "Login correcto!");
            view.dispose();
            new MainFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
