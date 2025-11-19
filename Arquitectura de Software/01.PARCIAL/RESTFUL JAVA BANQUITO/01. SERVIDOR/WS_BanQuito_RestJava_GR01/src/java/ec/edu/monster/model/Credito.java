/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ednan
 */
@Entity
@Table(name = "credito")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Credito.findAll", query = "SELECT c FROM Credito c")
    , @NamedQuery(name = "Credito.findByIdCredito", query = "SELECT c FROM Credito c WHERE c.idCredito = :idCredito")
    , @NamedQuery(name = "Credito.findByMontoPrestamo", query = "SELECT c FROM Credito c WHERE c.montoPrestamo = :montoPrestamo")
    , @NamedQuery(name = "Credito.findByTasaInteresAnual", query = "SELECT c FROM Credito c WHERE c.tasaInteresAnual = :tasaInteresAnual")
    , @NamedQuery(name = "Credito.findByNumeroCuotas", query = "SELECT c FROM Credito c WHERE c.numeroCuotas = :numeroCuotas")
    , @NamedQuery(name = "Credito.findByValorCuotaFija", query = "SELECT c FROM Credito c WHERE c.valorCuotaFija = :valorCuotaFija")
    , @NamedQuery(name = "Credito.findByFechaAprobacion", query = "SELECT c FROM Credito c WHERE c.fechaAprobacion = :fechaAprobacion")
    , @NamedQuery(name = "Credito.findByEstado", query = "SELECT c FROM Credito c WHERE c.estado = :estado")})
public class Credito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_credito")
    private Integer idCredito;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto_prestamo")
    private BigDecimal montoPrestamo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tasa_interes_anual")
    private BigDecimal tasaInteresAnual;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_cuotas")
    private int numeroCuotas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_cuota_fija")
    private BigDecimal valorCuotaFija;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAprobacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "estado")
    private String estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCredito")
    private Collection<AmortizacionDetalle> amortizacionDetalleCollection;
    @JoinColumn(name = "cedula_cliente", referencedColumnName = "CEDULA")
    @ManyToOne(optional = false)
    private Cliente cedulaCliente;

    public Credito() {
    }

    public Credito(Integer idCredito) {
        this.idCredito = idCredito;
    }

    public Credito(Integer idCredito, BigDecimal montoPrestamo, BigDecimal tasaInteresAnual, int numeroCuotas, BigDecimal valorCuotaFija, Date fechaAprobacion, String estado) {
        this.idCredito = idCredito;
        this.montoPrestamo = montoPrestamo;
        this.tasaInteresAnual = tasaInteresAnual;
        this.numeroCuotas = numeroCuotas;
        this.valorCuotaFija = valorCuotaFija;
        this.fechaAprobacion = fechaAprobacion;
        this.estado = estado;
    }

    public Integer getIdCredito() {
        return idCredito;
    }

    public void setIdCredito(Integer idCredito) {
        this.idCredito = idCredito;
    }

    public BigDecimal getMontoPrestamo() {
        return montoPrestamo;
    }

    public void setMontoPrestamo(BigDecimal montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public BigDecimal getTasaInteresAnual() {
        return tasaInteresAnual;
    }

    public void setTasaInteresAnual(BigDecimal tasaInteresAnual) {
        this.tasaInteresAnual = tasaInteresAnual;
    }

    public int getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(int numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public BigDecimal getValorCuotaFija() {
        return valorCuotaFija;
    }

    public void setValorCuotaFija(BigDecimal valorCuotaFija) {
        this.valorCuotaFija = valorCuotaFija;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<AmortizacionDetalle> getAmortizacionDetalleCollection() {
        return amortizacionDetalleCollection;
    }

    public void setAmortizacionDetalleCollection(Collection<AmortizacionDetalle> amortizacionDetalleCollection) {
        this.amortizacionDetalleCollection = amortizacionDetalleCollection;
    }

    public Cliente getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(Cliente cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCredito != null ? idCredito.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credito)) {
            return false;
        }
        Credito other = (Credito) object;
        if ((this.idCredito == null && other.idCredito != null) || (this.idCredito != null && !this.idCredito.equals(other.idCredito))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.controller.Credito[ idCredito=" + idCredito + " ]";
    }
    
}
