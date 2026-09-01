package ec.edu.monster.cuentas.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "cuenta")
@Data
public class Cuenta {
    @Id
    @Column(name = "chr_cuencodigo")
    private String codigo;

    @Column(name = "chr_monecodigo")
    private String moneda;

    @Column(name = "chr_sucucodigo")
    private String sucursal;

    @Column(name = "chr_emplcreacuenta")
    private String empleadoCrea;

    @Column(name = "chr_cliecodigo")
    private String cliente;

    @Column(name = "dec_cuensaldo")
    private BigDecimal saldo;

    @Column(name = "dtt_cuenfechacreacion")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @Column(name = "vch_cuenestado")
    private String estado;

    @Column(name = "int_cuencontmov")
    private Integer contadorMov;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getEmpleadoCrea() {
        return empleadoCrea;
    }

    public void setEmpleadoCrea(String empleadoCrea) {
        this.empleadoCrea = empleadoCrea;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getContadorMov() {
        return contadorMov;
    }

    public void setContadorMov(Integer contadorMov) {
        this.contadorMov = contadorMov;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEstadoBloqueo() {
        return estadoBloqueo;
    }

    public void setEstadoBloqueo(String estadoBloqueo) {
        this.estadoBloqueo = estadoBloqueo;
    }

    @Column(name = "chr_cuenclave")
    private String clave;

    @Column(name = "estado_bloqueo")
    private String estadoBloqueo; // 'LIBRE' o 'BLOQUEADO'
}