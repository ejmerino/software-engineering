package ec.edu.monster.model.dto;

import java.math.BigDecimal;

public class ItemFactura {
    private int idElectrodomestico;
    private int cantidad;
    private BigDecimal precioUnitario; // Este campo es llenado por el servidor después de la validación.

    // Constructor vacío
    public ItemFactura() {
    } 
    
    // Constructor con argumentos (Usado en ClienteController para el carrito)
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