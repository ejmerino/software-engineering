package ec.edu.monster.view;

import javax.swing.*;
import java.awt.*;

public class ConversionView extends JFrame {
    public JComboBox<String> categoryBox, fromBox, toBox;
    public JTextField valueField;
    public JButton convertButton;
    public JLabel resultLabel;

    public ConversionView() {
        setTitle("Conversión de Unidades");
        setSize(600, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // Imagen superior
        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("monster2.jpg"));
        Image img = icon.getImage().getScaledInstance(550, 220, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(600, 230));
        add(imageLabel, BorderLayout.NORTH);

        // Panel central
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20); // espacios consistentes
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        Font labelFont = new Font("Arial", Font.PLAIN, 15);

        // Categoría
        JLabel categoryLabel = new JLabel("Categoría:");
        categoryLabel.setFont(labelFont);
        categoryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panel.add(categoryLabel, gbc);
        categoryBox = new JComboBox<>(new String[]{"Seleccione","mass","length","temperature"});
        categoryBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(categoryBox, gbc);

        // De
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel fromLabel = new JLabel("De:");
        fromLabel.setFont(labelFont);
        fromLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.weightx = 0.3;
        panel.add(fromLabel, gbc);
        fromBox = new JComboBox<>();
        fromBox.addItem("Seleccione una categoría");
        fromBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(fromBox, gbc);

        // A
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel toLabel = new JLabel("A:");
        toLabel.setFont(labelFont);
        toLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.weightx = 0.3;
        panel.add(toLabel, gbc);
        toBox = new JComboBox<>();
        toBox.addItem("Seleccione una categoría");
        toBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(toBox, gbc);

        // Valor
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel valueLabel = new JLabel("Valor:");
        valueLabel.setFont(labelFont);
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.weightx = 0.3;
        panel.add(valueLabel, gbc);
        valueField = new JTextField();
        valueField.setPreferredSize(new Dimension(200, 35));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(valueField, gbc);

        // Botón Convertir
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        convertButton = new JButton("Convertir");
        convertButton.setPreferredSize(new Dimension(220, 45));
        convertButton.setBackground(new Color(70, 130, 180));
        convertButton.setForeground(Color.WHITE);
        convertButton.setFocusPainted(false);
        panel.add(convertButton, gbc);

        // Espacio
        gbc.gridy++;
        panel.add(Box.createRigidArea(new Dimension(0, 20)), gbc);

        // Resultado
        gbc.gridy++;
        resultLabel = new JLabel("<html> </html>", SwingConstants.CENTER);
        resultLabel.setForeground(new Color(0, 102, 204));
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultLabel.setPreferredSize(new Dimension(550, 100));
        panel.add(resultLabel, gbc);

        add(panel, BorderLayout.CENTER);
    }
}
