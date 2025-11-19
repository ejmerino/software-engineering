package ec.edu.monster.model.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * POJO/DTO para enviar la tabla de amortización como JSON limpio,
 * en lugar de la Entidad JPA.
 */
public class AmortizacionDetalleDTO {

    private int idAmortizacion;
    private int numeroCuota;
    private Date fechaPagoProgramada;
    private BigDecimal valorCuota;
    private BigDecimal interesPagado;
    private BigDecimal capitalPagado;
    private BigDecimal saldoCapital;

    // Getters y Setters
    public int getIdAmortizacion() { return idAmortizacion; }
    public void setIdAmortizacion(int idAmortizacion) { this.idAmortizacion = idAmortizacion; }
    public int getNumeroCuota() { return numeroCuota; }
    public void setNumeroCuota(int numeroCuota) { this.numeroCuota = numeroCuota; }
    public Date getFechaPagoProgramada() { return fechaPagoProgramada; }
    public void setFechaPagoProgramada(Date fechaPagoProgramada) { this.fechaPagoProgramada = fechaPagoProgramada; }
    public BigDecimal getValorCuota() { return valorCuota; }
    public void setValorCuota(BigDecimal valorCuota) { this.valorCuota = valorCuota; }
    public BigDecimal getInteresPagado() { return interesPagado; }
    public void setInteresPagado(BigDecimal interesPagado) { this.interesPagado = interesPagado; }
    public BigDecimal getCapitalPagado() { return capitalPagado; }
    public void setCapitalPagado(BigDecimal capitalPagado) { this.capitalPagado = capitalPagado; }
    public BigDecimal getSaldoCapital() { return saldoCapital; }
    public void setSaldoCapital(BigDecimal saldoCapital) { this.saldoCapital = saldoCapital; }
}