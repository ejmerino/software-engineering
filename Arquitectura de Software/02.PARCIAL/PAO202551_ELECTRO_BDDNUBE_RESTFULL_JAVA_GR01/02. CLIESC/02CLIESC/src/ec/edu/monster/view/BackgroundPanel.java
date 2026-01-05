package ec.edu.monster.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
    
    private Image backgroundImage;
    private Color overlayColor; 

    public BackgroundPanel(LayoutManager layout, String imagePath, Color overlayColor) {
        super(layout);
        this.overlayColor = overlayColor;
        this.setBackground(Color.DARK_GRAY);
        
        if (imagePath != null) { 
            try {
                this.backgroundImage = ImageIO.read(getClass().getResource(imagePath));
            } catch (Exception e) { 
                System.err.println("Error: No se pudo cargar la imagen. " + imagePath);
                this.backgroundImage = null;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Dimension size = this.getSize();
            g.drawImage(backgroundImage, 0, 0, size.width, size.height, this);
            
            // Dibuja el overlay semi-transparente para legibilidad
            if (overlayColor != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(overlayColor);
                g2d.fillRect(0, 0, size.width, size.height);
                g2d.dispose();
            }
        }
    }
}