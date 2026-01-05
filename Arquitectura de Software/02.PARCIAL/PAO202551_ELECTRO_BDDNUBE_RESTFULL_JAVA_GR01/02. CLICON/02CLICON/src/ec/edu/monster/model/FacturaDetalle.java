package ec.edu.monster.model;

import java.math.BigDecimal;

public class FacturaDetalle {
    private Integer idDetalle;
    private Electrodomestico electrodomestico; // Objeto completo para sacar el nombre
    private Integer cantidad;
    private BigDecimal precioUnitarioVenta;
    private BigDecimal subtotalLinea;

    public FacturaDetalle() {}

    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }

    public Electrodomestico getElectrodomestico() { return electrodomestico; }
    public void setElectrodomestico(Electrodomestico electrodomestico) { this.electrodomestico = electrodomestico; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitarioVenta() { return precioUnitarioVenta; }
    public void setPrecioUnitarioVenta(BigDecimal precioUnitarioVenta) { this.precioUnitarioVenta = precioUnitarioVenta; }

    public BigDecimal getSubtotalLinea() { return subtotalLinea; }
    public void setSubtotalLinea(BigDecimal subtotalLinea) { this.subtotalLinea = subtotalLinea; }
}