package ec.edu.monster.model;

import java.math.BigDecimal;

public class FacturaDetalle {
    private Integer idDetalle;
    private Integer cantidad;
    private BigDecimal precioUnitarioVenta;
    private BigDecimal subtotalLinea;
    // Solo necesitamos esto para mostrar datos básicos
    
    // Getters
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}