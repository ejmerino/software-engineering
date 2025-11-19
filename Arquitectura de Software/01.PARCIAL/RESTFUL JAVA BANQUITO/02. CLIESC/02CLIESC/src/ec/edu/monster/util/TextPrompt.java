package ec.edu.monster.util;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class TextPrompt extends JLabel implements FocusListener, DocumentListener {
    
    private final JTextComponent component;
    
    public TextPrompt(String text, JTextComponent component) {
        this.component = component;
        setText(text);
        setFont(component.getFont());
        setForeground(new Color(200, 200, 200, 150)); // Color del placeholder
        setBorder(component.getBorder());
        setHorizontalAlignment(JLabel.LEADING);

        component.addFocusListener(this);
        component.getDocument().addDocumentListener(this);
        
        // Coloca el JLabel sobre el JTextComponent
        component.setLayout(new BorderLayout());
        component.add(this);
        checkForPrompt();
    }

    private void checkForPrompt() {
        // Muestra u oculta el prompt basado en si hay texto
        setVisible(component.getText().length() == 0);
    }
    
    // --- FocusListener ---
    @Override
    public void focusGained(FocusEvent e) {
        checkForPrompt();
    }
    @Override
    public void focusLost(FocusEvent e) {
        checkForPrompt();
    }

    // --- DocumentListener ---
    @Override
    public void insertUpdate(DocumentEvent e) {
        checkForPrompt();
    }
    @Override
    public void removeUpdate(DocumentEvent e) {
        checkForPrompt();
    }
    @Override
    public void changedUpdate(DocumentEvent e) {
        // No aplica para texto plano
    }
}