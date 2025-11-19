package ec.edu.monster.model;

import java.math.BigDecimal;
import java.util.Date;
// (Esta clase es un POJO, no una entidad, porque usamos JDBC)
public class Factura {
    private int idFactura;
    private String cedulaCliente;
    private Date fecha;
    private String formaPago;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private int idCreditoBanco;

    // Añade Getters y Setters para todos los campos
    // (Gson los necesita para serializar)
    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }
    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public int getIdCreditoBanco() { return idCreditoBanco; }
    public void setIdCreditoBanco(int idCreditoBanco) { this.idCreditoBanco = idCreditoBanco; }
}