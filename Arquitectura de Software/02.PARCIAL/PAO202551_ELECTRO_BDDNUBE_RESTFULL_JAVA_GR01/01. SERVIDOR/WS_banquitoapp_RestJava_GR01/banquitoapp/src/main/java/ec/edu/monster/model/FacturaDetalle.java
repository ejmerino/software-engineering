package ec.edu.monster.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "Factura_Detalle")
public class FacturaDetalle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_factura", nullable = false)
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "id_electrodomestico", nullable = false)
    private Electrodomestico electrodomestico;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario_venta", nullable = false)
    private BigDecimal precioUnitarioVenta;

    @Column(name = "subtotal_linea", nullable = false)
    private BigDecimal subtotalLinea;

    public FacturaDetalle() {}

    // --- GETTERS Y SETTERS OBLIGATORIOS ---

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Electrodomestico getElectrodomestico() {
        return electrodomestico;
    }

    // ESTE ES EL QUE TE FALTABA Y DABA ERROR
    public void setElectrodomestico(Electrodomestico electrodomestico) {
        this.electrodomestico = electrodomestico;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    // ESTE TAMBIÉN FALTABA
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitarioVenta() {
        return precioUnitarioVenta;
    }

    // ESTE TAMBIÉN
    public void setPrecioUnitarioVenta(BigDecimal precioUnitarioVenta) {
        this.precioUnitarioVenta = precioUnitarioVenta;
    }

    public BigDecimal getSubtotalLinea() {
        return subtotalLinea;
    }

    // Y ESTE
    public void setSubtotalLinea(BigDecimal subtotalLinea) {
        this.subtotalLinea = subtotalLinea;
    }
}