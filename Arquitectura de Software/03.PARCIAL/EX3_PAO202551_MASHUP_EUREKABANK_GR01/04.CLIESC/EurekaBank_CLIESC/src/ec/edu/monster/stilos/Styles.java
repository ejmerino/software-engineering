package ec.edu.monster.stilos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class Styles {

    public static JPanel card(int padding) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(padding,padding,padding,padding));
        return roundWrapper(p);
    }

    public static JButton primary(String text) {
        JButton b = new JButton(text);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(75,0,130));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10,18,10,18));
        return b;
    }

    public static JButton ghost(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(new Color(255,255,255,160));
        b.setBorder(BorderFactory.createEmptyBorder(10,14,10,14));
        return b;
    }

    public static JTextField field() {
        JTextField t = new JTextField();
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210,210,220)),
                BorderFactory.createEmptyBorder(8,10,8,10)));
        return t;
    }

    public static JPasswordField pass() {
        JPasswordField t = new JPasswordField();
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210,210,220)),
                BorderFactory.createEmptyBorder(8,10,8,10)));
        return t;
    }

    public static JLabel h1(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 22f));
        return l;
    }

    public static JPanel row(int gap) {
        JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT, gap, 0));
        r.setOpaque(false);
        return r;
    }

    private static JPanel roundWrapper(JPanel content) {
        return new JPanel(new BorderLayout()) {
            { setOpaque(false); super.add(content, BorderLayout.CENTER); }
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255,255,255,200));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),24,24);
                g2.setColor(new Color(0,0,0,30));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,24,24);
                g2.dispose();
            }
        };
    }

    private Styles(){}
}
