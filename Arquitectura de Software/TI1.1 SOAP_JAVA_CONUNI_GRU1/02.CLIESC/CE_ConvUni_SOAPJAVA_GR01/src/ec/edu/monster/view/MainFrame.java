package ec.edu.monster.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import ec.edu.monster.controller.MainController;

public class MainFrame extends JFrame {

    private JComboBox<String> comboTipo;
    private JTextField valorField;
    private JButton convertirButton;
    private JButton cerrarSesionButton;
    private JLabel resultadoLabel;
    private JLabel unidadLabel;

    private Map<String, String> tipoMap = new HashMap<>();
    private Map<String, String> unidadFinalMap = new HashMap<>();

    public MainFrame() {
        setTitle("Conversor de Unidades - Cliente SOAP");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0,0,new Color(30,144,255),
                        0,getHeight(),new Color(135,206,250));
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        panel.setLayout(null);

        // Logo MonstersInc
        ImageIcon originalIcon = new ImageIcon("src/ec/edu/monster/view/monstersinc2.jpg");
        int imgWidth = originalIcon.getIconWidth();
        int imgHeight = originalIcon.getIconHeight();
        int maxWidth = 600;
        int maxHeight = 200;
        double scale = Math.min((double) maxWidth / imgWidth, (double) maxHeight / imgHeight);
        Image scaledImg = originalIcon.getImage().getScaledInstance((int)(imgWidth*scale), (int)(imgHeight*scale), Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));
        logoLabel.setBounds((700 - (int)(imgWidth*scale))/2, 10, (int)(imgWidth*scale), (int)(imgHeight*scale));
        panel.add(logoLabel);

        // Inicializamos mapas
        inicializarMapas();

        // Tipo de conversión
        JLabel tipoLabel = new JLabel("Tipo de conversión:");
        tipoLabel.setForeground(Color.WHITE);
        tipoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        tipoLabel.setBounds(50, 230, 200, 25);
        panel.add(tipoLabel);

        comboTipo = new JComboBox<>(tipoMap.keySet().toArray(new String[0]));
        comboTipo.setBounds(250, 230, 300, 30);
        panel.add(comboTipo);

        // Valor
        JLabel valorLabel = new JLabel("Valor:");
        valorLabel.setForeground(Color.WHITE);
        valorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        valorLabel.setBounds(50, 280, 100, 25);
        panel.add(valorLabel);

        valorField = new JTextField();
        valorField.setBounds(250, 280, 300, 30);
        panel.add(valorField);

        // Botón convertir
        convertirButton = new JButton("Convertir");
        convertirButton.setBounds(250, 330, 150, 40);
        convertirButton.setBackground(new Color(255, 165, 0));
        convertirButton.setForeground(Color.WHITE);
        convertirButton.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(convertirButton);

        // Botón cerrar sesión
        cerrarSesionButton = new JButton("Cerrar sesión");
        cerrarSesionButton.setBounds(550, 10, 120, 30);
        cerrarSesionButton.setBackground(new Color(220, 20, 60));
        cerrarSesionButton.setForeground(Color.WHITE);
        cerrarSesionButton.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(cerrarSesionButton);

        cerrarSesionButton.addActionListener(e -> {
            int resp = JOptionPane.showConfirmDialog(this, "¿Deseas cerrar sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION);
            if(resp == JOptionPane.YES_OPTION){
                this.dispose();
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            }
        });

        // Resultado
        resultadoLabel = new JLabel("Resultado:");
        resultadoLabel.setForeground(Color.WHITE);
        resultadoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultadoLabel.setBounds(50, 400, 200, 30);
        panel.add(resultadoLabel);

        unidadLabel = new JLabel("");
        unidadLabel.setForeground(Color.WHITE);
        unidadLabel.setFont(new Font("Arial", Font.BOLD, 18));
        unidadLabel.setBounds(260, 400, 50, 30);
        panel.add(unidadLabel);

        add(panel);

        // Controlador
        new MainController(this);

        // Enter también dispara conversión
        valorField.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    convertirButton.doClick();
                }
            }
        });
    }

    private void inicializarMapas(){
        tipoMap.put("Metros a Kilómetros", "metros_a_kilometros");
        tipoMap.put("Kilómetros a Metros", "kilometros_a_metros");
        tipoMap.put("Centímetros a Metros", "centimetros_a_metros");
        tipoMap.put("Gramos a Kilogramos", "gramos_a_kilogramos");
        tipoMap.put("Kilogramos a Gramos", "kilogramos_a_gramos");
        tipoMap.put("Libras a Kilogramos", "libras_a_kilogramos");
        tipoMap.put("Celsius a Fahrenheit", "celsius_a_fahrenheit");
        tipoMap.put("Fahrenheit a Celsius", "fahrenheit_a_celsius");
        tipoMap.put("Celsius a Kelvin", "celsius_a_kelvin");

        unidadFinalMap.put("metros_a_kilometros", "km");
        unidadFinalMap.put("kilometros_a_metros", "m");
        unidadFinalMap.put("centimetros_a_metros", "m");
        unidadFinalMap.put("gramos_a_kilogramos", "kg");
        unidadFinalMap.put("kilogramos_a_gramos", "g");
        unidadFinalMap.put("libras_a_kilogramos", "kg");
        unidadFinalMap.put("celsius_a_fahrenheit", "°F");
        unidadFinalMap.put("fahrenheit_a_celsius", "°C");
        unidadFinalMap.put("celsius_a_kelvin", "K");
    }

    public String getTipoSeleccionado(){
        String amigable = (String) comboTipo.getSelectedItem();
        return tipoMap.get(amigable);
    }

    public Double getValorIngresado(){
        try{
            String text = valorField.getText().trim();
            if(text.isEmpty()) throw new NumberFormatException();
            return Double.parseDouble(text);
        }catch(NumberFormatException e){
            mostrarError("Por favor ingresa un valor numérico válido.");
            return null;
        }
    }

    public void mostrarResultado(double r){
        String tipo = getTipoSeleccionado();
        resultadoLabel.setText("Resultado: " + r);
        unidadLabel.setText(unidadFinalMap.get(tipo));
    }

    public void mostrarError(String msg){
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setConvertAction(java.awt.event.ActionListener listener){
        convertirButton.addActionListener(listener);
    }
}
