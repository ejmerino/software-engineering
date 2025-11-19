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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ednan
 */
@Entity
@Table(name = "movimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Movimiento.findAll", query = "SELECT m FROM Movimiento m")
    , @NamedQuery(name = "Movimiento.findByCodMovimiento", query = "SELECT m FROM Movimiento m WHERE m.codMovimiento = :codMovimiento")
    , @NamedQuery(name = "Movimiento.findByTipo", query = "SELECT m FROM Movimiento m WHERE m.tipo = :tipo")
    , @NamedQuery(name = "Movimiento.findByValor", query = "SELECT m FROM Movimiento m WHERE m.valor = :valor")
    , @NamedQuery(name = "Movimiento.findByFecha", query = "SELECT m FROM Movimiento m WHERE m.fecha = :fecha")})
public class Movimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD_MOVIMIENTO")
    private Integer codMovimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "TIPO")
    private String tipo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "NUM_CUENTA", referencedColumnName = "NUM_CUENTA")
    @ManyToOne(optional = false)
    private Cuenta numCuenta;

    public Movimiento() {
    }

    public Movimiento(Integer codMovimiento) {
        this.codMovimiento = codMovimiento;
    }

    public Movimiento(Integer codMovimiento, String tipo, BigDecimal valor, Date fecha) {
        this.codMovimiento = codMovimiento;
        this.tipo = tipo;
        this.valor = valor;
        this.fecha = fecha;
    }

    public Integer getCodMovimiento() {
        return codMovimiento;
    }

    public void setCodMovimiento(Integer codMovimiento) {
        this.codMovimiento = codMovimiento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Cuenta getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(Cuenta numCuenta) {
        this.numCuenta = numCuenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codMovimiento != null ? codMovimiento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movimiento)) {
            return false;
        }
        Movimiento other = (Movimiento) object;
        if ((this.codMovimiento == null && other.codMovimiento != null) || (this.codMovimiento != null && !this.codMovimiento.equals(other.codMovimiento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.controller.Movimiento[ codMovimiento=" + codMovimiento + " ]";
    }
    
}
