/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ednan
 */
@Entity
@Table(name = "amortizacion_detalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AmortizacionDetalle.findAll", query = "SELECT a FROM AmortizacionDetalle a")
    , @NamedQuery(name = "AmortizacionDetalle.findByIdAmortizacion", query = "SELECT a FROM AmortizacionDetalle a WHERE a.idAmortizacion = :idAmortizacion")
    , @NamedQuery(name = "AmortizacionDetalle.findByNumeroCuota", query = "SELECT a FROM AmortizacionDetalle a WHERE a.numeroCuota = :numeroCuota")
    , @NamedQuery(name = "AmortizacionDetalle.findByFechaPagoProgramada", query = "SELECT a FROM AmortizacionDetalle a WHERE a.fechaPagoProgramada = :fechaPagoProgramada")
    , @NamedQuery(name = "AmortizacionDetalle.findByValorCuota", query = "SELECT a FROM AmortizacionDetalle a WHERE a.valorCuota = :valorCuota")
    , @NamedQuery(name = "AmortizacionDetalle.findByInteresPagado", query = "SELECT a FROM AmortizacionDetalle a WHERE a.interesPagado = :interesPagado")
    , @NamedQuery(name = "AmortizacionDetalle.findByCapitalPagado", query = "SELECT a FROM AmortizacionDetalle a WHERE a.capitalPagado = :capitalPagado")
    , @NamedQuery(name = "AmortizacionDetalle.findBySaldoCapital", query = "SELECT a FROM AmortizacionDetalle a WHERE a.saldoCapital = :saldoCapital")})
public class AmortizacionDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_amortizacion")
    private Integer idAmortizacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_cuota")
    private int numeroCuota;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_pago_programada")
    @Temporal(TemporalType.DATE)
    private Date fechaPagoProgramada;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_cuota")
    private BigDecimal valorCuota;
    @Basic(optional = false)
    @NotNull
    @Column(name = "interes_pagado")
    private BigDecimal interesPagado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "capital_pagado")
    private BigDecimal capitalPagado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "saldo_capital")
    private BigDecimal saldoCapital;
    @JoinColumn(name = "id_credito", referencedColumnName = "id_credito")
    @ManyToOne(optional = false)
    private Credito idCredito;

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

    public Credito getIdCredito() {
        return idCredito;
    }

    public void setIdCredito(Credito idCredito) {
        this.idCredito = idCredito;
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
        return "ec.edu.monster.controller.AmortizacionDetalle[ idAmortizacion=" + idAmortizacion + " ]";
    }
    
}
