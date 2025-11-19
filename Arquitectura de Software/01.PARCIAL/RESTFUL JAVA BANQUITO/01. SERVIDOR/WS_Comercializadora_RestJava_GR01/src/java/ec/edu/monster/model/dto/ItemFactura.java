package ec.edu.monster.model.dto;

import java.math.BigDecimal;

public class ItemFactura {
    private int idElectrodomestico;
    private int cantidad;
    private BigDecimal precioUnitario; // <-- Campo esencial para la facturación

    // Constructor vacío (SOLUCIONA EL ERROR DE COMPILACIÓN EN EL TEST)
    public ItemFactura() {
    } 
    
    // Constructor con argumentos (Para la lógica de la petición)
    public ItemFactura(int idElectrodomestico, int cantidad) {
        this.idElectrodomestico = idElectrodomestico;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public int getIdElectrodomestico() { return idElectrodomestico; }
    public void setIdElectrodomestico(int idElectrodomestico) { this.idElectrodomestico = idElectrodomestico; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}