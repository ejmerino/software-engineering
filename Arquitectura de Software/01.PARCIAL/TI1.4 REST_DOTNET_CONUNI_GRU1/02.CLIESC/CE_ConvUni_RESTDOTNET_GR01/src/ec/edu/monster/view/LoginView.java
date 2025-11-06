package ec.edu.monster.view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    public JTextField usernameField;
    public JPasswordField passwordField;
    public JButton loginButton;
    public JLabel errorLabel;

    public LoginView() {
        setTitle("Login - Conversión de Unidades");
        setSize(400, 550); // tamaño fijo
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Imagen superior
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("monster1.jpg"));
        Image img = icon.getImage().getScaledInstance(300, 180, Image.SCALE_SMOOTH); // redimensionar imagen
        imageLabel.setIcon(new ImageIcon(img));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(400, 200));
        add(imageLabel, BorderLayout.NORTH);

        // Panel central con campos
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Usuario
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridy++;
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(250, 30));
        panel.add(usernameField, gbc);

        // Contraseña
        gbc.gridy++;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridy++;
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 30));
        panel.add(passwordField, gbc);

        // Botón Login
        gbc.gridy++;
        loginButton = new JButton("Entrar");
        loginButton.setPreferredSize(new Dimension(200, 35));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        panel.add(loginButton, gbc);

        // Mensaje de error
        gbc.gridy++;
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel, gbc);

        add(panel, BorderLayout.CENTER);

        // Enter dispara login
        getRootPane().setDefaultButton(loginButton);
    }
}
