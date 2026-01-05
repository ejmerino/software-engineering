package ec.edu.monster.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Este es un POJO (un "molde") en el proyecto Cliente de Consola.
 * Solo se usa para que Gson pueda deserializar la respuesta JSON del servidor.
 */
public class AmortizacionDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // Estos nombres deben coincidir EXACTAMENTE con el JSON que envía el servidor
    private Integer idAmortizacion;
    private int numeroCuota;
    private Date fechaPagoProgramada;
    private BigDecimal valorCuota;
    private BigDecimal interesPagado;
    private BigDecimal capitalPagado;
    private BigDecimal saldoCapital;

    // Getters y Setters (necesarios para que Gson funcione)
    
    public Integer getIdAmortizacion() {
        return idAmortizacion;
    }

    public void setIdAmortizacion(Integer idAmortizacion) {
        this.idAmortizacion = idAmortizacion;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public Date getFechaPagoProgramada() {
        return fechaPagoProgramada;
    }

    public void setFechaPagoProgramada(Date fechaPagoProgramada) {
        this.fechaPagoProgramada = fechaPagoProgramada;
    }

    public BigDecimal getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(BigDecimal valorCuota) {
        this.valorCuota = valorCuota;
    }

    public BigDecimal getInteresPagado() {
        return interesPagado;
    }

    public void setInteresPagado(BigDecimal interesPagado) {
        this.interesPagado = interesPagado;
    }

    public BigDecimal getCapitalPagado() {
        return capitalPagado;
    }

    public void setCapitalPagado(BigDecimal capitalPagado) {
        this.capitalPagado = capitalPagado;
    }

    public BigDecimal getSaldoCapital() {
        return saldoCapital;
    }

    public void setSaldoCapital(BigDecimal saldoCapital) {
        this.saldoCapital = saldoCapital;
    }
}