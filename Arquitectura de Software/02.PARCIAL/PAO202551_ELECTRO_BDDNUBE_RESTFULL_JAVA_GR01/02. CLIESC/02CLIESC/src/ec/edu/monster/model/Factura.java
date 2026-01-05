package ec.edu.monster.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Factura {

    private Integer idFactura;
    private Cliente cliente; // <--- ESTO FALTABA (Objeto completo, no solo String)
    private Date fecha;
    private String formaPago;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private Integer idCreditoBanco;
    private List<FacturaDetalle> detalles; 

    public Factura() {
    }

    // --- GETTERS Y SETTERS ---

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    // --- ESTE ES EL MÉTODO QUE TE DABA ERROR ---
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    // ------------------------------------------

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getIdCreditoBanco() {
        return idCreditoBanco;
    }

    public void setIdCreditoBanco(Integer idCreditoBanco) {
        this.idCreditoBanco = idCreditoBanco;
    }

    public List<FacturaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<FacturaDetalle> detalles) {
        this.detalles = detalles;
    }
    
    // Método de compatibilidad por si tu código viejo usaba getItems()
    public List<FacturaDetalle> getItems() {
        return detalles;
    }
}