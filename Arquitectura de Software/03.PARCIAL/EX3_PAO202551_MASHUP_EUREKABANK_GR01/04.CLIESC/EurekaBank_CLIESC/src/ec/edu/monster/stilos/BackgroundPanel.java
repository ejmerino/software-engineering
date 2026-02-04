package ec.edu.monster.stilos;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class BackgroundPanel extends JPanel {
    private BufferedImage img;

    public BackgroundPanel(String resourcePath) {
        setLayout(new BorderLayout());
        setOpaque(false);
        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in != null) img = ImageIO.read(in);
        } catch (Exception ignored) {}
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            int w = getWidth(), h = getHeight();
            double rw = w / (double) img.getWidth();
            double rh = h / (double) img.getHeight();
            double r = Math.max(rw, rh); // cover
            int nw = (int) Math.round(img.getWidth() * r);
            int nh = (int) Math.round(img.getHeight() * r);
            int x = (w - nw) / 2;
            int y = (h - nh) / 2;
            g.drawImage(img, x, y, nw, nh, null);

            // velo sutil p/ legibilidad
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(255,255,255,90));
            g2.fillRect(0,0,w,h);
            g2.dispose();
        }
    }
}
