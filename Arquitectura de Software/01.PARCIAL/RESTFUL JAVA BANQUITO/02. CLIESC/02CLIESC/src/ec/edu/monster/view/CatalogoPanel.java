package ec.edu.monster.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * Panel con estética mejorada para el catálogo de productos.
 */
public class CatalogoPanel extends JPanel {

    // --- CONSTANTES DE DISEÑO ---
    private static final Color HEADER_BG_COLOR = new Color(45, 45, 45);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color SELECTION_COLOR = new Color(60, 179, 113, 100); // Verde menta semi-transparente
    private static final Color GRID_COLOR = new Color(80, 80, 80);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_CELL = new Font("Segoe UI", Font.PLAIN, 13);
    
    // --- COMPONENTES ---
    private JTable tblCatalogo;
    private JLabel lblProductoSeleccionado;

    public CatalogoPanel() {
        initComponents();
    }

    // --- GETTERS ---
    public JTable getTblCatalogo() { return tblCatalogo; }
    public JLabel getLblProductoSeleccionado() { return lblProductoSeleccionado; }
    
    private void initComponents() {
        this.setLayout(new BorderLayout(10, 15));
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20)); // Padding general

        // 1. Panel del Encabezado (Título + Instrucción)
        JPanel headerPanel = createHeaderPanel();
        this.add(headerPanel, BorderLayout.NORTH);

        // 2. Panel de la Tabla (con JScrollPane)
        JScrollPane scrollPane = createTableScrollPane();
        this.add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 5));
        headerPanel.setOpaque(false);
        
        JLabel lblTitle = new JLabel("Catálogo de Productos");
        lblTitle.setFont(FONT_TITLE);
        lblTitle.setForeground(TEXT_COLOR);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblProductoSeleccionado = new JLabel("Seleccione un producto de la tabla para comprar.");
        lblProductoSeleccionado.setFont(FONT_CELL.deriveFont(Font.ITALIC));
        lblProductoSeleccionado.setForeground(new Color(200, 200, 200)); // Un gris claro en lugar de amarillo
        lblProductoSeleccionado.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblProductoSeleccionado, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JScrollPane createTableScrollPane() {
        // Crear la tabla con el modelo de datos
        tblCatalogo = new JTable(new DefaultTableModel(new Object[]{"ID", "Nombre", "Descripción", "Precio"}, 0));
        
        // --- ESTILOS DE LA TABLA ---
        tblCatalogo.setOpaque(false);
        tblCatalogo.setFont(FONT_CELL);
        tblCatalogo.setForeground(TEXT_COLOR);
        tblCatalogo.setGridColor(GRID_COLOR);
        tblCatalogo.setRowHeight(30);
        tblCatalogo.setShowVerticalLines(false); // Diseño más limpio sin líneas verticales
        tblCatalogo.setSelectionBackground(SELECTION_COLOR);
        tblCatalogo.setSelectionForeground(TEXT_COLOR);

        // Permitir ordenar por columnas al hacer clic en el encabezado
        tblCatalogo.setAutoCreateRowSorter(true);

        // --- ESTILO DEL ENCABEZADO DE LA TABLA ---
        JTableHeader tableHeader = tblCatalogo.getTableHeader();
        tableHeader.setFont(FONT_HEADER);
        tableHeader.setForeground(TEXT_COLOR);
        tableHeader.setBackground(HEADER_BG_COLOR);
        tableHeader.setOpaque(false);
        tableHeader.setDefaultRenderer(new HeaderRenderer()); // Usar nuestro renderizador personalizado
        
        // --- RENDERIZADORES DE CELDA PERSONALIZADOS ---
        // Renderizador base para todas las celdas
        DefaultTableCellRenderer baseRenderer = new CustomCellRenderer();
        // Renderizador para celdas que deben estar centradas
        DefaultTableCellRenderer centeredRenderer = new CustomCellRenderer();
        centeredRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        tblCatalogo.setDefaultRenderer(Object.class, baseRenderer);
        tblCatalogo.getColumnModel().getColumn(0).setCellRenderer(centeredRenderer); // Centrar ID
        tblCatalogo.getColumnModel().getColumn(3).setCellRenderer(centeredRenderer); // Centrar Precio

        // --- AJUSTE DE ANCHO DE COLUMNAS ---
        TableColumnModel columnModel = tblCatalogo.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);   // ID
        columnModel.getColumn(1).setPreferredWidth(150);  // Nombre
        columnModel.getColumn(2).setPreferredWidth(250);  // Descripción (más ancha)
        columnModel.getColumn(3).setPreferredWidth(80);   // Precio

        // --- CONFIGURACIÓN DEL SCROLLPANE ---
        JScrollPane scrollPane = new JScrollPane(tblCatalogo);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 1)); // Borde sutil
        
        return scrollPane;
    }
    
    /**
     * Renderizador personalizado para las celdas de la tabla.
     * Hace el fondo transparente y añade padding vertical.
     */
    private class CustomCellRenderer extends DefaultTableCellRenderer {
        public CustomCellRenderer() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            // El color de fondo de la selección ya está manejado por JTable.setSelectionBackground
            // Aquí solo nos aseguramos de que el componente sea transparente cuando no está seleccionado.
            if (!isSelected) {
                c.setBackground(new Color(0, 0, 0, 0));
            }
            return c;
        }
    }
    
    /**
     * Renderizador personalizado para el encabezado de la tabla.
     * Permite establecer un fondo, fuente y padding personalizados.
     */
    private class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setOpaque(true); // El encabezado sí tiene un fondo sólido
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(HEADER_BG_COLOR);
            setForeground(TEXT_COLOR);
            setFont(FONT_HEADER);
            setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5)); // Padding vertical
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            return this;
        }
    }
}