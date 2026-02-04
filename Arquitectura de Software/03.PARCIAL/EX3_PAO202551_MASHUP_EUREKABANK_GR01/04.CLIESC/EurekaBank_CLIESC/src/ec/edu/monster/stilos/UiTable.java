package ec.edu.monster.stilos;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public final class UiTable {

    public static void setup(JTable t) {
        t.setFillsViewportHeight(true);
        t.setRowHeight(26);
        t.getTableHeader().setReorderingAllowed(false);

        // Zebra
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) c.setBackground(row % 2 == 0 ? new Color(255,255,255,200) : new Color(245,245,245,200));
                return c;
            }
        });

        // Alinear números a la derecha
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        t.getColumnModel().getColumn(2).setCellRenderer(right); // Importe
        t.getColumnModel().getColumn(3).setCellRenderer(right); // Saldo
    }

    private UiTable(){}
}
