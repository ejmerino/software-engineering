package ec.edu.monster.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Amortizacion_Detalle")
public class AmortizacionDetalle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_amortizacion")
    private Integer idAmortizacion;

    @Column(name = "numero_cuota")
    private int numeroCuota;

    @Column(name = "fecha_pago_programada")
    @Temporal(TemporalType.DATE)
    private Date fechaPagoProgramada;

    @Column(name = "valor_cuota")
    private BigDecimal valorCuota;

    @Column(name = "interes_pagado")
    private BigDecimal interesPagado;

    @Column(name = "capital_pagado")
    private BigDecimal capitalPagado;

    @Column(name = "saldo_capital")
    private BigDecimal saldoCapital;

    @ManyToOne
    @JoinColumn(name = "id_credito", nullable = false)
    private Credito credito;

    public AmortizacionDetalle() {}
    // Generar Getters y Setters...
    // --- PEGA ESTO EN AmortizacionDetalle.java ---

    public Credito getCredito() {
        return credito;
    }

    public void setCredito(Credito credito) {
        this.credito = credito;
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
}