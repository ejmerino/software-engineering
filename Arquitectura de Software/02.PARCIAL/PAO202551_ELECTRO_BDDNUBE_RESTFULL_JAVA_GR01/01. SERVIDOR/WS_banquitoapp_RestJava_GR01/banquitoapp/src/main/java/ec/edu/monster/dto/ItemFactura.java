package ec.edu.monster.dto;

import java.math.BigDecimal;

public class ItemFactura {
    private int idElectrodomestico;
    private int cantidad;

    // Este campo lo llenamos en el backend con el precio real de la base de datos
    private BigDecimal precioUnitario;

    public ItemFactura() {
    }

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