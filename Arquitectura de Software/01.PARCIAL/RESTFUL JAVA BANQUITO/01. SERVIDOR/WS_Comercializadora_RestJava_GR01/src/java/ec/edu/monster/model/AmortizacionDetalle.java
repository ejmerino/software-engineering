package ec.edu.monster.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Este es un POJO (un "molde") en el proyecto Comercializadora.
 * Solo se usa para que Gson pueda deserializar la respuesta JSON del servidor BanQuito.
 * * NO es una entidad de base de datos (@Entity) en este proyecto.
 */
public class AmortizacionDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer idAmortizacion;
    private int numeroCuota;
    private Date fechaPagoProgramada;
    private BigDecimal valorCuota;
    private BigDecimal interesPagado;
    private BigDecimal capitalPagado;
    private BigDecimal saldoCapital;
    
    // --- ¡HEMOS BORRADO LA VARIABLE 'idCredito' Y SUS MÉTODOS GET/SET! ---
    // Ya no existe la dependencia a la clase 'Credito'.

    public AmortizacionDetalle() {
    }

    public AmortizacionDetalle(Integer idAmortizacion) {
        this.idAmortizacion = idAmortizacion;
    }

    public AmortizacionDetalle(Integer idAmortizacion, int numeroCuota, Date fechaPagoProgramada, BigDecimal valorCuota, BigDecimal interesPagado, BigDecimal capitalPagado, BigDecimal saldoCapital) {
        this.idAmortizacion = idAmortizacion;
        this.numeroCuota = numeroCuota;
        this.fechaPagoProgramada = fechaPagoProgramada;
        this.valorCuota = valorCuota;
        this.interesPagado = interesPagado;
        this.capitalPagado = capitalPagado;
        this.saldoCapital = saldoCapital;
    }

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAmortizacion != null ? idAmortizacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AmortizacionDetalle)) {
            return false;
        }
        AmortizacionDetalle other = (AmortizacionDetalle) object;
        if ((this.idAmortizacion == null && other.idAmortizacion != null) || (this.idAmortizacion != null && !this.idAmortizacion.equals(other.idAmortizacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        // Corregimos el toString para que no dé error
        return "ec.edu.monster.model.AmortizacionDetalle[ idAmortizacion=" + idAmortizacion + " ]";
    }
    
}