package ec.edu.monster.view;

import javax.swing.*;
import java.awt.*;
import ec.edu.monster.controller.LoginController;

public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Login Cliente SOAP");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con fondo degradado
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 144, 255),
                                                     0, getHeight(), new Color(135, 206, 250));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(null);

        // Logo Monsters Inc escalado
        ImageIcon originalIcon = new ImageIcon("src/ec/edu/monster/view/monstersinc.jpg");
        Image scaledImg = originalIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));
        logoLabel.setBounds(100, 20, 300, 200);
        panel.add(logoLabel);

        // Usuario
        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userLabel.setBounds(50, 250, 80, 25);
        panel.add(userLabel);

        userField = new JTextField();
        userField.setBounds(150, 250, 280, 30);
        userField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(userField);

        // Contraseña
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passLabel.setBounds(50, 300, 100, 25);
        panel.add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(150, 300, 280, 30);
        passField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(passField);

        // Botón login
        loginButton = new JButton("Ingresar");
        loginButton.setBounds(180, 370, 140, 40);
        loginButton.setBackground(new Color(255, 165, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.add(loginButton);

        add(panel);

        LoginController controller = new LoginController(this);
        loginButton.addActionListener(e -> controller.validarLogin());
    }

    public String getUsuario() { return userField.getText(); }
    public String getContraseña() { return new String(passField.getPassword()); }

    // Mensaje de error o info más amigable
    public void mostrarMensaje(String msg, boolean exito) {
        if(exito) {
            JOptionPane.showMessageDialog(this, msg, "Bienvenido!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
