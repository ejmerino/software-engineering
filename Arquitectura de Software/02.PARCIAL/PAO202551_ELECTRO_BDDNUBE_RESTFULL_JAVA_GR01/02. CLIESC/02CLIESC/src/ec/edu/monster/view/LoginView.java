package ec.edu.monster.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class LoginView extends javax.swing.JFrame {

    // --- CONSTANTES DE DISEÑO ---
    private static final Color COLOR_TEXTO = new Color(240, 240, 240);
    private static final Color COLOR_FONDO_INPUT = new Color(0, 0, 0, 80);
    private static final Color COLOR_BORDE_INPUT = new Color(100, 100, 100);
    private static final Color COLOR_BOTON = new Color(70, 130, 180); // Steel Blue
    private static final Color COLOR_BOTON_HOVER = new Color(60, 110, 150);
    private static final Color COLOR_ERROR = new Color(255, 99, 71); // Tomato Red

    private static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONT_ETIQUETA = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);

    // --- COMPONENTES ---
    private JButton btnIngresar;
    private JLabel lblError;
    private JPasswordField txtPassword;
    private JTextField txtUsuario;

    public LoginView() {
        initComponents();
    }

    // --- GETTERS ---
    public JButton getBtnIngresar() { return btnIngresar; }
    public JLabel getLblError() { return lblError; }
    public JPasswordField getTxtPassword() { return txtPassword; }
    public JTextField getTxtUsuario() { return txtUsuario; }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inicio de Sesión - Comercializadora");
        setMinimumSize(new Dimension(450, 400));
        setResizable(false);

        // --- PANEL DE FONDO ---
        // Un overlay negro semi-transparente para mejorar la legibilidad del texto
        Color overlay = new Color(0, 0, 0, 160);
        BackgroundPanel backgroundPanel = new BackgroundPanel(new GridBagLayout(), "/ec/edu/monster/images/monstersinc1.png", overlay);
        
        // --- PANEL DE FORMULARIO ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Hacerlo transparente para que se vea el fondo
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Espaciado entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- TÍTULO ---
        JLabel lblTitulo = new JLabel("¡Bienvenido, MONSTER S.A.!");
        lblTitulo.setFont(FONT_TITULO);
        lblTitulo.setForeground(COLOR_TEXTO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.insets = new Insets(10, 10, 20, 10); // Más margen inferior para el título
        formPanel.add(lblTitulo, gbc);

        // --- Resetear insets y gridwidth ---
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        // --- ETIQUETA Y CAMPO DE USUARIO ---
        JLabel lblUser = new JLabel("Usuario:");
        lblUser.setFont(FONT_ETIQUETA);
        lblUser.setForeground(COLOR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST; // Alinea la etiqueta a la derecha
        formPanel.add(lblUser, gbc);

        txtUsuario = createStyledTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST; // Alinea el campo a la izquierda
        formPanel.add(txtUsuario, gbc);

        // --- ETIQUETA Y CAMPO DE CONTRASEÑA ---
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(FONT_ETIQUETA);
        lblPass.setForeground(COLOR_TEXTO);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(lblPass, gbc);

        txtPassword = createStyledPasswordField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(txtPassword, gbc);
        
        // --- MENSAJE DE ERROR ---
        lblError = new JLabel(" "); // Espacio para que ocupe lugar y no mueva el layout
        lblError.setFont(FONT_ETIQUETA.deriveFont(12f));
        lblError.setForeground(COLOR_ERROR);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(lblError, gbc);

        // --- BOTÓN DE INGRESAR ---
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(FONT_ETIQUETA);
        btnIngresar.setBackground(COLOR_BOTON);
        btnIngresar.setForeground(COLOR_TEXTO);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIngresar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Efecto Hover para el botón
        btnIngresar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnIngresar.setBackground(COLOR_BOTON_HOVER);
            }
            public void mouseExited(MouseEvent evt) {
                btnIngresar.setBackground(COLOR_BOTON);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 10, 10, 10); // Margen superior para el botón
        formPanel.add(btnIngresar, gbc);

        // Añadir el panel del formulario al panel de fondo (que usa GridBagLayout para centrarlo)
        backgroundPanel.add(formPanel, new GridBagConstraints());
        
        setContentPane(backgroundPanel);
        pack();
        setLocationRelativeTo(null); // Centrar en pantalla
    }
    
    // --- MÉTODOS DE AYUDA PARA CREAR COMPONENTES ESTILIZADOS ---
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15); // Tamaño del campo
        textField.setFont(FONT_INPUT);
        textField.setForeground(COLOR_TEXTO);
        textField.setBackground(COLOR_FONDO_INPUT);
        textField.setCaretColor(COLOR_TEXTO);
        
        // Borde compuesto: uno exterior para margen y uno interior para padding
        Border line = BorderFactory.createLineBorder(COLOR_BORDE_INPUT);
        Border padding = BorderFactory.createEmptyBorder(5, 8, 5, 8);
        textField.setBorder(BorderFactory.createCompoundBorder(line, padding));
        
        return textField;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField passField = new JPasswordField(15);
        passField.setFont(FONT_INPUT);
        passField.setForeground(COLOR_TEXTO);
        passField.setBackground(COLOR_FONDO_INPUT);
        passField.setCaretColor(COLOR_TEXTO);
        
        Border line = BorderFactory.createLineBorder(COLOR_BORDE_INPUT);
        Border padding = BorderFactory.createEmptyBorder(5, 8, 5, 8);
        passField.setBorder(BorderFactory.createCompoundBorder(line, padding));
        
        return passField;
    }
}